package code.nebula.cipherquest.configuration.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "cipher-quest.cloud.storage")
data class CloudStorageProperties(
    val storiesBucket: BucketProperties,
    val emailTemplatesBucket: BucketProperties,
) {
    data class BucketProperties(
        val name: String,
        val folder: String,
    )
}
