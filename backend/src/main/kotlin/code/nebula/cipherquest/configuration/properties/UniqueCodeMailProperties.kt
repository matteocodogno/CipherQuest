package code.nebula.cipherquest.configuration.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "cipher-quest.mail.unique-code")
data class UniqueCodeMailProperties(
    val subject: String = "",
    val from: String = "",
)
