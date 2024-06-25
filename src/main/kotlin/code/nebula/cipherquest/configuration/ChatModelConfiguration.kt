package code.nebula.cipherquest.configuration

import org.springframework.ai.ollama.OllamaChatModel
import org.springframework.ai.ollama.api.OllamaApi
import org.springframework.ai.ollama.api.OllamaOptions
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ChatModelConfiguration {
    @Bean
    fun chatModel(): OllamaChatModel =
        OllamaChatModel(
            OllamaApi(),
            OllamaOptions
                .create()
                .withModel("llama3")
                .withTemperature(0.8f),
        )
}
