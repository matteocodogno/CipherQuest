package code.nebula.cipherquest.repository

import code.nebula.cipherquest.configuration.properties.CloudStorageProperties
import com.google.cloud.storage.Blob
import com.google.cloud.storage.Storage
import org.springframework.stereotype.Repository

@Repository
class GCloudRepository(
    private val cloudStorageProperties: CloudStorageProperties,
    private val storage: Storage,
) {
    fun loadBlob(storyName: String): Blob {
        val blob =
            storage[
                cloudStorageProperties.storiesBucket.name,
                "${cloudStorageProperties.storiesBucket.folder}/$storyName/loadContent.json",
            ] ?: throw IllegalArgumentException("File not found in bucket ${cloudStorageProperties.storiesBucket.name}")

        return blob
    }
}
