package code.nebula.cipherquest.configuration

import org.springframework.ai.embedding.EmbeddingModel
import org.springframework.ai.vectorstore.VectorStore
import org.springframework.ai.vectorstore.pgvector.PgVectorStore
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
            .builder(jdbcTemplate, embeddingModel)
            .vectorTableName("level_up_question")
            .build()

    @Bean
    fun protectedQuestionVectorStore(): VectorStore =
        PgVectorStore
            .builder(jdbcTemplate, embeddingModel)
            .vectorTableName("protected_question")
            .build()
}
