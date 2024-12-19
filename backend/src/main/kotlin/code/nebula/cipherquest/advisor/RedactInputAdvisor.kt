package code.nebula.cipherquest.advisor

import code.nebula.cipherquest.models.DocumentType
import org.springframework.ai.chat.client.advisor.api.AdvisedRequest
import org.springframework.ai.chat.client.advisor.api.AdvisedResponse
import org.springframework.ai.chat.client.advisor.api.CallAroundAdvisor
import org.springframework.ai.chat.client.advisor.api.CallAroundAdvisorChain
import org.springframework.ai.chat.messages.AssistantMessage
import org.springframework.ai.chat.model.ChatResponse
import org.springframework.ai.chat.model.Generation
import org.springframework.ai.vectorstore.SearchRequest
import org.springframework.ai.vectorstore.VectorStore
import org.springframework.core.Ordered
import org.springframework.stereotype.Service

@Service
class RedactInputAdvisor(
    private val vectorStore: VectorStore,
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
        if (checkQuestion(advisedRequest.userText)) {
            val id = doGetConversationId(advisedRequest.adviseContext)

            return AdvisedResponse(
                ChatResponse
                    .builder()
                    .withGenerations(
                        listOf(
                            Generation(
                                AssistantMessage(
                                    "Resource #$id, the answer is REDACTED for your safety.",
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

    fun checkQuestion(query: String): Boolean {
        val redactedQuestionFound =
            vectorStore
                .similaritySearch(
                    SearchRequest
                        .defaults()
                        .withSimilarityThreshold(SIMILARITY_THRESHOLD)
                        .withQuery(query)
                        .withFilterExpression("type == '${DocumentType.REDACTED}'"),
                ).size > 0

        return redactedQuestionFound
    }

    private fun doGetConversationId(context: Map<String, Any>) = context["chat_memory_conversation_id"].toString()
}
