package code.nebula.cipherquest.configuration

import code.nebula.cipherquest.advisor.LastMessageMemoryAppenderAdvisor
import code.nebula.cipherquest.advisor.LevelUpAdvisor
import code.nebula.cipherquest.advisor.LoggingAdvisor
import code.nebula.cipherquest.advisor.RedactInputAdvisor
import code.nebula.cipherquest.advisor.SanitizeInputAdvisor
import code.nebula.cipherquest.advisor.TitleQuestionAnswerAdvisor
import code.nebula.cipherquest.components.MessageContext
import code.nebula.cipherquest.service.UserLevelService
import code.nebula.cipherquest.service.VectorStoreService
import org.springframework.ai.chat.client.ChatClient
import org.springframework.ai.chat.client.advisor.VectorStoreChatMemoryAdvisor
import org.springframework.ai.chat.model.ChatModel
import org.springframework.ai.vectorstore.SearchRequest
import org.springframework.ai.vectorstore.VectorStore
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.Resource

@Configuration
@Suppress("LongParameterList")
class ChatClientConfiguration(
    private val messageContext: MessageContext,
    private val chatModel: ChatModel,
    private val vectorStore: VectorStore,
    private val vectorStoreService: VectorStoreService,
    private val userLevelService: UserLevelService,
    @Value("\${overmind.ai.chat.history.max-size}")
    private val chatHistoryWindowSize: Int,
    @Value("\${overmind.ai.rag.result-limit}")
    private val requestLimitRag: Int,
    @Value("\${overmind.ai.rag.similarity-threshold}")
    private val similarityThreshold: Double,
) {
    @Value("classpath:/prompts/system-message.st")
    private lateinit var systemMessageResource: Resource

    @Value("classpath:/prompts/rag-system-text.st")
    private lateinit var ragSystemTextResource: Resource

    @Value("classpath:/prompts/memory-text.st")
    private lateinit var memoryTextResource: Resource

    @Bean
    fun chatClient(): ChatClient {
        val ragSystemText = ragSystemTextResource.getContentAsString(Charsets.UTF_8)
        val memorySystemText = memoryTextResource.getContentAsString(Charsets.UTF_8)
        val builder = ChatClient.builder(chatModel)

        return builder
            .defaultSystem(systemMessageResource)
            .defaultAdvisors(
                SanitizeInputAdvisor(),
                RedactInputAdvisor(vectorStore, vectorStoreService),
                LevelUpAdvisor(vectorStore, userLevelService, messageContext),
                VectorStoreChatMemoryAdvisor(vectorStore, memorySystemText, chatHistoryWindowSize),
                LastMessageMemoryAppenderAdvisor(vectorStoreService),
                TitleQuestionAnswerAdvisor(
                    vectorStore,
                    SearchRequest
                        .defaults()
                        .withTopK(requestLimitRag)
                        .withSimilarityThreshold(similarityThreshold),
                    ragSystemText,
                    messageContext,
                ),
                LoggingAdvisor(),
            ).build()
    }

    @Bean
    fun functionChatClient(): ChatClient {
        val builder = ChatClient.builder(chatModel)

        return builder
            .defaultSystem(systemMessageResource)
            .defaultAdvisors(
                RedactInputAdvisor(vectorStore, vectorStoreService),
                LoggingAdvisor(),
            ).build()
    }
}
