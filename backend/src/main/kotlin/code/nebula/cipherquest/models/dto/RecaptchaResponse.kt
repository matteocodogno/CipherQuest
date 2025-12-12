package code.nebula.cipherquest.models.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class RecaptchaResponse(
    val success: Boolean = false,
    val score: Double? = null,
    val action: String? = null,
    val hostname: String? = null,
    @field:JsonProperty("challenge_ts")
    val challengeTs: String? = null,
    @field:JsonProperty("error-codes")
    val errorCodes: List<String>? = null,
)
