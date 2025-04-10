package code.nebula.cipherquest.models.requests

import java.time.LocalDate

data class Prize(
    val name: String,
    val position: Int,
)

data class PrizeRequest(
    val date: LocalDate = LocalDate.now(),
    val prizes: List<Prize>,
)
