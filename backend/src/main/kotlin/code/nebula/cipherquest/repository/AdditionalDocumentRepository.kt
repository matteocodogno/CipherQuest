package code.nebula.cipherquest.repository

import code.nebula.cipherquest.repository.entities.AdditionalDocument
import org.springframework.data.repository.ListCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface AdditionalDocumentRepository : ListCrudRepository<AdditionalDocument, String> {
    fun getDocumentsByTypeAndLevelLessThanEqualAndStoryName(
        type: String,
        level: Int,
        storyName: String,
    ): List<AdditionalDocument>

    fun getDocumentsByStoryName(storyName: String): List<AdditionalDocument>
}
