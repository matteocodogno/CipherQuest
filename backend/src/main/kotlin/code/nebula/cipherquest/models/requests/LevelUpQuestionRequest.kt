package code.nebula.cipherquest.models.requests

data class LevelUpQuestionRequest(
    override val content: String,
    val level: Int,
) : QuestionRequest
