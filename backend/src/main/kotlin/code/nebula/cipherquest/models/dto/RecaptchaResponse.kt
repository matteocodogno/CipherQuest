package code.nebula.cipherquest.models.dto

data class RecaptchaResponse(
    val success: Boolean,
    val challege_ts: String?,
    val hostname: String?,
    val score: Double?,
    val action: String?,
)
