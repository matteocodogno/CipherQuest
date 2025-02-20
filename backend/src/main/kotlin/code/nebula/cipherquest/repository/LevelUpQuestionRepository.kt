package code.nebula.cipherquest.repository

import code.nebula.cipherquest.models.requests.LevelUpQuestionRequest
import code.nebula.cipherquest.repository.entities.LevelUpQuestion
import org.springframework.ai.vectorstore.VectorStore
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository
import java.sql.ResultSet
import java.util.UUID

@Repository
class LevelUpQuestionRepository(
    levelUpQuestionVectorStore: VectorStore,
    jdbcTemplate: JdbcTemplate,
) : QuestionRepository<LevelUpQuestionRequest, LevelUpQuestion>(
        levelUpQuestionVectorStore,
        jdbcTemplate,
        mapOf("level" to LevelUpQuestionRequest::level),
    ) {
    override val tableName = "level_up_question"

    override fun buildQuestion(rs: ResultSet) =
        LevelUpQuestion(
            id = UUID.fromString(rs.getString("id")),
            level = rs.getInt("level"),
            storyName = rs.getString("storyName"),
            content = rs.getString("content"),
        )
}
