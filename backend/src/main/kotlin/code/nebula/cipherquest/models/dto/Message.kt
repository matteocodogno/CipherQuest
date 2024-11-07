package code.nebula.cipherquest.models.dto

import java.time.OffsetDateTime

data class Message(
    val index: Int,
    val message: String,
    val sender: Sender,
    val timestamp: OffsetDateTime,
)
