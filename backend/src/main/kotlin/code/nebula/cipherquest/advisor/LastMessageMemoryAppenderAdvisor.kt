package code.nebula.cipherquest.advisor

import code.nebula.cipherquest.service.VectorStoreService
import org.springframework.ai.chat.client.advisor.api.AdvisedRequest
import org.springframework.ai.chat.client.advisor.api.AdvisedResponse
import org.springframework.ai.chat.client.advisor.api.Advisor.DEFAULT_CHAT_MEMORY_PRECEDENCE_ORDER
import org.springframework.ai.chat.client.advisor.api.CallAroundAdvisor
import org.springframework.ai.chat.client.advisor.api.CallAroundAdvisorChain
import org.springframework.stereotype.Service

@Service
class LastMessageMemoryAppenderAdvisor(
    private val vectorStoreService: VectorStoreService,
) : CallAroundAdvisor {
    override fun getOrder() = DEFAULT_CHAT_MEMORY_PRECEDENCE_ORDER + 1

    override fun getName(): String = javaClass.simpleName

    override fun aroundCall(
        advisedRequest: AdvisedRequest,
        chain: CallAroundAdvisorChain,
    ): AdvisedResponse {
        val conversationId = doGetConversationId(advisedRequest.adviseContext)
        val lastMessage = vectorStoreService.getMessageHistoryByUserId(conversationId).lastOrNull()?.message

        return if (lastMessage == null) {
            chain.nextAroundCall(advisedRequest)
        } else {
            val updatedParams =
                advisedRequest.systemParams().toMutableMap().apply {
                    val currentMemory = this["long_term_memory"]?.toString().orEmpty()
                    if (lastMessage !in currentMemory) {
                        this["long_term_memory"] =
                            buildString {
                                append(currentMemory)
                                if (currentMemory.isNotEmpty()) appendLine()
                                append(lastMessage)
                            }
                    }
                }

            chain.nextAroundCall(
                AdvisedRequest
                    .from(advisedRequest)
                    .withSystemParams(updatedParams)
                    .build(),
            )
        }
    }

    private fun doGetConversationId(context: Map<String, Any>) = context["chat_memory_conversation_id"].toString()
}
