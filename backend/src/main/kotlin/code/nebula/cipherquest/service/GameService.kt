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
    @Value("\${overmind.prompt.max-length}")
    private val promptMaxLength: Int,
    private val userLevelService: UserLevelService,
    private val functionCallbackContext: FunctionCallbackContext,
    private val messageContext: MessageContext,
    private val vectorStoreService: VectorStoreService,
) {
    companion object {
        private const val CHAT_MEMORY_MAX_SIZE = 20
        private const val MAX_LEVEL = 3
        private const val MIN_QUESTIONS = 6
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
            val messageId = vectorStoreService.getLastMessage(userToQuery.first.userId).id
            vectorStoreService.updateInfo(messageId, botMessage.info)
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
            val messageId = vectorStoreService.getLastMessage(userToQuery.first.userId).id
            vectorStoreService.updateInfo(messageId, botMessage.info)
            return botMessage
        } else {
            null
        }
    }

    private fun detectCheat(userToQuery: Pair<UserLevel, String>): BotMessage? {
        val (userLevel, userMessage) = userToQuery
        val userId = userLevel.userId

        return Regex(winCondition)
            .takeIf {
                it.containsMatchIn(userMessage) &&
                    userLevel.level < MAX_LEVEL &&
                    vectorStoreService.countUserMessages(userId) < MIN_QUESTIONS
            }?.let {
                userLevelService.hasCheated(userId)
                val botMessage = BotMessage.buildCheatMessage(userLevel)

                vectorStoreService.saveMessage(userId, userMessage, MessageType.USER)
                vectorStoreService.saveMessage(userId, botMessage.message, MessageType.ASSISTANT)
                val messageId = vectorStoreService.getLastMessage(userToQuery.first.userId).id
                vectorStoreService.updateInfo(messageId, botMessage.info)
                botMessage
            }
    }

    /**
     * Go ahead to the next turn in the game. Pass the user's message to the chat client and return the response.
     */
    private fun gameNextTurn(userToQuery: Pair<UserLevel, String>): String {
        val chatResponse = createChatResponseUsingProxyToolCalls(userToQuery)
        val toolCallResult = executeToolCalls(chatResponse)

        if (toolCallResult != null) {
            vectorStoreService.saveMessage(userToQuery.first.userId, userToQuery.second, MessageType.USER)
            vectorStoreService.saveMessage(userToQuery.first.userId, toolCallResult, MessageType.ASSISTANT)
        }

        return toolCallResult ?: fallbackToChatClient(userToQuery) ?: ""
    }

    private fun createChatResponseUsingProxyToolCalls(userToQuery: Pair<UserLevel, String>): ChatResponse? =
        functionChatClient
            .prompt()
            .options(FunctionCallingOptionsBuilder().withProxyToolCalls(true).build())
            .functions("getDiaryPages")
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
        val userMessageTruncated: String = userMessage.take(promptMaxLength)

        val userLevel = userLevelService.getLevelByUser(userId)

        return listOf(::detectCheat, ::gameOver, ::gameWin)
            .firstNotNullOfOrNull { fn -> fn(Pair(userLevel, userMessageTruncated)) }
            ?: gameNextTurn(Pair(userLevel, userMessageTruncated)).let { response ->
                val user = userLevelService.decreaseCoins(userId)
                val messageId = vectorStoreService.getLastMessage(userId).id
                vectorStoreService.updateInfo(messageId, messageContext.context)
                return BotMessage.build(response, user, messageContext.context)
            }
    }
}
