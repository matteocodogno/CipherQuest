package code.nebula.cipherquest.repository.gcs

import code.nebula.cipherquest.configuration.properties.CloudStorageProperties
import com.fasterxml.jackson.databind.ObjectMapper
import com.google.cloud.storage.Blob
import com.google.cloud.storage.BlobId
import com.google.cloud.storage.Storage
import org.springframework.stereotype.Repository

@Repository
class EmailTemplateRepository(
    override val objectMapper: ObjectMapper,
    override val storage: Storage,
    private val cloudStorageProperties: CloudStorageProperties,
) : GcsStreamRepository(objectMapper, storage) {
    private fun getBlobId(
        storyName: String,
        filename: String,
    ): BlobId =
        BlobId.of(
            cloudStorageProperties.emailTemplatesBucket.name,
            "${cloudStorageProperties.emailTemplatesBucket.folder}/$storyName/$filename",
        )

    fun findUniqueCodeByStoryName(storyName: String): Blob = download(getBlobId(storyName, "unique-code.html"))
}
