package code.nebula.cipherquest.service

import org.springframework.ai.chat.client.ChatClient
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor
import org.springframework.ai.chat.memory.ChatMemory
import org.springframework.ai.chat.model.ChatModel
import org.springframework.ai.transformer.KeywordMetadataEnricher
import org.springframework.ai.transformer.SummaryMetadataEnricher
import org.springframework.ai.transformer.SummaryMetadataEnricher.SummaryType
import org.springframework.ai.vectorstore.SearchRequest
import org.springframework.ai.vectorstore.VectorStore
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.Resource
import org.springframework.stereotype.Service
import java.nio.charset.Charset

@Service
class ChatService(
    private val chatModel: ChatModel,
    private val builder: ChatClient.Builder,
    private val vectorStore: VectorStore,
    private val chatMemory: ChatMemory,
) {
    @Value("classpath:/prompts/system-message.st")
    private lateinit var systemMessageResource: Resource

    @Value("classpath:/prompts/text-rag-advice.st")
    private lateinit var textRagAdviceResource: Resource

    fun getChatClient(): ChatClient {
        val userTextAdviceMessage = textRagAdviceResource.getContentAsString(Charset.defaultCharset())

        return builder
            .defaultSystem(systemMessageResource)
            .defaultAdvisors(
                MessageChatMemoryAdvisor(chatMemory),
                QuestionAnswerAdvisor(vectorStore, SearchRequest.defaults(), userTextAdviceMessage),
            ).build()
    }

    fun getSummaryMetadataEnricher(): SummaryMetadataEnricher {
        val summaryTypes =
            listOf(
                SummaryType.PREVIOUS,
                SummaryType.CURRENT,
                SummaryType.NEXT,
            )
        return SummaryMetadataEnricher(chatModel, summaryTypes)
    }

    fun getKeywordMetadataEnricher(): KeywordMetadataEnricher = KeywordMetadataEnricher(chatModel, 3)
}
