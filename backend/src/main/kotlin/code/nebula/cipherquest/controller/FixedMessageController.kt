package code.nebula.cipherquest.controller

import org.springframework.web.bind.annotation.*
import javax.validation.Valid
import code.nebula.cipherquest.models.requests.FixedBotMessageRequest
import code.nebula.cipherquest.repository.entities.FixedBotMessage
import code.nebula.cipherquest.service.FixedBotMessageService

@RestController
@RequestMapping("/fixedMessage")
class FixedMessageController(private val fixedBotMessageService: FixedBotMessageService) {

    @PostMapping("/{storyName}")
    fun addFixedBotMessages(
        @PathVariable storyName: String,
        @Valid @RequestBody messages: FixedBotMessageRequest,
    ): List<FixedBotMessage> = fixedBotMessageService.addFixedBotMessages(messages, storyName)
}
