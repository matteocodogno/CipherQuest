package code.nebula.cipherquest.service

import code.nebula.cipherquest.controller.request.LevelUpQuestion
import org.springframework.stereotype.Service

@Service
class LevelUpQuestionService {

    val levelUpQuestionList =
        listOf(
            LevelUpQuestion(2, "How did Dr. Elara Chen die?"),
            LevelUpQuestion(2, "Is Dr. Elara Chen alive?"),
            LevelUpQuestion(2, "Is Dr. Elara Chen dead?"),
            LevelUpQuestion(3, "What is the Dr. Elara Chen's first research paper?"),
        )

    fun getLevelUpQuestionList() = levelUpQuestionList
}
