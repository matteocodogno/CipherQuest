package code.nebula.cipherquest.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient

@Configuration
class CheatDetectionConfig {
    @Bean
    fun cheatDetectionWebClient(): WebClient =
        WebClient
            .builder()
            .baseUrl("http://localhost:8000")
            .build()
}
