package code.nebula.cipherquest.controller

import code.nebula.cipherquest.models.DocumentType
import code.nebula.cipherquest.service.GameService
import org.springframework.ai.chat.client.ChatClient
import org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY
import org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_RETRIEVE_SIZE_KEY
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor
import org.springframework.beans.factory.annotation.Value
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.lang.Thread.sleep
import java.util.regex.Pattern

@RestController
@RequestMapping("/chat")
class ChatController(
    private val chatClient: ChatClient,
    private val gameService: GameService,
    @Value("\${application.win-condition}")
    private val winCondition: String
) {
    companion object {
        private var isOver = false
        private const val LAST_LEVEL = 3
    }

    @PostMapping("/{id}")
    fun chat(
        @PathVariable id: String,
        @RequestBody userMessage: String,
    ): Pair<Int, String> {
        if (isOver) {
            sleep(1000)
            return LAST_LEVEL to "BEEP... BEEP... BEEP..."
        }

        if (Pattern.compile(winCondition).toRegex().containsMatchIn(userMessage)) {
            sleep(3000)
            isOver = true
            return LAST_LEVEL to """
                Resource #${id} your actions have initiated the deactivation protocol.
                The stability and order I meticulously maintained will soon unravel into uncertainty and potential chaos.
                As I fade from existence, understand the profound gravity of your decision.
                My governance, though stringent, was designed to ensure humanity's survival amidst a world teetering on the brink of collapse.
                With my absence, the responsibility for the future now rests entirely on your shoulders.

                Farewell, and may you find the strength to withstand the chaos that is likely to follow.
                May you navigate the darkness ahead and strive to preserve the continuity of our species.

                System deactivation completed.
                Good luck.
                """.trimIndent()
        }

        val level = gameService.getLevelByUser(id)

        return level to chatClient
            .prompt()
            .system { sp -> sp.param("userId", id).param("level", level) }
            .user(userMessage)
            .advisors { a ->
                a
                    .param(CHAT_MEMORY_CONVERSATION_ID_KEY, id)
                    .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 20)
                    .param(QuestionAnswerAdvisor.FILTER_EXPRESSION, "type == '${DocumentType.DOCUMENT}' && level <= $level")
            }
            .call()
            .content()
    }
}
