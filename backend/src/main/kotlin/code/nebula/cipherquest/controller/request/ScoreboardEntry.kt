package code.nebula.cipherquest.controller.request

data class ScoreboardEntry(
    val index: Int,
    val username: String,
    val userId: String,
    val score: Int,
    val time: Int,
)
