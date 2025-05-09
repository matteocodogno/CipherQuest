package code.nebula.cipherquest.exceptions

class EmailTemplateNotFoundException(
    message: String,
    cause: Throwable? = null,
) : RuntimeException(message, cause)
