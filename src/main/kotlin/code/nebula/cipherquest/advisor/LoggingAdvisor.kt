package code.nebula.cipherquest.advisor

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.ai.chat.client.AdvisedRequest
import org.springframework.ai.chat.client.RequestResponseAdvisor
import org.springframework.ai.chat.model.ChatResponse

class LoggingAdvisor : RequestResponseAdvisor {
    override fun adviseRequest(
        request: AdvisedRequest,
        context: Map<String, Any>,
    ): AdvisedRequest {
        logger.info("Request: $request")
        return request
    }

    override fun adviseResponse(
        response: ChatResponse,
        context: Map<String, Any>,
    ): ChatResponse {
        logger.info("Token: " + response.metadata.usage.totalTokens)

        return response
    }

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(LoggingAdvisor::class.java)
    }
}
