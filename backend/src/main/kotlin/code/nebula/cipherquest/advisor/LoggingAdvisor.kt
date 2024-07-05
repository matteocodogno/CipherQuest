package code.nebula.cipherquest.advisor

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.ai.chat.client.AdvisedRequest
import org.springframework.ai.chat.client.RequestResponseAdvisor
import org.springframework.ai.chat.model.ChatResponse

private val logger = KotlinLogging.logger {}

class LoggingAdvisor : RequestResponseAdvisor {
    override fun adviseRequest(
        request: AdvisedRequest,
        context: Map<String, Any>,
    ): AdvisedRequest {
        logger.info { "SystemText: ${request.systemText}" }
        logger.info { "UserText: ${request.userText}" }
        logger.info { "Context: ${request.systemParams}" }

        return request
    }

    override fun adviseResponse(
        response: ChatResponse,
        context: Map<String, Any>,
    ): ChatResponse {
        logger.info {
            "Token: " + response.metadata.usage.totalTokens
        }
        return response
    }
}
