package code.nebula.cipherquest.configuration.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "overmind")
data class GameConfig(
    val winCondition: String,
    val prompt: Prompt,
) {
    data class Prompt(
        val maxLength: Int,
    )
}
