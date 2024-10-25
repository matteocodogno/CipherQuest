package code.nebula.cipherquest.controller

import code.nebula.cipherquest.models.dto.ScoreboardEntry
import code.nebula.cipherquest.service.GameService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/score")
class ScoreboardController(
    private val gameService: GameService,
) {
    @GetMapping("/")
    fun getScoreboard(): List<ScoreboardEntry> = gameService.calculateScoreboard()
}
