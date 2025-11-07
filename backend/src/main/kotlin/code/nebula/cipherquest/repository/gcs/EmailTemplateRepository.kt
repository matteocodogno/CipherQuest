package code.nebula.cipherquest.repository.gcs

import code.nebula.cipherquest.configuration.properties.CloudStorageProperties
import com.fasterxml.jackson.databind.ObjectMapper
import com.google.cloud.storage.Blob
import com.google.cloud.storage.BlobId
import com.google.cloud.storage.Storage
import com.google.cloud.storage.StorageException
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Repository

private val logger = KotlinLogging.logger {}

@Repository
class EmailTemplateRepository(
    override val objectMapper: ObjectMapper,
    override val storage: Storage,
    override val cloudStorageProperties: CloudStorageProperties,
) : GcsStreamRepository(objectMapper, storage, cloudStorageProperties) {
    fun findUniqueCodeByStoryName(storyName: String): Blob = download(getBlobIdEmail(storyName, "email-template.html"))

    /**
     * Retrieves all assets associated with a given story from the email templates bucket.
     * Assets are expected to be located in the "assets" subfolder under the story folder.
     *
     * @param storyName The name of the story whose assets should be retrieved
     * @return List of Blobs representing the assets, or empty list if no assets found or in case of error
     */
    fun getAssetsByStoryName(storyName: String): List<Blob> =
        try {
            val assetsFolder = "${cloudStorageProperties.emailTemplatesBucket.folder}/$storyName/assets/"

            storage
                .list(
                    cloudStorageProperties.emailTemplatesBucket.name,
                    Storage.BlobListOption.prefix(assetsFolder),
                    Storage.BlobListOption.includeFolders(false),
                ).iterateAll()
                .toList()
        } catch (e: StorageException) {
            logger.error(e) { "Failed to retrieve assets for story: $storyName" }
            emptyList()
        }

    fun getBlobIdEmail(
        storyName: String,
        filename: String,
    ): BlobId =
        BlobId.of(
            cloudStorageProperties.emailTemplatesBucket.name,
            "${cloudStorageProperties.emailTemplatesBucket.folder}/$storyName/$filename",
        )
}
