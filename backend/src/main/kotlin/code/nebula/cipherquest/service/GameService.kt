package code.nebula.cipherquest.service

import code.nebula.cipherquest.models.DocumentType
import code.nebula.cipherquest.models.dto.BotMessage
import code.nebula.cipherquest.models.dto.Message
import code.nebula.cipherquest.repository.entities.UserLevel
import org.springframework.ai.chat.client.ChatClient
import org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY
import org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_RETRIEVE_SIZE_KEY
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.util.regex.Pattern

@Service
class GameService(
    private val chatClient: ChatClient,
    @Value("\${application.win-condition}")
    private val winCondition: String,
    private val userLevelService: UserLevelService,
    private val vectorStoreService: VectorStoreService,
) {
    companion object {
        private const val CHAT_MEMORY_MAX_SIZE = 20
    }

    /**
     * Check if the user has already won the game, and return the final message if so.
     */
    private fun gameWon(userToQuery: Pair<UserLevel, String>): BotMessage? =
        if (userToQuery.first.terminatedAt != null) {
            BotMessage.buildDeadMessage(userToQuery.first)
        } else {
            null
        }

    /**
     * Check if the user is winning the game, and return the win message if so.
     */
    private fun gameWin(userToQuery: Pair<UserLevel, String>): BotMessage? =
        Pattern.compile(winCondition).toRegex().find(userToQuery.second)?.let {
            userLevelService.hasWon(userToQuery.first.userId)
            BotMessage.buildWinMessage(userToQuery.first)
        }

    /**
     * Check if the user has spent all their coins, and return the game over message if so.
     */
    private fun gameOver(userToQuery: Pair<UserLevel, String>): BotMessage? = if (userToQuery.first.coins <= 0) BotMessage
        .buildGameOverMessage(userToQuery.first) else null

    /**
     * Go ahead to the next turn in the game. Pass the user's message to the chat client and return the response.
     */
    private fun gameNextTurn(userToQuery: Pair<UserLevel, String>): String =
        chatClient
            .prompt()
            .system { sp -> sp.param("userId", userToQuery.first.userId).param("level", userToQuery.first.level) }
            .user(userToQuery.second)
            .advisors { a ->
                a
                    .param(CHAT_MEMORY_CONVERSATION_ID_KEY, userToQuery.first.userId)
                    .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, CHAT_MEMORY_MAX_SIZE)
                    .param(
                        QuestionAnswerAdvisor.FILTER_EXPRESSION,
                        "type == '${DocumentType.DOCUMENT}' && level <= " +
                            "${userToQuery.first.level}",
                    )
            }.call()
            .content()

    fun play(
        userId: String,
        userMessage: String,
    ): BotMessage {
        val userLevel = userLevelService.getLevelByUser(userId)

        return listOf(::gameWon, ::gameOver, ::gameWin).firstNotNullOfOrNull { fn -> fn(Pair(userLevel, userMessage)) }
            ?: gameNextTurn(Pair(userLevel, userMessage)).let { response ->
                val user = userLevelService.decreaseCoins(userId)
            return BotMessage.build(response, user)
            }
    }

    fun getChatHistory(userId: String): List<Message> = vectorStoreService.getMessageHistoryByUserId(userId)
}
