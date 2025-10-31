package code.nebula.cipherquest.repository.gcs

import code.nebula.cipherquest.configuration.properties.CloudStorageProperties
import com.fasterxml.jackson.databind.ObjectMapper
import com.google.cloud.storage.Blob
import com.google.cloud.storage.Storage
import org.springframework.stereotype.Repository

@Repository
class StoryRepository(
    override val objectMapper: ObjectMapper,
    override val storage: Storage,
    override val cloudStorageProperties: CloudStorageProperties,
) : GcsStreamRepository(objectMapper, storage, cloudStorageProperties) {
    fun findByStoryName(storyName: String): List<Blob> =
        storage
            .list(
                cloudStorageProperties.storiesBucket.name,
                Storage.BlobListOption.prefix("${cloudStorageProperties.storiesBucket.folder}/$storyName/"),
            ).values
            .map(Blob::getBlobId)
            .map(::download)
}
