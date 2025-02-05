package code.nebula.cipherquest.configuration.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "overmind.feature")
data class FeatureFlags(
    val sendEmail: Boolean = false,
)
