package code.nebula.cipherquest.controller

import org.springframework.ai.chat.client.ChatClient
import org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY
import org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_RETRIEVE_SIZE_KEY
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/chat")
class ChatController(
    private val chatClient: ChatClient,
) {
    @PostMapping("/{id}")
    fun chat(
        @PathVariable id: String,
        @RequestBody userMessage: String,
    ): String =
        chatClient
            .prompt()
            .user(userMessage)
            .advisors { a ->
                a
                    .param(CHAT_MEMORY_CONVERSATION_ID_KEY, id)
                    .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 100)
            }
            .call()
            .content()
}
