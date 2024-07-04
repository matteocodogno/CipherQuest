package code.nebula.cipherquest.controller

import code.nebula.cipherquest.repository.UserLevelRepository
import org.springframework.ai.chat.client.ChatClient
import org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY
import org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_RETRIEVE_SIZE_KEY
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor
import org.springframework.ai.vectorstore.SearchRequest
import org.springframework.ai.vectorstore.VectorStore
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
) {
    @PostMapping("/{id}")
    fun chat(
        @PathVariable id: String,
        @RequestBody userMessage: String,
    ): String {
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
                .filter { it.metadata["distance"].toString().toFloat() <= 0.2f }
                .minByOrNull { it.metadata["distance"].toString().toFloat() }
                ?.metadata
                ?.get("level")
                ?.toString()
                ?.toInt()
                ?: 0

        if (matchedQuestionLevel > level) {
            userLevelRepository.update(id, matchedQuestionLevel)
            level = matchedQuestionLevel
        }

        return chatClient
            .prompt()
            .user(userMessage)
            .advisors { a ->
                a
                    .param(CHAT_MEMORY_CONVERSATION_ID_KEY, id)
                    .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 100)
                    .param(QuestionAnswerAdvisor.FILTER_EXPRESSION, "type != 'question' && level <= '$level'")
            }.call()
            .content()
    }
}
