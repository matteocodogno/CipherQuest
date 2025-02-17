package code.nebula.cipherquest.configuration

import org.springframework.ai.embedding.EmbeddingModel
import org.springframework.ai.vectorstore.PgVectorStore
import org.springframework.ai.vectorstore.VectorStore
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.core.JdbcTemplate

@Configuration
class VectorStoreConfiguration(
    private val jdbcTemplate: JdbcTemplate,
    private val embeddingModel: EmbeddingModel,
) {
    @Bean
    fun levelUpQuestionVectorStore(): VectorStore =
        PgVectorStore
            .Builder(jdbcTemplate, embeddingModel)
            .withVectorTableName("level_up_question")
            .build()

    @Bean
    fun protectedQuestionVectorStore(): VectorStore =
        PgVectorStore
            .Builder(jdbcTemplate, embeddingModel)
            .withVectorTableName("protected_question")
            .build()
}
