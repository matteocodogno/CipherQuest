package code.nebula.cipherquest.controller

import code.nebula.cipherquest.models.CustomByteArrayResource
import code.nebula.cipherquest.models.DocumentType
import code.nebula.cipherquest.repository.gcs.StoryRepository
import code.nebula.cipherquest.service.LevelUpQuestions
import code.nebula.cipherquest.service.RedactedQuestions
import code.nebula.cipherquest.service.VectorStoreService
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.ai.document.Document
import org.springframework.ai.reader.TextReader
import org.springframework.core.io.ResourceLoader
import org.springframework.core.io.support.ResourcePatternResolver
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.nio.file.Path

private val logger = KotlinLogging.logger {}

@RestController
@RequestMapping("/rag")
class RagController(
    val vectorStoreService: VectorStoreService,
    val resourceLoader: ResourceLoader,
    val resourcePatternResolver: ResourcePatternResolver,
    private val storyRepository: StoryRepository,
) {
    @PostMapping("/load/{storyName}")
    fun load(
        @PathVariable storyName: String,
    ) {
        val documents =
            storyRepository
                .findByStoryName(storyName)
                .map { blob -> Path.of(blob.name).fileName.toString() to blob }
                .filterNot { (filename, _) -> filename.contains(storyName) }
                .filterNot { (filename, _) -> vectorStoreService.existsDocumentWithSource(filename) }
                .flatMap { (originalFilename, blob) ->
                    CustomByteArrayResource(blob.getContent(), originalFilename).let { resource ->
                        TextReader(resource)
                            .apply {
                                customMetadata["source"] = originalFilename

                                val filename =
                                    originalFilename
                                        .split(".")
                                        .getOrNull(1)
                                        .orEmpty()

                                customMetadata["level"] =
                                    originalFilename
                                        .split(".")
                                        .getOrNull(0)
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
