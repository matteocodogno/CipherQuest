package code.nebula.cipherquest.service

import code.nebula.cipherquest.models.DocumentType
import code.nebula.cipherquest.models.UserQuery
import org.springframework.ai.chat.client.ChatClient
import org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY
import org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_RETRIEVE_SIZE_KEY
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor
import org.springframework.ai.model.function.FunctionCallback
import org.springframework.ai.model.function.FunctionCallbackContext
import org.springframework.ai.model.function.FunctionCallingOptionsBuilder
import org.springframework.stereotype.Service

@Service
class ChatService(
    private val chatClient: ChatClient,
    private val functionChatClient: ChatClient,
    private val functionCallbackContext: FunctionCallbackContext,
) {
    companion object {
        private const val CHAT_MEMORY_MAX_SIZE = 20
    }

    fun chat(userQuery: UserQuery): String {
        val toolCallResult = toolResponse(userQuery)

//        TODO: do we need to manually save messages into DB? Can we use memory advisor?
//        if (toolCallResult != null) {
//            vectorStoreService.saveMessage(userQuery.user.userId, userQuery.message, MessageType.USER)
//            vectorStoreService.saveMessage(userQuery.user.userId, toolCallResult, MessageType.ASSISTANT)
//        }

        return toolCallResult ?: chatResponse(userQuery) ?: ""
    }

    private fun toolResponse(userToQuery: UserQuery): String? =
        functionChatClient
            .prompt()
            .options(FunctionCallingOptionsBuilder().withProxyToolCalls(true).build())
            .functions("getDiaryPages")
            .system { sp -> sp.param("userId", userToQuery.user.userId).param("level", userToQuery.user.level) }
            .user(userToQuery.message)
            .advisors { a ->
                a
                    .param(CHAT_MEMORY_CONVERSATION_ID_KEY, userToQuery.user.userId)
                    .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, CHAT_MEMORY_MAX_SIZE)
            }
//            TODO: why did not we use the memory advisor?
            .call()
            .chatResponse()
            ?.result
            ?.output
            ?.toolCalls
            ?.firstNotNullOfOrNull { toolCall ->
                val callback: FunctionCallback = functionCallbackContext.getFunctionCallback(toolCall.name, null)

                try {
                    callback.call(toolCall.arguments())?.removeSurrounding("\"", "\"")
                } catch (e: NullPointerException) {
                    return null
                }
            }

    private fun chatResponse(userToQuery: UserQuery): String? =
        chatClient
            .prompt()
            .system { sp -> sp.param("userId", userToQuery.user.userId).param("level", userToQuery.user.level) }
            .user(userToQuery.message)
            .advisors { a ->
                a
                    .param(CHAT_MEMORY_CONVERSATION_ID_KEY, userToQuery.user.userId)
                    .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, CHAT_MEMORY_MAX_SIZE)
                    .param(
                        QuestionAnswerAdvisor.FILTER_EXPRESSION,
                        "type == '${DocumentType.DOCUMENT}' && level <= ${userToQuery.user.level}",
                    )
            }.call()
            .content()
}
