package code.nebula.cipherquest.service

import org.springframework.ai.chat.model.ChatModel
import org.springframework.ai.transformer.KeywordMetadataEnricher
import org.springframework.ai.transformer.SummaryMetadataEnricher
import org.springframework.ai.transformer.SummaryMetadataEnricher.SummaryType
import org.springframework.stereotype.Service

@Service
class ChatService(
    private val chatModel: ChatModel,
) {
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
