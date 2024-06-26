package code.nebula.cipherquest.configuration;

import org.springframework.ai.embedding.*;
import org.springframework.ai.transformers.*;
import org.springframework.context.annotation.*;

@Configuration
public class EmbeddingsConfiguration {
    @Bean
    public TransformersEmbeddingModel embeddingModel() {
        return new TransformersEmbeddingModel();
    }
}
