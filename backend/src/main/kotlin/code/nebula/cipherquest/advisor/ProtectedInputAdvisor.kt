package code.nebula.cipherquest.advisor

import code.nebula.cipherquest.models.DocumentType
import code.nebula.cipherquest.service.VectorStoreService
import org.springframework.ai.chat.client.advisor.api.AdvisedRequest
import org.springframework.ai.chat.client.advisor.api.AdvisedResponse
import org.springframework.ai.chat.client.advisor.api.CallAroundAdvisor
import org.springframework.ai.chat.client.advisor.api.CallAroundAdvisorChain
import org.springframework.ai.chat.messages.AssistantMessage
import org.springframework.ai.chat.messages.MessageType
import org.springframework.ai.chat.model.ChatResponse
import org.springframework.ai.chat.model.Generation
import org.springframework.ai.vectorstore.SearchRequest
import org.springframework.ai.vectorstore.VectorStore
import org.springframework.core.Ordered
import org.springframework.stereotype.Service

@Service
class ProtectedInputAdvisor(
    private val vectorStore: VectorStore,
    private val vectorStoreService: VectorStoreService,
) : CallAroundAdvisor {
    companion object {
        private const val SIMILARITY_THRESHOLD = 0.95
    }

    override fun getOrder() = Ordered.HIGHEST_PRECEDENCE + 1

    override fun getName(): String = javaClass.simpleName

    override fun aroundCall(
        advisedRequest: AdvisedRequest,
        chain: CallAroundAdvisorChain,
    ): AdvisedResponse {
        if (hasMatchingProtectedDocuments(advisedRequest.userText)) {
            val id = doGetConversationId(advisedRequest.adviseContext)

            val message = "Resource #$id, the answer is REDACTED for your safety."

            vectorStoreService.saveMessage(id, advisedRequest.userText, MessageType.USER)
            vectorStoreService.saveMessage(id, message, MessageType.ASSISTANT)

            return AdvisedResponse(
                ChatResponse
                    .builder()
                    .generations(
                        listOf(
                            Generation(
                                AssistantMessage(
                                    message,
                                ),
                            ),
                        ),
                    ).build(),
                advisedRequest.adviseContext(),
            )
        } else {
            return chain.nextAroundCall(advisedRequest)
        }
    }

    /**
     * Checks if there are any matching documents of type `DocumentType.PROTECTED` based on the given query.
     *
     * @param query The search query to find matching protected documents.
     * @return `true` if there are matching protected documents, `false` otherwise.
     */
    fun hasMatchingProtectedDocuments(query: String) =
        (
            vectorStore
                .similaritySearch(
                    SearchRequest
                        .builder()
                        .similarityThreshold(SIMILARITY_THRESHOLD)
                        .query(query)
                        .filterExpression("type == '${DocumentType.PROTECTED}'")
                        .build(),
                )?.size ?: 0
        ) > 0

    private fun doGetConversationId(context: Map<String, Any>) = context["chat_memory_conversation_id"].toString()
}
