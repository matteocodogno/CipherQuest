package code.nebula.cipherquest.models.requests

import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotEmpty
import java.time.LocalDate

data class Prize(
    @field:NotBlank(message = "Prize name cannot be blank")
    val name: String,
    val position: Int,
)

data class PrizeRequest(
    val date: LocalDate = LocalDate.now(),
    @field:NotEmpty(message = "Prizes should contain at least one entry")
    @field:Valid
    val prizes: List<Prize>,
)
