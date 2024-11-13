package code.nebula.cipherquest.models.dto

import org.springframework.ai.chat.prompt.PromptTemplate
import java.time.OffsetDateTime

data class Message(
    val index: Int,
    val message: String,
    val sender: Sender,
    val timestamp: OffsetDateTime,
) {
    companion object {
        fun getInitialMessage(
            content: String,
            userId: String,
        ): Message =
            Message(
                index = 0,
                message =
                    PromptTemplate(content)
                        .create(mapOf("userId" to userId))
                        .contents,
                sender = Sender.ASSISTANT,
                timestamp = OffsetDateTime.now(),
            )
    }
}
