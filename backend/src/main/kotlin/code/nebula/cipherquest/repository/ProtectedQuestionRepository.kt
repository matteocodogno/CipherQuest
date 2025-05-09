package code.nebula.cipherquest.repository

import code.nebula.cipherquest.models.requests.ProtectedQuestionRequest
import code.nebula.cipherquest.repository.entities.ProtectedQuestion
import org.springframework.ai.vectorstore.VectorStore
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
class ProtectedQuestionRepository(
    protectedQuestionVectorStore: VectorStore,
    jdbcTemplate: JdbcTemplate,
) : QuestionRepository<ProtectedQuestionRequest, ProtectedQuestion>(
        protectedQuestionVectorStore,
        jdbcTemplate,
    ) {
    override val tableName = "protected_question"

    override val questionMapper =
        RowMapper<ProtectedQuestion> { rs, _ ->
            ProtectedQuestion(
                id = UUID.fromString(rs.getString("id")),
                content = rs.getString("content"),
                storyName = rs.getString("storyName"),
            )
        }
}
