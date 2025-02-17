package code.nebula.cipherquest.service

import code.nebula.cipherquest.components.MessageContext
import code.nebula.cipherquest.configuration.properties.GameConfig
import code.nebula.cipherquest.models.UserQuery
import code.nebula.cipherquest.models.dto.BotMessage
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Service
import java.util.regex.Pattern

private val logger = KotlinLogging.logger {}

@Service
class GameService(
    private val userLevelService: UserLevelService,
    private val messageContext: MessageContext,
    private val vectorStoreService: VectorStoreService,
    private val chatService: ChatService,
    private val gameConfig: GameConfig,
    private val cheatDetectionService: CheatDetectionService,
) {
    /**
     * Determines if the user has won the game based on their query input and handles the victory process.
     *
     * If the user's query matches the defined win condition, this method triggers the win process for the user,
     * constructs a win message, logs the query and bot's response, updates additional information in the
     * storage system, and returns a `BotMessage` representing the win state. If the win condition is not met,
     * it returns `null`.
     *
     * @param userQuery the user query containing the user's information and their message input.
     * @return a [BotMessage] representing the win state if the win condition is met, or `null` otherwise.
     */
    private fun gameWin(userQuery: UserQuery): BotMessage? =
        if (Pattern
                .compile(gameConfig.winCondition)
                .toRegex()
                .containsMatchIn(userQuery.message)
        ) {
            userLevelService.hasWon(userQuery.user.userId)
            BotMessage.buildWinMessage(userQuery.user)
        } else {
            null
        }

    /**
     * Determines if the game is over for the user based on their current coin count and processes the game over state.
     *
     * If the user's coins are less than or equal to zero, creates a game over message,
     * logs the user's query and the corresponding bot message, updates additional information in storage,
     * and returns the `BotMessage`. Otherwise, returns `null`.
     *
     * @param userQuery the user's query object containing user-specific data and the message they sent.
     * @return a [BotMessage] representing the game over state if the game ends, or `null` if the game is not over.
     */
    private fun gameOver(userQuery: UserQuery): BotMessage? =
        if (userQuery.user.coins <= 0) {
            BotMessage.buildGameOverMessage(userQuery.user)
        } else {
            null
        }

    /**
     * Detects if a user is attempting to cheat in the game based on their query.
     * If cheating is detected, updates the user's status, logs the related messages,
     * and returns a cheat detection message.
     *
     * @param userQuery the user query containing the user's information and message.
     * @return a [BotMessage] indicating the detected cheating behavior, or `null` if no cheating is detected.
     */
    private fun detectCheat(userQuery: UserQuery): BotMessage? {
        val userLevel = userQuery.user
        val userId = userLevel.userId

        if (cheatDetectionService.checkIfCheating(userQuery)) {
            userLevelService.hasCheated(userId)
            BotMessage.buildCheatMessage(userLevel)
        }

        return null
    }

    /**
     * Processes the user's query for the next turn in the game and retrieves the corresponding response.
     *
     * @param userQuery the user's query containing their information and message.
     * @return a string response generated based on the user's query.
     */
    private fun gameNextTurn(userQuery: UserQuery): String = chatService.chat(userQuery)

    fun play(
        userId: String,
        userMessage: String,
    ): BotMessage {
        val userQuery =
            UserQuery(
                Pair(
                    userLevelService.getLevelByUser(userId),
                    userMessage.take(gameConfig.prompt.maxLength),
                ),
            )

        return listOf(::detectCheat, ::gameOver, ::gameWin)
            .firstNotNullOfOrNull { fn -> fn(userQuery) }
            ?: gameNextTurn(userQuery).let { response ->
                val user = userLevelService.decreaseCoins(userId)
                val messageId = vectorStoreService.getLastMessage(userId).id
                vectorStoreService.updateInfo(messageId, messageContext)
                return BotMessage.build(response, user, messageContext)
            }
    }
}
