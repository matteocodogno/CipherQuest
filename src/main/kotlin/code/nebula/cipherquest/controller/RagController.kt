package code.nebula.cipherquest.controller

import code.nebula.cipherquest.service.VectorStoreService
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.ai.reader.TextReader
import org.springframework.core.io.ResourceLoader
import org.springframework.util.ResourceUtils
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.nio.charset.Charset

private val logger = KotlinLogging.logger {}

@RestController
@RequestMapping("/rag")
class RagController(
    val vectorStoreService: VectorStoreService,
    val resourceLoader: ResourceLoader,
) {
    @PostMapping("/load")
    fun load() {
        ResourceUtils
            .getFile("classpath:documents/")
            .listFiles()
            ?.mapTo(ArrayList()) { file ->
                TextReader(resourceLoader.getResource("classpath:documents/${file.name}"))
                    .let { reader ->
                        reader.customMetadata.putAll(mapOf("level" to file.name.split("-")[1]))
                        reader.charset = Charset.defaultCharset()
                        reader.get()
                    }
            }?.forEach { documents ->
                logger.info { "Loading ${documents.size} documents" }
                vectorStoreService.loadDocument(documents)
            }

//        vectorStoreService.loadDocument(documents)
    }
}
