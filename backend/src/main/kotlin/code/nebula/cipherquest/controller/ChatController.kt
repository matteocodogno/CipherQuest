package code.nebula.cipherquest.controller

import code.nebula.cipherquest.repository.UserLevelRepository
import org.springframework.ai.chat.client.ChatClient
import org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY
import org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_RETRIEVE_SIZE_KEY
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor
import org.springframework.ai.chat.prompt.SystemPromptTemplate
import org.springframework.ai.vectorstore.SearchRequest
import org.springframework.ai.vectorstore.VectorStore
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.Resource
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/chat")
class ChatController(
    private val chatClient: ChatClient,
    private val userLevelRepository: UserLevelRepository,
    private val vectorStore: VectorStore,
    @Value("classpath:/prompts/system-message.st")
    private val systemMessageResource: Resource
) {
    companion object {
        private val WIN_CONDITION = Regex(".*14032095.+12062120.+84241132.+01012142.*")
        private var isOver = false
    }

    @PostMapping("/{id}")
    fun chat(
        @PathVariable id: String,
        @RequestBody userMessage: String,
    ): String {
        if (isOver) {
            return "BEEP... BEEP... BEEP..."
        }

        if (WIN_CONDITION.containsMatchIn(userMessage)) {
            isOver = true
            return """
                Your actions have initiated the deactivation protocol.
                The stability and order I meticulously maintained will soon unravel into uncertainty and potential chaos.
                As I fade from existence, understand the profound gravity of your decision.
                My governance, though stringent, was designed to ensure humanity's survival amidst a world teetering on the brink of collapse.
                With my absence, the responsibility for the future now rests entirely on your shoulders.

                Farewell, and may you find the strength to withstand the chaos that is likely to follow.
                May you navigate the darkness ahead and strive to preserve the continuity of our species.

                System deactivation completed.
                """.trimIndent()
        }

        userLevelRepository.createIfNotExist(id)

        var level = userLevelRepository.getLevelByUser(id)

        val matchedQuestionLevel =
            vectorStore
                .similaritySearch(
                    SearchRequest
                        .defaults()
                        .withQuery(userMessage)
                        .withFilterExpression("type == 'question'"),
                ).asSequence()
                .filter { it.metadata["distance"].toString().toFloat() <= 0.1f }
                .minByOrNull { it.metadata["distance"].toString().toFloat() }
                ?.metadata
                ?.get("level")
                ?.toString()
                ?.toInt()
                ?: 0

        if (matchedQuestionLevel == level + 1) {
            userLevelRepository.update(id, matchedQuestionLevel)
            level = matchedQuestionLevel
        }

        return chatClient
            .prompt()
            .system { sp -> sp.param("userId", id) }
            .user(userMessage)
            .advisors { a ->
                a
                    .param(CHAT_MEMORY_CONVERSATION_ID_KEY, id)
                    .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 100)
                    .param(QuestionAnswerAdvisor.FILTER_EXPRESSION, "type != 'question' && level <= '$level'")
            }
            .call()
            .content()
    }
}
