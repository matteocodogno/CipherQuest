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
        val newUserText =
            advisedRequest.userText
                .replace("{", "")
                .replace("}", "")

        val newRequest =
            AdvisedRequest(
                advisedRequest.chatModel,
                newUserText,
                advisedRequest.systemText,
                advisedRequest.chatOptions,
                advisedRequest.media,
                advisedRequest.functionNames,
                advisedRequest.functionCallbacks,
                advisedRequest.messages,
                advisedRequest.userParams,
                advisedRequest.systemParams,
                advisedRequest.advisors,
                advisedRequest.advisorParams,
                advisedRequest.adviseContext,
                advisedRequest.toolContext,
            )

        return chain.nextAroundCall(newRequest)
    }
}
