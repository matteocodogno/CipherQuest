package code.nebula.cipherquest.advisor

import code.nebula.cipherquest.repository.UserLevelRepository
import org.springframework.ai.chat.client.AdvisedRequest
import org.springframework.ai.chat.client.RequestResponseAdvisor
import org.springframework.ai.chat.model.ChatResponse
import org.springframework.ai.vectorstore.SearchRequest
import org.springframework.ai.vectorstore.VectorStore
import org.springframework.stereotype.Service

@Service
class LevelUpAdvisor(
    val userLevelRepository: UserLevelRepository,
    val vectorStore: VectorStore,
) : RequestResponseAdvisor {
    override fun adviseRequest(
        request: AdvisedRequest,
        context: Map<String, Any>,
    ): AdvisedRequest {
        val id = doGetConversationId(context)

        val level = userLevelRepository.getLevelByUser(id)

        val matchedQuestionLevel =
            vectorStore
                .similaritySearch(
                    SearchRequest
                        .defaults()
                        .withQuery(request.userText)
                        .withFilterExpression("type == 'question'"),
                )
                .minByOrNull { it.metadata["distance"].toString().toFloat() }
                ?.metadata
                ?.get("level")
                ?.toString()
                ?.toInt()
                ?: 0

        if (matchedQuestionLevel == level + 1) {
            userLevelRepository.update(id, matchedQuestionLevel)
        }

        return request
    }

    override fun adviseResponse(
        response: ChatResponse,
        context: Map<String, Any>,
    ): ChatResponse {
        val id = doGetConversationId(context)

        val level = userLevelRepository.getLevelByUser(id)

        val matchedQuestionLevel =
            vectorStore
                .similaritySearch(
                    SearchRequest
                        .defaults()
                        .withQuery(response.result.output.content)
                        .withFilterExpression("type == 'question'"),
                )
                .minByOrNull { it.metadata["distance"].toString().toFloat() }
                ?.metadata
                ?.get("level")
                ?.toString()
                ?.toInt()
                ?: 0

        if (matchedQuestionLevel == level + 1) {
            userLevelRepository.update(id, matchedQuestionLevel)
        }

        return response
    }

    protected fun doGetConversationId(context: Map<String, Any>): String {
        return context["chat_memory_conversation_id"].toString()
    }
}

