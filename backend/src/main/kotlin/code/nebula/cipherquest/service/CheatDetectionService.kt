package code.nebula.cipherquest.service

import code.nebula.cipherquest.configuration.properties.GameConfig
import code.nebula.cipherquest.models.UserQuery
import org.springframework.stereotype.Service

@Service
class CheatDetectionService(
    private val vectorStoreService: VectorStoreService,
    private val gameConfig: GameConfig,
) {
    companion object {
        private const val MAX_LEVEL = 3
        private const val MIN_QUESTIONS = 6
    }

    /**
     * Checks if the user is attempting to cheat in the game based on their query.
     *
     * The method validates the user's query message against the game's win condition. It also ensures
     * the user has not surpassed the defined level threshold and has submitted fewer than the minimum
     * required messages.
     *
     * @param userQuery the query submitted by the user, containing the user's information and message.
     * @return true if the user is detected to be cheating, false otherwise.
     */
    fun checkIfCheating(userQuery: UserQuery): Boolean =
        Regex(gameConfig.winCondition)
            .containsMatchIn(userQuery.message) &&
            userQuery.user.level < MAX_LEVEL &&
            vectorStoreService.countUserMessages(userQuery.user.userId) < MIN_QUESTIONS
}
