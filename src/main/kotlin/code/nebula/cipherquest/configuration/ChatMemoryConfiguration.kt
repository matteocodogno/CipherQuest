package code.nebula.cipherquest.configuration

import org.springframework.ai.chat.memory.InMemoryChatMemory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ChatMemoryConfiguration {
    @Bean
    fun chatMemory(): InMemoryChatMemory = InMemoryChatMemory() // TODO: implement database chat memory
}
