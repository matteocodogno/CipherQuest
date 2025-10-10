package code.nebula.cipherquest.service

import code.nebula.cipherquest.models.UserStatus
import code.nebula.cipherquest.models.dto.BotMessage
import code.nebula.cipherquest.models.requests.FixedBotMessageRequest
import code.nebula.cipherquest.repository.FixedBotMessageRepository
import code.nebula.cipherquest.repository.entities.FixedBotMessage
import code.nebula.cipherquest.repository.entities.FixedBotMessageType
import code.nebula.cipherquest.repository.entities.UserLevel
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class FixedBotMessageService(
    private val fixedBotMessageRepository: FixedBotMessageRepository,
) {
    /**
     * Retrieves a message template based on the provided message type and story name.
     * The returned function formats the retrieved template with the specified userId.
     *
     * @param type the type of the fixed bot message to retrieve
     * @return a lambda function that takes a userId and storyName as inputs
     *         and returns the formatted message as a String
     */
    private fun getMessage(type: FixedBotMessageType): (String, String) -> String =
        { userId, storyName ->
            String.format(
                fixedBotMessageRepository.findByTypeAndStoryName(type, storyName)?.message ?: "",
                userId,
            )
        }

    val getProtectedMessage = getMessage(FixedBotMessageType.PROTECTED)
    val getDocumentMessage = getMessage(FixedBotMessageType.DOCUMENT)
    val getWinMessage = getMessage(FixedBotMessageType.WIN)
    val getGameOverMessage = getMessage(FixedBotMessageType.GAME_OVER)
    val getCheatMessage = getMessage(FixedBotMessageType.CHEAT_DETECT)

    fun buildWinMessage(
        userLevel: UserLevel,
        storyName: String,
    ): BotMessage = build(getWinMessage(userLevel.userId, storyName), userLevel, UserStatus.WIN)

    fun buildGameOverMessage(
        userLevel: UserLevel,
        storyName: String,
    ): BotMessage = build(getGameOverMessage(userLevel.userId, storyName), userLevel, UserStatus.GAME_OVER)

    fun buildCheatMessage(
        userLevel: UserLevel,
        storyName: String,
    ): BotMessage = build(getCheatMessage(userLevel.userId, storyName), userLevel, UserStatus.CHEATED)

    private fun build(
        message: String,
        userLevel: UserLevel,
        userStatus: UserStatus,
    ): BotMessage =
        BotMessage.build(
            message,
            userLevel,
            mutableMapOf("status" to userStatus, "isLevelUp" to false, "sources" to emptyList<String>()),
        )

    @Transactional
    fun addFixedBotMessages(
        fixedBotMessageRequest: FixedBotMessageRequest,
        storyName: String,
    ): List<FixedBotMessage> {
        require(fixedBotMessageRequest.messages.isNotEmpty()) {
            "FixedBotMessage list cannot be empty"
        }

        val takenTypes =
            fixedBotMessageRepository
                .findByStoryName(storyName)
                .map { it.type }

        return fixedBotMessageRequest.messages
            .filterNot {
                takenTypes.contains(it.type)
            }.map {
                require(it.content.isNotBlank()) { "FixedBotMessage list should contain at least one entry" }
                FixedBotMessage(
                    type = it.type,
                    message = it.content,
                    storyName = storyName,
                )
            }.let { fixedBotMessageRepository.saveAll(it) }
    }
}
