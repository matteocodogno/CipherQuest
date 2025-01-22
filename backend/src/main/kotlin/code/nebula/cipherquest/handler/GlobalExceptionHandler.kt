package code.nebula.cipherquest.handler

import code.nebula.cipherquest.exceptions.DocumentNotFoundException
import code.nebula.cipherquest.exceptions.UserAlreadyExistsException
import jakarta.persistence.EntityNotFoundException
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
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
    @ExceptionHandler(DocumentNotFoundException::class)
    fun handleDocumentNotFoundException(e: DocumentNotFoundException): ResponseEntity<String> =
        ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.message)

    @ExceptionHandler(MailException::class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    fun handleMailExceptionException(ex: MailException): String = ex.message ?: "Mail server error"

    @ExceptionHandler(UserAlreadyExistsException::class)
    @ResponseStatus(HttpStatus.CONFLICT)
    fun handleMethodArgumentNotValidException(ex: UserAlreadyExistsException): String =
        ex.message
            ?: "Username already exists"

    @ExceptionHandler(EntityNotFoundException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun handleMethodArgumentNotValidException(ex: EntityNotFoundException): String =
        ex.message
            ?: "Entity not found"

    @ExceptionHandler(MethodArgumentNotValidException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleMethodArgumentNotValidException(ex: MethodArgumentNotValidException): ResponseEntity<Map<String, List<String>>> {
        val errors: Map<String, List<String>> =
            ex.bindingResult
                .fieldErrors
                .groupBy({ it.field }, { it.defaultMessage ?: "Generic Error" })

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors)
    }
}
