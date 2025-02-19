package code.nebula.cipherquest.repository

import code.nebula.cipherquest.models.requests.QuestionRequest
import code.nebula.cipherquest.repository.entities.Question
import org.springframework.ai.document.Document
import org.springframework.ai.vectorstore.VectorStore
import org.springframework.data.repository.NoRepositoryBean
import org.springframework.jdbc.core.JdbcTemplate
import java.sql.ResultSet

@NoRepositoryBean
abstract class QuestionRepository<S : QuestionRequest, T : Question>(
    private val vectorStore: VectorStore,
    private val jdbcTemplate: JdbcTemplate,
    tableName: String,
) {
    protected open val getFindAllByStoryNameQuery: String =
        """
        SELECT id, content, metadata->>'storyName' as storyName
        FROM $tableName
        WHERE metadata->>'storyName' = ?
        """.trimIndent()

    open fun save(
        newQuestions: List<S>,
        storyName: String,
    ) {
        findAllByStoryName(storyName)
            .map { existing -> existing.content }
            .let { existingContent ->
                newQuestions
                    .distinctBy { it.content }
                    .filterNot { it.content in existingContent }
                    .map {
                        Document(
                            it.content,
                            getDocumentMetadata(it, storyName),
                        )
                    }.let(vectorStore::add)
            }
    }

    private fun findAllByStoryName(storyName: String): List<T> {
        val query =
            """
            SELECT id, content, metadata->>'storyName' as storyName 
            ${additionalMetadata.joinToString { ", metadata->>'$it' as $it" }}
            FROM $tableName
            WHERE metadata->>'storyName' = ?
            """.trimIndent()

        return jdbcTemplate.query(
            query,
            { rs, _ ->
                buildQuestion(rs)
            },
            storyName,
        )
    }

    abstract fun buildQuestion(rs: ResultSet): T

    open fun getDocumentMetadata(
        question: S,
        storyName: String,
    ): Map<String, Any> =
        mapOf(
            "storyName" to storyName,
        )
}
