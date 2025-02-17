package code.nebula.cipherquest.repository

import code.nebula.cipherquest.models.requests.ProtectedQuestionRequest
import code.nebula.cipherquest.repository.entities.ProtectedQuestion
import org.springframework.ai.document.Document
import org.springframework.ai.vectorstore.VectorStore
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
class ProtectedQuestionRepository(
    private val protectedQuestionVectorStore: VectorStore,
    private val jdbcTemplate: JdbcTemplate,
) {
    fun save(
        questions: List<ProtectedQuestionRequest>,
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
                            mapOf("storyName" to storyName),
                        )
                    }.let(protectedQuestionVectorStore::add)
            }
    }

    private fun findAllByStoryName(storyName: String): List<ProtectedQuestion> {
        val query =
            """
            SELECT id, content, metadata->>'storyName' as storyName
            FROM protected_question
            WHERE metadata->>'storyName' = ?
            """.trimIndent()

        return jdbcTemplate.query(
            query,
            { rs, _ ->
                ProtectedQuestion(
                    id = UUID.fromString(rs.getString("id")),
                    storyName = rs.getString("storyName"),
                    content = rs.getString("content"),
                )
            },
            storyName,
        )
    }
}
