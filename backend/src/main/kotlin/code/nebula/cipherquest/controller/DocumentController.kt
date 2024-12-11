package code.nebula.cipherquest.controller

import code.nebula.cipherquest.service.VectorStoreService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/document")
class DocumentController(
    private val vectorStoreService: VectorStoreService,
) {
    @GetMapping("/{id}")
    fun chat(
        @PathVariable id: String,
    ): String? = vectorStoreService.getDocumentById(id)
}
