package code.nebula.cipherquest.advisor

import code.nebula.cipherquest.components.MessageContext
import code.nebula.cipherquest.models.dto.Source
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor
import org.springframework.ai.chat.client.advisor.api.AdvisedRequest
import org.springframework.ai.chat.client.advisor.api.AdvisedResponse
import org.springframework.ai.chat.client.advisor.api.CallAroundAdvisorChain
import org.springframework.ai.chat.model.ChatResponse
import org.springframework.ai.vectorstore.SearchRequest
import org.springframework.ai.vectorstore.VectorStore

class TitleQuestionAnswerAdvisor(
    private val vectorStore: VectorStore,
    private val searchRequest: SearchRequest = SearchRequest.builder().build(),
    private val userTextAdvise: String = DEFAULT_USER_TEXT_ADVISE,
    private val messageContext: MessageContext,
) : QuestionAnswerAdvisor(vectorStore, searchRequest, userTextAdvise) {
    companion object {
        private const val DEFAULT_USER_TEXT_ADVISE = """
            Context information is below.
            ---------------------
            {question_answer_context}
            ---------------------
            Given the context and provided history information and not prior knowledge,
            reply to the user comment. If the answer is not in the context, inform
            the user that you can't answer the question.
        """
        private const val ORDER_AFTER_CHAT_MEMORY = 10
    }

    override fun getOrder(): Int = DEFAULT_CHAT_MEMORY_PRECEDENCE_ORDER + ORDER_AFTER_CHAT_MEMORY

    override fun aroundCall(
        advisedRequest: AdvisedRequest,
        chain: CallAroundAdvisorChain,
    ) = before(advisedRequest)
        .let { chain.nextAroundCall(it) }
        .let { after(it) }

    private fun before(request: AdvisedRequest): AdvisedRequest {
        val context = HashMap(request.adviseContext())

        // 1. Advise the system text.
        val advisedUserText = "${request.userText()}\n$userTextAdvise"

        val searchRequestToUse =
            SearchRequest
                .from(this.searchRequest)
                .query(request.userText())
                .filterExpression(doGetFilterExpression(context))
                .build()

        // 2. Search for similar documents in the vector store.
        val documents = vectorStore.similaritySearch(searchRequestToUse)

        if (documents != null) {
//            TODO: Do we need it?
//            context[RETRIEVED_DOCUMENTS] = documents

            messageContext.sources =
                documents
                    .map { doc ->
                        Source(
                            doc.id,
                            doc.metadata["source"]
                                ?.toString()
                                ?.split(".")
                                ?.getOrNull(1)
                                .orEmpty(),
                        )
                    }.toList()
        }

        // 3. Create the context from the documents.
        val documentContext =
            documents
                ?.joinToString(
                    System.lineSeparator(),
                ) { document -> "Title: ${document.metadata["source"]}\n${document.text}" }

        // 4. Advise the user parameters.
        return AdvisedRequest
            .from(request)
            .userText(advisedUserText)
            .userParams(mapOf("question_answer_context" to documentContext))
            .build()
    }

    private fun after(advisedResponse: AdvisedResponse) =
        ChatResponse
            .builder()
            .from(advisedResponse.response())
            .run {
                metadata(
                    RETRIEVED_DOCUMENTS,
                    advisedResponse.adviseContext()[RETRIEVED_DOCUMENTS],
                )
                AdvisedResponse(build(), advisedResponse.adviseContext())
            }
}
