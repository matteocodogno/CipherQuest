package code.nebula.cipherquest.controller

import code.nebula.cipherquest.models.requests.FixedBotMessageRequest
import code.nebula.cipherquest.repository.entities.FixedBotMessage
import code.nebula.cipherquest.service.FixedBotMessageService
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
@RequestMapping("/fixedMessage")
class FixedMessageController(
    private val fixedBotMessageService: FixedBotMessageService,
) {
    @PostMapping("/{storyName}")
    fun addFixedBotMessages(
        @PathVariable storyName: String,
        @Valid @RequestBody messages: FixedBotMessageRequest,
    ): List<FixedBotMessage> = fixedBotMessageService.addFixedBotMessages(messages, storyName)
}
