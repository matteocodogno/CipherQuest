package code.nebula.cipherquest.service.aop

import code.nebula.cipherquest.models.UserQuery
import code.nebula.cipherquest.models.dto.BotMessage
import code.nebula.cipherquest.service.VectorStoreService
import org.aspectj.lang.annotation.AfterReturning
import org.aspectj.lang.annotation.Aspect
import org.springframework.ai.chat.messages.MessageType
import org.springframework.stereotype.Component

@Aspect
@Component
class VectorStoreAspect(
    private val vectorStoreService: VectorStoreService,
) {
    @AfterReturning(
        "execution(* code.nebula.cipherquest.service.GameService.play(..))",
    )
    fun storeMessage(
        userId: String,
        userQuery: UserQuery,
        botMessage: BotMessage,
    ) {
        vectorStoreService.saveMessage(userId, userQuery.message, MessageType.USER)
        vectorStoreService.saveMessage(userId, botMessage.message, MessageType.ASSISTANT)
        vectorStoreService.updateInfoOnLastMessage(userQuery.user.userId, botMessage.info)
    }
}
