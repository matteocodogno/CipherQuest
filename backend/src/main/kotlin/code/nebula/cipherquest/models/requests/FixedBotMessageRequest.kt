package code.nebula.cipherquest.models.requests

import code.nebula.cipherquest.repository.entities.FixedBotMessageType
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotEmpty

data class FixedBotMessage(
    val type: FixedBotMessageType,
    @field:NotBlank(message = "Content cannot be blank")
    val content: String = "",
)

data class FixedBotMessageRequest(
    @field:NotEmpty(message = "FixedBotMessage list should contain at least one entry")
    @field:Valid
    val messages: List<FixedBotMessage> = emptyList(),
)
