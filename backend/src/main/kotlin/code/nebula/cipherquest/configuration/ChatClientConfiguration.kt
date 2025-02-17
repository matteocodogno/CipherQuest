package code.nebula.cipherquest.configuration

import code.nebula.cipherquest.advisor.LastMessageMemoryAppenderAdvisor
import code.nebula.cipherquest.advisor.LevelUpAdvisor
import code.nebula.cipherquest.advisor.LoggingAdvisor
import code.nebula.cipherquest.advisor.ProtectedInputAdvisor
import code.nebula.cipherquest.advisor.SanitizeInputAdvisor
import code.nebula.cipherquest.advisor.TitleQuestionAnswerAdvisor
import code.nebula.cipherquest.components.MessageContext
import code.nebula.cipherquest.configuration.properties.GameConfig
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
class ChatClientConfiguration(
    private val messageContext: MessageContext,
    private val chatModel: ChatModel,
    private val vectorStore: VectorStore,
    private val vectorStoreService: VectorStoreService,
    private val userLevelService: UserLevelService,
    private val gameConfig: GameConfig,
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
                ProtectedInputAdvisor(vectorStore, vectorStoreService),
                LevelUpAdvisor(vectorStore, userLevelService, messageContext),
                VectorStoreChatMemoryAdvisor(vectorStore, memorySystemText, gameConfig.ai.chat.historyMaxSize),
                LastMessageMemoryAppenderAdvisor(vectorStoreService),
                TitleQuestionAnswerAdvisor(
                    vectorStore,
                    SearchRequest
                        .defaults()
                        .withTopK(gameConfig.ai.rag.resultLimit)
                        .withSimilarityThreshold(gameConfig.ai.rag.similarityThreshold),
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
                ProtectedInputAdvisor(vectorStore, vectorStoreService),
                LoggingAdvisor(),
            ).build()
    }
}
