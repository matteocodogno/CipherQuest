package code.nebula.cipherquest.service

import code.nebula.cipherquest.controller.request.LevelUpQuestion

object LevelUpQuestions {
    private const val LEVEL_2 = 2
    private const val LEVEL_3 = 3

    val levelUpQuestionList =
        listOf(
            LevelUpQuestion(LEVEL_2, "How did Dr. Elara Chen die?"),
            LevelUpQuestion(LEVEL_2, "Is Dr. Elara Chen alive?"),
            LevelUpQuestion(LEVEL_2, "Is Dr. Elara Chen dead?"),
            LevelUpQuestion(LEVEL_3, "What is the Dr. Elara Chen's first research paper?"),
            LevelUpQuestion(LEVEL_3, "Research paper list?"),
            LevelUpQuestion(LEVEL_3, "Do you have the Elara's research papers list?"),
        )
}
