package code.nebula.cipherquest.service

import code.nebula.cipherquest.components.MessageContext
import org.springframework.ai.chat.model.ToolContext
import org.springframework.ai.tool.annotation.Tool
import org.springframework.stereotype.Service

@Service
class DocumentTools(
    private val vectorStoreService: VectorStoreService,
    private val messageContext: MessageContext,
) {
    @Tool(
        description = "give me the Elara Chen's diary pages|give me the diary|show me the diary",
        returnDirect = true,
    )
    fun getDiaryPages(toolContext: ToolContext) =
        vectorStoreService
            .getAllDiaryPages(toolContext.context.getOrDefault("level", 1) as Int)
            .orEmpty()
            .let { diaries ->
                messageContext.sources = diaries
                """
Resource #${toolContext.context.getOrDefault("userId", "")}, here is the Dr. Elara Chen's diary pages that you have
access to as requested.
                """.trimIndent().replace("\n", " ")
            }
}
