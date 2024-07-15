package code.nebula.cipherquest.advisor

import code.nebula.cipherquest.models.DocumentType
import code.nebula.cipherquest.repository.UserLevelRepository
import code.nebula.cipherquest.repository.entities.UserLevel
import org.springframework.ai.chat.client.AdvisedRequest
import org.springframework.ai.chat.client.RequestResponseAdvisor
import org.springframework.ai.chat.model.ChatResponse
import org.springframework.ai.vectorstore.SearchRequest
import org.springframework.ai.vectorstore.VectorStore
import org.springframework.stereotype.Service

@Service
class LevelUpAdvisor(
    private val vectorStore: VectorStore,
    private val userLevelRepository: UserLevelRepository,
) : RequestResponseAdvisor {
    companion object {
        private const val LEVEL_UP_THRESHOLD = 0.82
        private const val LEVEL_UP_COINS = 10
    }

    override fun adviseRequest(
        request: AdvisedRequest,
        context: Map<String, Any>,
    ): AdvisedRequest {
        val id = doGetConversationId(context)

        levelUp(id, request.userText)

        return request
    }


    override fun adviseResponse(
        response: ChatResponse,
        context: Map<String, Any>,
    ): ChatResponse {
        val id = doGetConversationId(context)

        levelUp(id, response.result.output.content)

        return response
    }

    fun levelUp(id: String, query: String) {
        val userLevel = userLevelRepository.findById(id).orElseThrow()

        val matchedQuestionLevel =
            vectorStore
                .similaritySearch(
                    SearchRequest
                        .defaults()
                        .withSimilarityThreshold(LEVEL_UP_THRESHOLD)
                        .withQuery(query)
                        .withFilterExpression("type == '${DocumentType.QUESTION}'"),
                )
                .minByOrNull { document -> document.metadata["distance"].toString().toFloat() }
                ?.metadata
                ?.get("level")
                ?.toString()
                ?.toInt()
                ?: 0

        if (matchedQuestionLevel == userLevel.level + 1) {
            userLevelRepository.save(userLevel.copy(level = matchedQuestionLevel, coins = userLevel.coins + LEVEL_UP_COINS))
        }
    }


    protected fun doGetConversationId(context: Map<String, Any>): String {
        return context["chat_memory_conversation_id"].toString()
    }
}

