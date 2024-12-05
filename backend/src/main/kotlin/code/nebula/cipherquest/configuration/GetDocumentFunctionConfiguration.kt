package code.nebula.cipherquest.configuration

import code.nebula.cipherquest.service.VectorStoreService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Description

@Configuration
class GetDocumentFunctionConfiguration {
    @Bean
    @Description("get document by name")
    fun getDocument(vectorStoreService: VectorStoreService): java.util.function.Function<Request, String?> =
        java.util.function.Function { req ->
            req.let { (filename, level) ->
                vectorStoreService
                    .getDocumentByFilename(filename, level)
            }
        }
}

data class Request(
    val filename: String,
    val level: Int,
) {
    constructor() : this(filename = "", level = 1)
}