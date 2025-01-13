package code.nebula.cipherquest.models.requests

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotEmpty

data class CreateUserLevelRequest(
    @field:Email(message = "Email is not valid", regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")
    @field:NotEmpty(message = "Email cannot be empty")
    var email: String,
)
