package code.nebula.cipherquest.handler

import code.nebula.cipherquest.exceptions.DocumentNotFoundException
import code.nebula.cipherquest.exceptions.StoryNotFoundException
import code.nebula.cipherquest.exceptions.UserAlreadyExistsException
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.mail.MailException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
@Suppress("MaxLineLength")
class GlobalExceptionHandler {
    @ExceptionHandler(DocumentNotFoundException::class, StoryNotFoundException::class)
    fun handleEntityNotFoundException(e: Exception): ResponseEntity<String> =
        ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(e.message)

    @ExceptionHandler(MailException::class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    fun handleMailExceptionException(ex: MailException): String = ex.message ?: "Mail server error"

    @ExceptionHandler(UserAlreadyExistsException::class)
    @ResponseStatus(HttpStatus.CONFLICT)
    fun handleMethodArgumentNotValidException(ex: UserAlreadyExistsException): String =
        ex.message
            ?: "Username already exists"

    @ExceptionHandler(MethodArgumentNotValidException::class, DataIntegrityViolationException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleMethodArgumentNotValidException(ex: Exception): ResponseEntity<Map<String, List<String>>> {
        val errors: Map<String, List<String>> =
            when (ex) {
                is MethodArgumentNotValidException ->
                    ex.bindingResult
                        .fieldErrors
                        .groupBy({ it.field }, { it.defaultMessage ?: "Generic Error" })

                is DataIntegrityViolationException -> mapOf("error" to listOf(ex.message ?: "Generic Error"))
                else -> mapOf("error" to listOf("Unknown error occurred"))
            }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors)
    }
}
