package code.nebula.cipherquest.controller

import code.nebula.cipherquest.models.dto.BotMessage
import code.nebula.cipherquest.models.dto.Message
import code.nebula.cipherquest.service.GameService
import code.nebula.cipherquest.service.VectorStoreService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/chat")
class ChatController(
    private val gameService: GameService,
    private val vectorStoreService: VectorStoreService,
) {
    @PostMapping("/{id}")
    fun chat(
        @PathVariable id: String,
        @RequestBody userMessage: String,
    ): BotMessage = gameService.play(id, userMessage)

    @GetMapping("/{id}")
    fun getChatHistory(
        @PathVariable id: String,
    ): List<Message> = vectorStoreService.getMessageHistoryByUserId(id)
}
