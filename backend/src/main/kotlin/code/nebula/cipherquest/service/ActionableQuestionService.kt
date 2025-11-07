package code.nebula.cipherquest.service

import code.nebula.cipherquest.models.requests.LevelUpQuestionRequest
import code.nebula.cipherquest.models.requests.ProtectedQuestionRequest
import code.nebula.cipherquest.repository.LevelUpQuestionRepository
import code.nebula.cipherquest.repository.ProtectedQuestionRepository
import org.springframework.stereotype.Service

@Service
class ActionableQuestionService(
    private val levelUpQuestionRepository: LevelUpQuestionRepository,
    private val protectedQuestionRepository: ProtectedQuestionRepository,
) {
    fun addLevelUpQuestion(
        questions: List<LevelUpQuestionRequest>,
        storyName: String,
    ) = levelUpQuestionRepository.save(questions, storyName)

    fun addProtectedQuestion(
        questions: List<ProtectedQuestionRequest>,
        storyName: String,
    ) = protectedQuestionRepository.save(questions, storyName)
}
