package code.nebula.cipherquest.configuration

import code.nebula.cipherquest.advisor.LastMessageMemoryAppenderAdvisor
import code.nebula.cipherquest.advisor.LevelUpAdvisor
import code.nebula.cipherquest.advisor.LoggingAdvisor
import code.nebula.cipherquest.advisor.ProtectedInputAdvisor
import code.nebula.cipherquest.advisor.SanitizeInputAdvisor
import code.nebula.cipherquest.advisor.TitleQuestionAnswerAdvisor
import code.nebula.cipherquest.components.MessageContext
import code.nebula.cipherquest.configuration.properties.GameConfig
import code.nebula.cipherquest.service.FixedBotMessageService
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
    private val protectedQuestionVectorStore: VectorStore,
    private val levelUpQuestionVectorStore: VectorStore,
    private val fixedBotMessageService: FixedBotMessageService,
    private val userLevelService: UserLevelService,
    private val gameConfig: GameConfig,
) {
    @Value("classpath:/prompts/system-message.st")
    private lateinit var systemMessageResource: Resource

    @Value("classpath:/prompts/rag-system-text.st")
    private lateinit var ragSystemTextResource: Resource

    @Bean
    fun chatClient(): ChatClient {
        val ragSystemText = ragSystemTextResource.getContentAsString(Charsets.UTF_8)
        val builder = ChatClient.builder(chatModel)

        return builder
            .defaultSystem(systemMessageResource)
            .defaultAdvisors(
                SanitizeInputAdvisor(),
                ProtectedInputAdvisor(vectorStoreService, protectedQuestionVectorStore, fixedBotMessageService, gameConfig),
                LevelUpAdvisor(levelUpQuestionVectorStore, userLevelService, messageContext),
                VectorStoreChatMemoryAdvisor
                    .builder(vectorStore)
                    .chatMemoryRetrieveSize(gameConfig.ai.chat.historyMaxSize)
                    .build(),
                LastMessageMemoryAppenderAdvisor(vectorStoreService),
                TitleQuestionAnswerAdvisor(
                    vectorStore,
                    SearchRequest
                        .builder()
                        .topK(gameConfig.ai.rag.resultLimit)
                        .similarityThreshold(gameConfig.ai.rag.similarityThreshold)
                        .build(),
                    ragSystemText,
                    messageContext,
                ),
                LoggingAdvisor(),
            ).build()
    }
}
