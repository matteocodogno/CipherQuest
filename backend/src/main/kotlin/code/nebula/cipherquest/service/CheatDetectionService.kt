package code.nebula.cipherquest.service

import code.nebula.cipherquest.configuration.properties.GameConfig
import code.nebula.cipherquest.models.CheatDetectionResponse
import code.nebula.cipherquest.models.UserQuery
import code.nebula.cipherquest.models.requests.CheatDetectionRequest
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient

@Service
class CheatDetectionService(
    private val vectorStoreService: VectorStoreService,
    private val gameConfig: GameConfig,
    private val cheatDetectionWebClient: WebClient,
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

    fun scoreSession(
        sessionLengthSeconds: Long,
        coins: Int,
    ): Double {
        val request =
            CheatDetectionRequest(
                session_length = sessionLengthSeconds.toDouble(),
                coins = coins.toDouble(),
            )

        val response =
            cheatDetectionWebClient
                .post()
                .uri("/predict")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(CheatDetectionResponse::class.java)
                .block()

        return response?.cheat_probability ?: 0.0
    }
}
