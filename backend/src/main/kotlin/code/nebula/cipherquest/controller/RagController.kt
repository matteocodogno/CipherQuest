package code.nebula.cipherquest.controller

import code.nebula.cipherquest.models.DocumentType
import code.nebula.cipherquest.service.LevelUpQuestions
import code.nebula.cipherquest.service.RedactedQuestions
import code.nebula.cipherquest.service.VectorStoreService
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.ai.document.Document
import org.springframework.ai.reader.TextReader
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
                    vectorStoreService.existsDocumentWithSource(resource.filename ?: "")
                }.flatMap { resource ->
                    TextReader(
                        resourceLoader.getResource("classpath:documents/${resource.filename}"),
                    ).apply {
                        val filename =
                            resource.filename
                                ?.split(".")
                                ?.getOrNull(1)
                                .orEmpty()

                        customMetadata["level"] =
                            resource.filename
                                ?.split(".")
                                ?.getOrNull(0)
                                .orEmpty()
                                .toInt()

                        customMetadata["type"] =
                            if (filename.contains("diary", true)) {
                                DocumentType.DIARY
                            } else {
                                DocumentType.DOCUMENT
                            }
                    }.get()
                }

        if (documents.isNotEmpty()) {
            documents.forEach { document ->
                vectorStoreService
                    .loadDocument(listOf(document))
            }
            logger.info { "Loading ${documents.size} documents" }
        } else {
            logger.info { "No new documents to load" }
        }
    }

    @PostMapping("/loadQuestions")
    fun loadQuestions() {
        val documents: List<Document> =
            LevelUpQuestions.levelUpQuestionList
                .filterNot { q ->
                    vectorStoreService.existsDocumentWithSource(q.question)
                }.map { d ->
                    Document(
                        d.question,
                        mapOf<String, Any>(
                            "type" to DocumentType.QUESTION,
                            "level" to d.level,
                            "source" to d.question,
                        ),
                    )
                }

        if (documents.isNotEmpty()) {
            logger.info { "Loading ${documents.size} questions" }
            vectorStoreService.loadDocument(documents)
        } else {
            logger.info { "No new questions to load" }
        }
    }

    @PostMapping("/loadRedacted")
    fun loadRedacted() {
        val documents: List<Document> =
            RedactedQuestions.redactedQuestionsList
                .filterNot { q ->
                    vectorStoreService.existsDocumentWithSource(q)
                }.map { d ->
                    Document(
                        d,
                        mapOf<String, Any>(
                            "type" to DocumentType.REDACTED,
                            "source" to d,
                        ),
                    )
                }

        if (documents.isNotEmpty()) {
            logger.info { "Loading ${documents.size} questions" }
            vectorStoreService.loadDocument(documents)
        } else {
            logger.info { "No new questions to load" }
        }
    }
}
