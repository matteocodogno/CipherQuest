package code.nebula.cipherquest.configuration

import code.nebula.cipherquest.components.MessageContext
import code.nebula.cipherquest.models.dto.Source
import code.nebula.cipherquest.service.VectorStoreService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Description

@Configuration
class DiaryPagesFunctionConfiguration {
    @Bean
    @Description("give me the Elara Chen's diary pages|give me the diary|show me the diary")
    fun getDiaryPages(
        vectorStoreService: VectorStoreService,
        messageContext: MessageContext,
    ): java.util.function.Function<DiaryRequest, String> =
        java.util.function.Function { req ->
            req.let { (userId, level) ->
                val allDiaryPages: List<Source>? = vectorStoreService.getAllDiaryPages(level)
                messageContext.sources = allDiaryPages.orEmpty()

                "Resource #$userId, here is the Dr. Elara Chen's diary pages that you have access to as requested."
            }
        }
}

data class DiaryRequest(
    val userId: String,
    val level: Int,
) {
    constructor() : this(userId = "", level = 1)
}
