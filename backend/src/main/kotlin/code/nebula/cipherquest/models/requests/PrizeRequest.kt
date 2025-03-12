package code.nebula.cipherquest.models.requests

import java.time.LocalDate

data class PrizeRequest(
    val name: String,
    val position: Int,
    val date: LocalDate = LocalDate.now(),
)
