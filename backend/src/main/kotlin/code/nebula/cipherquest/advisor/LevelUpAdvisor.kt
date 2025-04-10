package code.nebula.cipherquest.advisor

import code.nebula.cipherquest.components.MessageContext
import code.nebula.cipherquest.service.UserLevelService
import org.springframework.ai.chat.client.advisor.api.AdvisedRequest
import org.springframework.ai.chat.client.advisor.api.AdvisedResponse
import org.springframework.ai.chat.client.advisor.api.CallAroundAdvisor
import org.springframework.ai.chat.client.advisor.api.CallAroundAdvisorChain
import org.springframework.ai.vectorstore.SearchRequest
import org.springframework.ai.vectorstore.VectorStore
import org.springframework.core.Ordered
import org.springframework.stereotype.Service

@Service
class LevelUpAdvisor(
    private val levelUpQuestionVectorStore: VectorStore,
    private val userLevelService: UserLevelService,
    private val messageContext: MessageContext,
) : CallAroundAdvisor {
    companion object {
        private const val LEVEL_UP_THRESHOLD = 0.85
    }

    override fun getOrder() = Ordered.HIGHEST_PRECEDENCE + 2

    override fun getName(): String = javaClass.simpleName

    override fun aroundCall(
        advisedRequest: AdvisedRequest,
        chain: CallAroundAdvisorChain,
    ): AdvisedResponse {
        val id = doGetConversationId(advisedRequest.adviseContext)
        val result = levelUp(id, advisedRequest.userText)

        messageContext.isLevelUp = result

        return chain.nextAroundCall(advisedRequest)
    }

    fun levelUp(
        userId: String,
        query: String,
    ): Boolean {
        val userLevel = userLevelService.getLevelByUser(userId)

        val matchedQuestionLevel =
            levelUpQuestionVectorStore
                .similaritySearch(
                    SearchRequest
                        .builder()
                        .query(query)
                        .similarityThreshold(LEVEL_UP_THRESHOLD)
                        .build(),
                )?.minByOrNull { document -> document.metadata["distance"].toString().toFloat() }
                ?.metadata
                ?.get("level")
                ?.toString()
                ?.toInt()
                ?: 0

        if (matchedQuestionLevel == userLevel.level + 1) {
            userLevelService.increaseLevelTo(userId, matchedQuestionLevel)
            return true
        }
        return false
    }

    private fun doGetConversationId(context: Map<String, Any>) = context["chat_memory_conversation_id"].toString()
}
