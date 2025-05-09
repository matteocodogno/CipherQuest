package code.nebula.cipherquest.configuration.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "cipher-quest")
data class GameConfig(
    val storyName: String,
    val winCondition: String,
    val prompt: Prompt,
    val ai: Ai,
) {
    data class Prompt(
        val maxLength: Int,
    )

    data class Ai(
        val rag: Rag,
        val chat: Chat,
    ) {
        data class Rag(
            val resultLimit: Int,
            val similarityThreshold: Double,
        )

        data class Chat(
            val historyMaxSize: Int,
        )
    }
}
