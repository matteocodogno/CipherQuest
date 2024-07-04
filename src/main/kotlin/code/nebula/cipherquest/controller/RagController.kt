package code.nebula.cipherquest.controller

import code.nebula.cipherquest.advisor.CustomMetadataPdfDocumentReader
import code.nebula.cipherquest.service.VectorStoreService
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.ai.document.Document
import org.springframework.core.io.ResourceLoader
import org.springframework.core.io.support.ResourcePatternResolver
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

private val logger = KotlinLogging.logger {}

@RestController
@RequestMapping("/rag")
class RagController(
    val vectorStoreService: VectorStoreService,
    val resourceLoader: ResourceLoader,
    val resourcePatternResolver: ResourcePatternResolver,
) {
    @PostMapping("/load")
    fun load() {
        val documents: List<Document> =
            resourcePatternResolver
                .getResources("classpath:documents/*")
                .filterNot { resource ->
                    vectorStoreService.existsDocumentWithFileName(resource.filename ?: "")
                }.flatMap { resource ->
                    CustomMetadataPdfDocumentReader(
                        resourceLoader.getResource("classpath:documents/${resource.filename}"),
                    ).apply {
                        customMetadata["level"] =
                            resource.filename
                                ?.split(".")
                                ?.getOrNull(0)
                                .orEmpty()
                        customMetadata["type"] = "document"
                    }.get()
                }

        if (documents.isNotEmpty()) {
            logger.info { "Loading ${documents.size} documents" }
            vectorStoreService.loadDocument(documents)
        } else {
            logger.info { "No new documents to load" }
        }
    }

    @PostMapping("/loadQuestions")
    fun loadQuestions() {
        val documents: List<Document> =
            listOf(
                LevelUpQuestion(2, "How Dr. Elara Chen died?"),
            ).filterNot { q ->
                vectorStoreService.existsDocumentWithFileName(q.question)
            }.map { d ->
                Document(
                    d.question,
                    mapOf<String, Any>(
                        "type" to "question",
                        "level" to d.level,
                        "filename" to d.question,
                    ),
                )
            }

        if (documents.isNotEmpty()) {
            logger.info { "Loading ${documents.size} documents" }
            vectorStoreService.loadDocument(documents)
        } else {
            logger.info { "No new documents to load" }
        }
    }
}
