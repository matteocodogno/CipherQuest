package code.nebula.cipherquest.controller

import code.nebula.cipherquest.controller.request.Score
import code.nebula.cipherquest.controller.request.ScoreboardEntry
import code.nebula.cipherquest.service.UserLevelService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/score")
class ScoreboardController(
    private val userLevelService: UserLevelService,
) {
    @GetMapping
    fun getScoreboard(): List<ScoreboardEntry> = userLevelService.calculateScoreboard()

    @GetMapping("/{id}")
    fun getScore(
        @PathVariable id: String,
    ): Score = Score(userLevelService.getLevelByUser(id).score.toInt())
}
