package code.nebula.cipherquest.advisor

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.ai.chat.client.advisor.api.AdvisedRequest
import org.springframework.ai.chat.client.advisor.api.AdvisedResponse
import org.springframework.ai.chat.client.advisor.api.CallAroundAdvisor
import org.springframework.ai.chat.client.advisor.api.CallAroundAdvisorChain
import org.springframework.core.Ordered

private val logger = KotlinLogging.logger {}

class LoggingAdvisor : CallAroundAdvisor {
    override fun getOrder(): Int = Ordered.LOWEST_PRECEDENCE

    override fun getName(): String = javaClass.simpleName

    override fun aroundCall(
        advisedRequest: AdvisedRequest,
        chain: CallAroundAdvisorChain,
    ): AdvisedResponse {
        logger.info { "SystemText: ${advisedRequest.systemText}" }
        logger.info { "UserText: ${advisedRequest.userText}" }
        logger.info { "Context: ${advisedRequest.systemParams}" }

        val advisedResponse = chain.nextAroundCall(advisedRequest)

        logger.info { "Token: " + advisedResponse.response.metadata.usage.totalTokens }

        return advisedResponse
    }
}
