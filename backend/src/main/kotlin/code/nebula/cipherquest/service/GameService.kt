package code.nebula.cipherquest.service

import code.nebula.cipherquest.components.MessageContext
import code.nebula.cipherquest.models.DocumentType
import code.nebula.cipherquest.models.dto.BotMessage
import code.nebula.cipherquest.repository.entities.UserLevel
import org.springframework.ai.chat.client.ChatClient
import org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY
import org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_RETRIEVE_SIZE_KEY
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor
import org.springframework.ai.chat.messages.MessageType
import org.springframework.ai.chat.model.ChatResponse
import org.springframework.ai.model.function.FunctionCallback
import org.springframework.ai.model.function.FunctionCallbackContext
import org.springframework.ai.model.function.FunctionCallingOptionsBuilder
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.util.regex.Pattern

@Service
@Suppress("SwallowedException", "TooGenericExceptionCaught", "LongParameterList")
class GameService(
    private val chatClient: ChatClient,
    private val functionChatClient: ChatClient,
    @Value("\${application.win-condition}")
    private val winCondition: String,
    private val userLevelService: UserLevelService,
    private val functionCallbackContext: FunctionCallbackContext,
    private val messageContext: MessageContext,
    private val vectorStoreService: VectorStoreService,
) {
    companion object {
        private const val CHAT_MEMORY_MAX_SIZE = 20
    }

    /**
     * Check if the user has already won the game, and return the final message if so.
     */
    private fun gameWon(userToQuery: Pair<UserLevel, String>): BotMessage? {
        return if (userToQuery.first.terminatedAt != null) {
            val botMessage =
                BotMessage
                    .buildDeadMessage(userToQuery.first)
            vectorStoreService.saveMessage(userToQuery.first.userId, userToQuery.second, MessageType.USER)
            vectorStoreService.saveMessage(userToQuery.first.userId, botMessage.message, MessageType.ASSISTANT)
            return botMessage
        } else {
            null
        }
    }

    /**
     * Check if the user is winning the game, and return the win message if so.
     */
    private fun gameWin(userToQuery: Pair<UserLevel, String>): BotMessage? {
        return if (Pattern
                .compile(winCondition)
                .toRegex()
                .containsMatchIn(userToQuery.second)
        ) {
            userLevelService.hasWon(userToQuery.first.userId)
            val botMessage =
                BotMessage
                    .buildWinMessage(userToQuery.first)
            vectorStoreService.saveMessage(userToQuery.first.userId, userToQuery.second, MessageType.USER)
            vectorStoreService.saveMessage(userToQuery.first.userId, botMessage.message, MessageType.ASSISTANT)
            return botMessage
        } else {
            null
        }
    }

    /**
     * Check if the user has spent all their coins, and return the game over message if so.
     */
    private fun gameOver(userToQuery: Pair<UserLevel, String>): BotMessage? {
        return if (userToQuery.first.coins <= 0) {
            val botMessage =
                BotMessage
                    .buildGameOverMessage(userToQuery.first)
            vectorStoreService.saveMessage(userToQuery.first.userId, userToQuery.second, MessageType.USER)
            vectorStoreService.saveMessage(userToQuery.first.userId, botMessage.message, MessageType.ASSISTANT)
            return botMessage
        } else {
            null
        }
    }

    /**
     * Go ahead to the next turn in the game. Pass the user's message to the chat client and return the response.
     */
    private fun gameNextTurn(userToQuery: Pair<UserLevel, String>): String {
        val chatResponse = createChatResponseUsingProxyToolCalls(userToQuery)
        val toolCallResult = executeToolCalls(chatResponse)

        return toolCallResult ?: fallbackToChatClient(userToQuery) ?: ""
    }

    private fun createChatResponseUsingProxyToolCalls(userToQuery: Pair<UserLevel, String>): ChatResponse? =
        functionChatClient
            .prompt()
            .options(FunctionCallingOptionsBuilder().withProxyToolCalls(true).build())
            .functions("getDocument")
            .system { sp -> sp.param("userId", userToQuery.first.userId).param("level", userToQuery.first.level) }
            .user(userToQuery.second)
            .call()
            .chatResponse()

    private fun executeToolCalls(chatResponse: ChatResponse?): String? =
        chatResponse?.result?.output?.toolCalls?.firstNotNullOfOrNull { toolCall ->
            val callback: FunctionCallback = functionCallbackContext.getFunctionCallback(toolCall.name, null)

            try {
                callback.call(toolCall.arguments())?.removeSurrounding("\"", "\"")
            } catch (e: NullPointerException) {
                return null
            }
        }

    private fun fallbackToChatClient(userToQuery: Pair<UserLevel, String>): String? =
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
                        "type == '${DocumentType.DOCUMENT}' && level <= ${userToQuery.first.level}",
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
                val messageId = vectorStoreService.getLastMessage(userId).id
                vectorStoreService.updateInfo(messageId, messageContext.context)
                return BotMessage.build(response, user, messageContext.context)
            }
    }
}
