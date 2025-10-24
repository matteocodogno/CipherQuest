package code.nebula.cipherquest.models.dto

enum class RecaptchaVersion { V2, V3 }

data class RecaptchaResponse(
    val success: Boolean = false,
    val score: Double? = null,
    val action: String? = null,
    val hostname: String? = null,
    @com.fasterxml.jackson.annotation.JsonProperty("challenge_ts")
    val challengeTs: String? = null,
    @com.fasterxml.jackson.annotation.JsonProperty("error-codes")
    val errorCodes: List<String>? = null,
)
