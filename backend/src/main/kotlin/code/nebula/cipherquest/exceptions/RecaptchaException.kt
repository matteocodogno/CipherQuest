package code.nebula.cipherquest.exceptions

import code.nebula.cipherquest.models.ErrorType

class RecaptchaException(
    val status: Int,
    val type: ErrorType,
    override val message: String,
) : RuntimeException(message)
