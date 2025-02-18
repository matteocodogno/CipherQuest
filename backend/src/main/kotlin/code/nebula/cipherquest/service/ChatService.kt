package code.nebula.cipherquest.service

import code.nebula.cipherquest.models.DocumentType
import code.nebula.cipherquest.models.UserQuery
import org.springframework.ai.chat.client.ChatClient
import org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY
import org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_RETRIEVE_SIZE_KEY
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor
import org.springframework.stereotype.Service

@Service
class ChatService(
    private val chatClient: ChatClient,
    private val documentTools: DocumentTools,
) {
    companion object {
        private const val CHAT_MEMORY_MAX_SIZE = 20
    }

    fun chat(userQuery: UserQuery) =
        chatClient
            .prompt()
            .tools(documentTools)
            .toolContext(
                mapOf(
                    "userId" to userQuery.user.userId,
                    "level" to userQuery.user.level,
                ),
            ).system { sp -> sp.param("userId", userQuery.user.userId).param("level", userQuery.user.level) }
            .user(userQuery.message)
            .advisors { a ->
                a
                    .param(CHAT_MEMORY_CONVERSATION_ID_KEY, userQuery.user.userId)
                    .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, CHAT_MEMORY_MAX_SIZE)
                    .param(
                        QuestionAnswerAdvisor.FILTER_EXPRESSION,
                        "type == '${DocumentType.DOCUMENT}' && level <= ${userQuery.user.level}",
                    )
            }.call()
            .content()
            .orEmpty()
}
