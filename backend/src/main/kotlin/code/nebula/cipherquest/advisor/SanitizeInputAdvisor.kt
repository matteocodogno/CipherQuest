package code.nebula.cipherquest.advisor

import org.springframework.ai.chat.client.advisor.api.AdvisedRequest
import org.springframework.ai.chat.client.advisor.api.AdvisedResponse
import org.springframework.ai.chat.client.advisor.api.CallAroundAdvisor
import org.springframework.ai.chat.client.advisor.api.CallAroundAdvisorChain
import org.springframework.core.Ordered
import org.springframework.stereotype.Service

@Service
class SanitizeInputAdvisor : CallAroundAdvisor {
    override fun getOrder() = Ordered.HIGHEST_PRECEDENCE

    override fun getName(): String = javaClass.simpleName

    override fun aroundCall(
        advisedRequest: AdvisedRequest,
        chain: CallAroundAdvisorChain,
    ): AdvisedResponse {
        val updatedRequest =
            advisedRequest.updateContext { context ->
                context["userText"] =
                    advisedRequest.userText
                        .replace("{", "")
                        .replace("}", "")
                context
            }

        return chain.nextAroundCall(updatedRequest)
    }
}
