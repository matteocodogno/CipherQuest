package code.nebula.cipherquest.advisor

import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor
import org.springframework.ai.chat.client.advisor.api.AdvisedRequest
import org.springframework.ai.chat.client.advisor.api.AdvisedResponse
import org.springframework.ai.chat.client.advisor.api.CallAroundAdvisorChain
import org.springframework.ai.chat.model.ChatResponse
import org.springframework.ai.vectorstore.SearchRequest
import org.springframework.ai.vectorstore.VectorStore
import org.springframework.core.Ordered

class TitleQuestionAnswerAdvisor(
    private val vectorStore: VectorStore,
    private val searchRequest: SearchRequest = SearchRequest.defaults(),
    private val userTextAdvise: String = DEFAULT_USER_TEXT_ADVISE,
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
    }

    override fun getOrder(): Int = DEFAULT_CHAT_MEMORY_PRECEDENCE_ORDER + 10

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
                .withQuery(request.userText())
                .withFilterExpression(doGetFilterExpression(context))

        // 2. Search for similar documents in the vector store.
        val documents = vectorStore.similaritySearch(searchRequestToUse)

        if (documents != null) {
            context[RETRIEVED_DOCUMENTS] = documents
        }

        // 3. Create the context from the documents.
        val documentContext =
            documents
                ?.joinToString(
                    System.lineSeparator(),
                ) { document -> "Title: ${document.metadata["file_name"]}\n${document.content}" }

        // 4. Advise the user parameters.
        return AdvisedRequest
            .from(request)
            .withUserText(advisedUserText)
            .withUserParams(mapOf("question_answer_context" to documentContext))
            .build()
    }

    private fun after(advisedResponse: AdvisedResponse) =
        ChatResponse
            .builder()
            .from(advisedResponse.response())
            .run {
                withMetadata(
                    RETRIEVED_DOCUMENTS,
                    advisedResponse.adviseContext()[RETRIEVED_DOCUMENTS],
                )
                AdvisedResponse(build(), advisedResponse.adviseContext())
            }
}
