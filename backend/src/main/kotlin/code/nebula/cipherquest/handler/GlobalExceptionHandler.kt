package code.nebula.cipherquest.handler

import code.nebula.cipherquest.exceptions.DocumentNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(DocumentNotFoundException::class)
    fun handleDocumentNotFoundException(e: DocumentNotFoundException): ResponseEntity<String> =
        ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.message)
}
