package code.nebula.cipherquest.service

import code.nebula.cipherquest.models.dto.Source
import code.nebula.cipherquest.repository.AdditionalDocumentRepository
import org.springframework.stereotype.Service

@Service
class AdditionalDocumentService(
    private val additionalDocumentRepository: AdditionalDocumentRepository,
) {
    fun getDocuments(
        type: String,
        level: Int,
        storyName: String,
    ): List<Source> =
        additionalDocumentRepository
            .getDocumentsByTypeAndLevelLessThanEqualAndStoryName(type, level, storyName)
            .map { Source(it.id, it.source) }

    fun getAvailableDocumentTypes(storyName: String): Set<String> =
        additionalDocumentRepository
            .getDocumentsByStoryName(storyName)
            .map { it.type }
            .toSet()
}
