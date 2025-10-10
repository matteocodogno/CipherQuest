package code.nebula.cipherquest.models.dto

import code.nebula.cipherquest.models.requests.LevelUpQuestionRequest
import code.nebula.cipherquest.models.requests.Prize
import code.nebula.cipherquest.models.requests.ProtectedQuestionRequest

data class GameDataFile(
    val levelUpQuestions: List<LevelUpQuestionRequest> = emptyList(),
    val protectedQuestions: List<ProtectedQuestionRequest> = emptyList(),
    // fixedBotMessages
    val prizes: List<Prize> = emptyList(),
)
