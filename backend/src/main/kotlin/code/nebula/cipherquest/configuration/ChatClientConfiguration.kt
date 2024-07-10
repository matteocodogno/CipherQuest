package code.nebula.cipherquest.configuration

import code.nebula.cipherquest.advisor.LevelUpAdvisor
import code.nebula.cipherquest.advisor.LoggingAdvisor
import code.nebula.cipherquest.advisor.PgVectorStoreChatMemoryAdvisor
import code.nebula.cipherquest.service.GameService
import org.springframework.ai.chat.client.ChatClient
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor
import org.springframework.ai.chat.model.ChatModel
import org.springframework.ai.vectorstore.SearchRequest
import org.springframework.ai.vectorstore.VectorStore
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.Resource

@Configuration
class ChatClientConfiguration(
    val chatModel: ChatModel,
    val vectorStore: VectorStore,
    private val gameService: GameService,
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
                PgVectorStoreChatMemoryAdvisor(vectorStore, memorySystemText),
//                PromptChatMemoryAdvisor(chatMemory, memorySystemText),
                QuestionAnswerAdvisor(vectorStore, SearchRequest.defaults().withTopK(5), ragSystemText),
                LevelUpAdvisor(gameService),
                LoggingAdvisor(),
            ).build()
    }
}
