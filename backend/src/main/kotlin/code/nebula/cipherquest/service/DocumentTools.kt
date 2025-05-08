package code.nebula.cipherquest.service

import code.nebula.cipherquest.components.MessageContext
import code.nebula.cipherquest.configuration.properties.GameConfig
import code.nebula.cipherquest.service.converter.RemoveSurroundingQuotesConverter
import org.springframework.ai.chat.model.ToolContext
import org.springframework.ai.tool.annotation.Tool
import org.springframework.stereotype.Service

@Service
class DocumentTools(
    private val vectorStoreService: VectorStoreService,
    private val messageContext: MessageContext,
    private val fixedBotMessageService: FixedBotMessageService,
    private val userLevelService: UserLevelService,
    private val gameConfig: GameConfig,
) {
    @Tool(
        description = "give me the Elara Chen's diary pages|give me the diary|show me the diary",
        returnDirect = true,
        resultConverter = RemoveSurroundingQuotesConverter::class,
    )
    fun getDiaryPages(toolContext: ToolContext) =
        vectorStoreService
            .getAllDiaryPages(toolContext.context.getOrDefault("level", 1) as Int)
            .orEmpty()
            .let { diaries ->
                messageContext.sources = diaries
                fixedBotMessageService.getDocumentMessage(
                    userLevelService.getLevelByUser(toolContext.context.getOrDefault("userId", "") as String),
                    gameConfig.storyName,
                )
            }
}
