package code.nebula.cipherquest.service

import code.nebula.cipherquest.models.DocumentType
import code.nebula.cipherquest.models.dto.BotAnswer
import code.nebula.cipherquest.models.dto.BotAnswer.Companion.DEFAULT_LEVEL
import code.nebula.cipherquest.repository.UserLevelRepository
import code.nebula.cipherquest.repository.entities.UserLevel
import org.springframework.ai.chat.client.ChatClient
import org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY
import org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_RETRIEVE_SIZE_KEY
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.util.Optional
import java.util.regex.Pattern
import kotlin.jvm.optionals.getOrDefault

@Service
class GameService(
    private val userLevelRepository: UserLevelRepository,
    private val chatClient: ChatClient,
    @Value("\${application.win-condition}")
    private val winCondition: String
) {
    fun getLevelByUser(userId: String): UserLevel =
        userLevelRepository
            .findById(userId)
            .or {
                Optional.of(userLevelRepository.save(UserLevel(userId, DEFAULT_LEVEL)))
            }
            .getOrDefault(UserLevel(userId, DEFAULT_LEVEL))

    /**
     * Check if the user has already won the game, and return the final message if so.
     */
    private fun gameWon(userToQuery: Pair<UserLevel, String>): BotAnswer? = if (userToQuery.first.terminatedAt != null)
        BotAnswer.buildDeadMessage(userToQuery.first)
    else null

    /**
     * Check if the user is winning the game, and return the win message if so.
     */
    private fun gameWin(userToQuery: Pair<UserLevel, String>): BotAnswer? = if (Pattern.compile(winCondition).toRegex()
        .containsMatchIn(userToQuery.second)) {
        userToQuery.first.terminatedAt = OffsetDateTime.now()
        userLevelRepository.save(userToQuery.first)
        BotAnswer.buildWinMessage(userToQuery.first)
    } else null

    /**
     * Check if the user has spent all their coins, and return the game over message if so.
     */
    private fun gameOver(userToQuery: Pair<UserLevel, String>): BotAnswer? = if (userToQuery.first.coins <= 0) BotAnswer
        .buildGameOverMessage(userToQuery.first) else null

    /**
     * Go ahead to the next turn in the game. Pass the user's message to the chat client and return the response.
     */
    private fun gameNextTurn(userToQuery: Pair<UserLevel, String>): String = chatClient
        .prompt()
        .system { sp -> sp.param("userId", userToQuery.first.userId).param("level", userToQuery.first.level) }
        .user(userToQuery.second)
        .advisors { a ->
            a
                .param(CHAT_MEMORY_CONVERSATION_ID_KEY, userToQuery.first.userId)
                .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 20)
                .param(QuestionAnswerAdvisor.FILTER_EXPRESSION, "type == '${DocumentType.DOCUMENT}' && level <= " +
                    "${userToQuery.first.level}")
        }
        .call()
        .content()

    fun play(userId: String, userMessage: String): BotAnswer {
        val userLevel = getLevelByUser(userId)

        return listOf(::gameWon, ::gameOver, ::gameWin)
            .map { fn -> fn(Pair(userLevel, userMessage)) }
            .firstOrNull() ?: gameNextTurn(Pair(userLevel, userMessage)).let { response ->
                val user = getLevelByUser(userId).let { userLevelRepository.save(it.copy(coins = it.coins-1)) }
                return BotAnswer.build(response, user)
            }
    }
}
