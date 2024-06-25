package code.nebula.cipherquest.configuration

import org.springframework.ai.embedding.EmbeddingModel
import org.springframework.ai.ollama.OllamaEmbeddingModel
import org.springframework.ai.ollama.api.OllamaApi
import org.springframework.ai.ollama.api.OllamaOptions
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class EmbeddingModelConfiguration {
    @Bean
    fun embeddingModel(): EmbeddingModel =
        OllamaEmbeddingModel(
            OllamaApi(),
            OllamaOptions
                .create()
                .withModel("llama3"),
        )
}
