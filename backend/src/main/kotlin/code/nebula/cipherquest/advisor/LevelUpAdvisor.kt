package code.nebula.cipherquest.advisor

import code.nebula.cipherquest.service.GameService
import org.springframework.ai.chat.client.AdvisedRequest
import org.springframework.ai.chat.client.RequestResponseAdvisor
import org.springframework.ai.chat.model.ChatResponse
import org.springframework.stereotype.Service

@Service
class LevelUpAdvisor(
    private val gameService: GameService,
) : RequestResponseAdvisor {
    override fun adviseRequest(
        request: AdvisedRequest,
        context: Map<String, Any>,
    ): AdvisedRequest {
        val id = doGetConversationId(context)

        gameService.levelUp(id, request.userText)

        return request
    }


    override fun adviseResponse(
        response: ChatResponse,
        context: Map<String, Any>,
    ): ChatResponse {
        val id = doGetConversationId(context)

        gameService.levelUp(id, response.result.output.content)

        return response
    }

    protected fun doGetConversationId(context: Map<String, Any>): String {
        return context["chat_memory_conversation_id"].toString()
    }
}

