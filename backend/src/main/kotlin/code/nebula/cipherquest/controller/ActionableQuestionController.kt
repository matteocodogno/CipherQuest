package code.nebula.cipherquest.controller

import code.nebula.cipherquest.models.requests.LevelUpQuestionRequest
import code.nebula.cipherquest.models.requests.ProtectedQuestionRequest
import code.nebula.cipherquest.service.ActionableQuestionService
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/question")
class ActionableQuestionController(
    private val actionableQuestionService: ActionableQuestionService,
) {
    @PostMapping("/levelUp/{storyName}")
    fun addLevelUpQuestions(
        @PathVariable storyName: String,
        @RequestBody questions: List<LevelUpQuestionRequest>,
    ) = actionableQuestionService.addLevelUpQuestion(questions, storyName)

    @PostMapping("/protected/{storyName}")
    fun addProtectedQuestions(
        @PathVariable storyName: String,
        @RequestBody questions: List<ProtectedQuestionRequest>,
    ) = actionableQuestionService.addProtectedQuestion(questions, storyName)
}
