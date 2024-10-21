package code.nebula.cipherquest.advisor

import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor
import org.springframework.ai.chat.client.advisor.api.AdvisedRequest
import org.springframework.ai.chat.client.advisor.api.AdvisedResponse
import org.springframework.ai.chat.client.advisor.api.CallAroundAdvisorChain
import org.springframework.ai.chat.model.ChatResponse
import org.springframework.ai.document.Document
import org.springframework.ai.vectorstore.SearchRequest
import org.springframework.ai.vectorstore.VectorStore
import java.util.stream.Collectors

class TitleQuestionAnswerAdvisor : QuestionAnswerAdvisor {
    private var userTextAdvise: String? = null
    private var vectorStore: VectorStore? = null
    private var searchRequest: SearchRequest? = null

    constructor(vectorStore: VectorStore) : super(vectorStore) {
        this.vectorStore = vectorStore
    }

    constructor(vectorStore: VectorStore, searchRequest: SearchRequest) : super(vectorStore, searchRequest) {
        this.vectorStore = vectorStore
        this.searchRequest = searchRequest
    }

    constructor(vectorStore: VectorStore, searchRequest: SearchRequest, userTextAdvise: String) : super(
        vectorStore,
        searchRequest,
        userTextAdvise,
    ) {
        this.vectorStore = vectorStore
        this.searchRequest = searchRequest
        this.userTextAdvise = userTextAdvise
    }

    override fun aroundCall(
        advisedRequest: AdvisedRequest,
        chain: CallAroundAdvisorChain,
    ): AdvisedResponse {
        val advisedRequest2 = before(advisedRequest)
        val advisedResponse = chain.nextAroundCall(advisedRequest2)
        return after(advisedResponse)
    }

    private fun before(request: AdvisedRequest): AdvisedRequest {
        val context = HashMap(request.adviseContext())

        // 1. Advise the system text.
        val advisedUserText = request.userText() + System.lineSeparator() + this.userTextAdvise

        val searchRequestToUse =
            SearchRequest
                .from(this.searchRequest)
                .withQuery(request.userText())
                .withFilterExpression(doGetFilterExpression(context))

        // 2. Search for similar documents in the vector store.
        val documents = vectorStore?.similaritySearch(searchRequestToUse)

        if (documents != null) {
            context[RETRIEVED_DOCUMENTS] = documents
        }

        // 3. Create the context from the documents.
        val documentContext =
            documents
                ?.stream()
                ?.map { obj: Document -> "Title: " + obj.metadata["file_name"].toString() + System.lineSeparator() + obj.content }
                ?.collect(Collectors.joining(System.lineSeparator()))

        // 4. Advise the user parameters.
        val advisedUserParams: MutableMap<String, Any> = HashMap(request.userParams())
        advisedUserParams["question_answer_context"] = documentContext!!

        val advisedRequest =
            AdvisedRequest
                .from(request)
                .withUserText(advisedUserText)
                .withUserParams(advisedUserParams)
                .build()

        return advisedRequest
    }

    private fun after(advisedResponse: AdvisedResponse): AdvisedResponse {
        val chatResponseBuilder = ChatResponse.builder().from(advisedResponse.response())
        chatResponseBuilder.withMetadata(
            RETRIEVED_DOCUMENTS,
            advisedResponse.adviseContext()[RETRIEVED_DOCUMENTS],
        )
        return AdvisedResponse(chatResponseBuilder.build(), advisedResponse.adviseContext())
    }
}
