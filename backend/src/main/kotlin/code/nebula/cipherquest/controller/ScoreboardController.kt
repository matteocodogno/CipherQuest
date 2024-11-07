package code.nebula.cipherquest.controller

import code.nebula.cipherquest.controller.request.ScoreboardEntry
import code.nebula.cipherquest.service.UserLevelService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/score")
class ScoreboardController(
    private val userLevelService: UserLevelService,
) {
    @GetMapping("/")
    fun getScoreboard(): List<ScoreboardEntry> = userLevelService.calculateScoreboard()
}
