package code.nebula.cipherquest.configuration

import code.nebula.cipherquest.advisor.LoggingAdvisor
import org.springframework.ai.chat.client.ChatClient
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor
import org.springframework.ai.chat.memory.ChatMemory
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
    val chatMemory: ChatMemory,
    val vectorStore: VectorStore,
) {
    @Value("classpath:/prompts/system-message.st")
    private lateinit var systemMessageResource: Resource

    @Value("classpath:/prompts/text-rag-advice.st")
    private lateinit var textRagAdviceResource: Resource

    @Bean
    fun chatClient(): ChatClient {
        val userTextAdviceMessage = textRagAdviceResource.getContentAsString(Charsets.UTF_8)
        val builder = ChatClient.builder(chatModel)

        return builder
            .defaultSystem(systemMessageResource)
            .defaultAdvisors(
//                VectorStoreChatMemoryAdvisor(vectorStore),
                MessageChatMemoryAdvisor(chatMemory),
                LoggingAdvisor(),
                QuestionAnswerAdvisor(vectorStore, SearchRequest.defaults(), userTextAdviceMessage),
            ).build()
    }
}
