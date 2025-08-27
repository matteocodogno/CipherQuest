package code.nebula.cipherquest.service

import code.nebula.cipherquest.components.MessageContext
import code.nebula.cipherquest.configuration.properties.GameConfig
import code.nebula.cipherquest.service.converter.RemoveSurroundingQuotesConverter
import org.springframework.ai.chat.model.ToolContext
import org.springframework.ai.tool.annotation.Tool
import org.springframework.stereotype.Service

@Service
class DocumentTools(
    private val additionalDocumentService: AdditionalDocumentService,
    private val messageContext: MessageContext,
    private val fixedBotMessageService: FixedBotMessageService,
    private val gameConfig: GameConfig,
) {
    @Tool(
        description = "Retrieve the list of types of the documents available.",
    )
    private fun getAvailableDocumentTypes(toolContext: ToolContext): Set<String> =
        additionalDocumentService
            .getAvailableDocumentTypes(gameConfig.storyName)

    @Tool(
        description = "Retrieve the documents of the given specific type among the ones available.",
        returnDirect = true,
        resultConverter = RemoveSurroundingQuotesConverter::class,
    )
    fun getDocuments(
        type: String,
        toolContext: ToolContext,
    ) = additionalDocumentService
        .getDocuments(
            type,
            toolContext.context.getOrDefault("level", 1) as Int,
            gameConfig.storyName,
        ).takeUnless { it.isEmpty() }
        ?.let { documents ->
            messageContext.sources = documents
            fixedBotMessageService.getDocumentMessage(
                toolContext.context.getOrDefault("userId", "") as String,
                gameConfig.storyName,
            )
        } ?: "No documents of type $type found."
}
