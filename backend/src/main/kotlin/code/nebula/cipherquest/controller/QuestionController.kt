package code.nebula.cipherquest.controller

import code.nebula.cipherquest.repository.LevelUpQuestionRepository
import code.nebula.cipherquest.repository.ProtectedQuestionRepository
import code.nebula.cipherquest.repository.entities.LevelUpQuestion
import code.nebula.cipherquest.repository.entities.ProtectedQuestion
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/question")
class QuestionController(
    private val levelUpQuestionRepository: LevelUpQuestionRepository,
    private val protectedQuestionRepository: ProtectedQuestionRepository,
) {
    @PostMapping("/levelUp")
    fun addLevelUpQuestion(
        @RequestBody question: LevelUpQuestion,
    ) = levelUpQuestionRepository.save(question)

    @PostMapping("/bulk/levelUp")
    fun addLevelUpQuestions(
        @RequestBody questions: List<LevelUpQuestion>,
    ): List<LevelUpQuestion> = levelUpQuestionRepository.saveAll(questions)

    @PostMapping("/protected")
    fun addProtectedQuestion(
        @RequestBody question: ProtectedQuestion,
    ) = protectedQuestionRepository.save(question)

    @PostMapping("/bulk/protected")
    fun addProtectedQuestions(
        @RequestBody questions: List<ProtectedQuestion>,
    ): List<ProtectedQuestion> = protectedQuestionRepository.saveAll(questions)
}
