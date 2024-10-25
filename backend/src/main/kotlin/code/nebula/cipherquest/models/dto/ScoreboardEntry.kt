package code.nebula.cipherquest.models.dto

data class ScoreboardEntry(
    val username: String,
    val userId: String,
    val score: Int,
    val time: Int
)
