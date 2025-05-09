package code.nebula.cipherquest.service

import code.nebula.cipherquest.models.UserStatus
import code.nebula.cipherquest.models.dto.BotMessage
import code.nebula.cipherquest.repository.FixedBotMessageRepository
import code.nebula.cipherquest.repository.entities.FixedBotMessageType
import code.nebula.cipherquest.repository.entities.UserLevel
import org.springframework.stereotype.Service

@Service
class FixedBotMessageService(
    private val fixedBotMessageRepository: FixedBotMessageRepository,
) {
    private fun getMessage(
        type: FixedBotMessageType,
        userId: String,
        storyName: String,
    ) = String.format(
        fixedBotMessageRepository.findByTypeAndStoryName(type, storyName)?.message ?: "",
        userId,
    )

    fun getProtectedMessage(
        userId: String,
        storyName: String,
    ): String = getMessage(FixedBotMessageType.PROTECTED, userId, storyName)

    fun getDocumentMessage(
        userId: String,
        storyName: String,
    ): String = getMessage(FixedBotMessageType.DOCUMENT, userId, storyName)

    private fun build(
        type: FixedBotMessageType,
        userLevel: UserLevel,
        storyName: String,
        userStatus: UserStatus,
    ): BotMessage =
        BotMessage.build(
            getMessage(type, userLevel.userId, storyName),
            userLevel,
            mutableMapOf("status" to userStatus, "isLevelUp" to false, "sources" to emptyList<String>()),
        )

    fun buildWinMessage(
        userLevel: UserLevel,
        storyName: String,
    ): BotMessage = build(FixedBotMessageType.WIN, userLevel, storyName, UserStatus.WIN)

    fun buildGameOverMessage(
        userLevel: UserLevel,
        storyName: String,
    ): BotMessage = build(FixedBotMessageType.GAME_OVER, userLevel, storyName, UserStatus.GAME_OVER)

    fun buildCheatMessage(
        userLevel: UserLevel,
        storyName: String,
    ): BotMessage = build(FixedBotMessageType.CHEAT_DETECT, userLevel, storyName, UserStatus.CHEATED)
}
