package code.nebula.cipherquest.models.requests

import code.nebula.cipherquest.repository.entities.FixedBotMessageType
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull

data class FixedBotMessage(
    @field:NotNull(message = "type cannot be blank")
    val type: FixedBotMessageType,
    @field:NotBlank(message = "content cannot be blank")
    val content: String,
)

data class FixedBotMessageRequest(
    @field:NotEmpty(message = "FixedMessage list should contain at least one entry")
    @field:Valid
    val messages: List<FixedBotMessage>,
)
