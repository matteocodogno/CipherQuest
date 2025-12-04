package code.nebula.cipherquest.models.requests

import com.fasterxml.jackson.annotation.JsonProperty

data class CheatDetectionRequest(
    @field:JsonProperty("session_length")
    val sessionLength: Double,
    val coins: Double,
)
