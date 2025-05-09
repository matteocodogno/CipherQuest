package code.nebula.cipherquest.models.requests

data class ProtectedQuestionRequest(
    override val content: String,
) : QuestionRequest
