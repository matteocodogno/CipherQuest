package code.nebula.cipherquest.exceptions

class EmailSendingException(
    message: String,
    cause: Throwable? = null,
) : RuntimeException(message, cause)
