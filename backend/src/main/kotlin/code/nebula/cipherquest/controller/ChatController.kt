package code.nebula.cipherquest.controller

import code.nebula.cipherquest.models.dto.BotAnswer
import code.nebula.cipherquest.service.GameService
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/chat")
class ChatController(
    private val gameService: GameService,
) {
    @PostMapping("/{id}")
    fun chat(
        @PathVariable id: String,
        @RequestBody userMessage: String,
    ): BotAnswer = gameService.play(id, userMessage)
}
