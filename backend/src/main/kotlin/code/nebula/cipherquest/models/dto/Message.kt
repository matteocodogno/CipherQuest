package code.nebula.cipherquest.models.dto

import org.springframework.ai.chat.prompt.PromptTemplate
import java.time.OffsetDateTime
import java.util.*

data class Message(
    val id: String,
    val index: Int,
    val message: String,
    val sender: Sender,
    val timestamp: OffsetDateTime,
    val info: Info,
) {
    companion object {
        fun getInitialMessage(
            content: String,
            userId: String,
        ): Message =
            Message(
                id = UUID.randomUUID().toString(),
                index = 0,
                message =
                    PromptTemplate(content)
                        .create(mapOf("userId" to userId))
                        .contents,
                sender = Sender.ASSISTANT,
                timestamp = OffsetDateTime.now(),
                info = Info()
            )
    }
}
