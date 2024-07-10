package code.nebula.cipherquest.controller

import code.nebula.cipherquest.advisor.CustomMetadataPdfDocumentReader
import code.nebula.cipherquest.models.DocumentType
import code.nebula.cipherquest.service.VectorStoreService
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.ai.document.Document
import org.springframework.ai.reader.pdf.config.PdfDocumentReaderConfig
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
    val levelUpQuestionList =
        listOf(
            LevelUpQuestion(2, "How did Dr. Elara Chen die?"),
            LevelUpQuestion(2, "Is Dr. Elara Chen alive?"),
            LevelUpQuestion(2, "Is Dr. Elara Chen dead?"),
            LevelUpQuestion(3, "What is the Dr. Elara Chen's first research paper?"),
        )

    @PostMapping("/load")
    fun load() {
        val documents: List<Document> =
            resourcePatternResolver
                .getResources("classpath:documents/*")
                .filterNot { resource ->
                    vectorStoreService.existsDocumentWithFileName(resource.filename ?: "")
                }
                .flatMap { resource ->
                    CustomMetadataPdfDocumentReader(
                        resourceLoader.getResource("classpath:documents/${resource.filename}"),
                        PdfDocumentReaderConfig.builder().withPagesPerDocument(0).build(),
                    ).apply {
                        customMetadata["level"] =
                            resource.filename
                                ?.split(".")
                                ?.getOrNull(0)
                                .orEmpty().toInt()
                        customMetadata["type"] = DocumentType.DOCUMENT
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
            levelUpQuestionList
                .filterNot { q ->
                    vectorStoreService.existsDocumentWithFileName(q.question)
                }.map { d ->
                    Document(
                        d.question,
                        mapOf<String, Any>(
                            "type" to DocumentType.QUESTION,
                            "level" to d.level,
                            "filename" to d.question,
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
