package code.nebula.cipherquest.repository

import code.nebula.cipherquest.models.requests.LevelUpQuestionRequest
import code.nebula.cipherquest.repository.entities.LevelUpQuestion
import org.springframework.ai.document.Document
import org.springframework.ai.vectorstore.VectorStore
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
class LevelUpQuestionRepository(
    private val levelUpQuestionVectorStore: VectorStore,
    private val jdbcTemplate: JdbcTemplate,
) {
    fun save(
        questions: List<LevelUpQuestionRequest>,
        storyName: String,
    ) {
        findAllByStoryName(storyName)
            .map { existing -> existing.content }
            .let { existingContent ->
                questions
                    .distinctBy { it.content }
                    .filterNot { it.content in existingContent }
                    .map {
                        Document(
                            it.content,
                            mapOf("storyName" to storyName, "level" to it.level),
                        )
                    }.let(levelUpQuestionVectorStore::add)
            }
    }

    private fun findAllByStoryName(storyName: String): List<LevelUpQuestion> {
        val query =
            """
            SELECT id, content, metadata->>'storyName' as storyName, metadata->>'level' as level
            FROM level_up_question
            WHERE metadata->>'storyName' = ?
            """.trimIndent()

        return jdbcTemplate.query(
            query,
            { rs, _ ->
                LevelUpQuestion(
                    id = UUID.fromString(rs.getString("id")),
                    level = rs.getInt("level"),
                    storyName = rs.getString("storyName"),
                    content = rs.getString("content"),
                )
            },
            storyName,
        )
    }
}
