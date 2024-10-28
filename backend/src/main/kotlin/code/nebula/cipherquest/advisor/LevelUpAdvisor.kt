package code.nebula.cipherquest.advisor

import code.nebula.cipherquest.models.DocumentType
import code.nebula.cipherquest.service.UserLevelService
import org.springframework.ai.chat.client.advisor.api.AdvisedRequest
import org.springframework.ai.chat.client.advisor.api.AdvisedResponse
import org.springframework.ai.chat.client.advisor.api.CallAroundAdvisor
import org.springframework.ai.chat.client.advisor.api.CallAroundAdvisorChain
import org.springframework.ai.vectorstore.SearchRequest
import org.springframework.ai.vectorstore.VectorStore
import org.springframework.stereotype.Service

@Service
class LevelUpAdvisor(
    private val vectorStore: VectorStore,
    private val userLevelService: UserLevelService,
) : CallAroundAdvisor {
    companion object {
        private const val LEVEL_UP_THRESHOLD = 0.82
    }

    override fun getOrder() = 0

    override fun getName(): String = javaClass.simpleName

    override fun aroundCall(
        advisedRequest: AdvisedRequest,
        chain: CallAroundAdvisorChain,
    ): AdvisedResponse {
        val id = doGetConversationId(advisedRequest.adviseContext)
        levelUp(id, advisedRequest.userText)

        return chain.nextAroundCall(advisedRequest)
    }

    fun levelUp(
        userId: String,
        query: String,
    ) {
        val userLevel = userLevelService.getLevelByUser(userId)

        val matchedQuestionLevel =
            vectorStore
                .similaritySearch(
                    SearchRequest
                        .defaults()
                        .withSimilarityThreshold(LEVEL_UP_THRESHOLD)
                        .withQuery(query)
                        .withFilterExpression("type == '${DocumentType.QUESTION}'"),
                ).minByOrNull { document -> document.metadata["distance"].toString().toFloat() }
                ?.metadata
                ?.get("level")
                ?.toString()
                ?.toInt()
                ?: 0

        if (matchedQuestionLevel == userLevel.level + 1) {
            userLevelService.increaseLevelTo(userId, matchedQuestionLevel)
        }
    }

    protected fun doGetConversationId(context: Map<String, Any>): String = context["chat_memory_conversation_id"].toString()
}
