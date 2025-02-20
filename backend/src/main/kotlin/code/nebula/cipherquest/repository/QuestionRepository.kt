package code.nebula.cipherquest.repository

import code.nebula.cipherquest.models.requests.QuestionRequest
import code.nebula.cipherquest.repository.entities.Question
import org.springframework.ai.document.Document
import org.springframework.ai.vectorstore.VectorStore
import org.springframework.data.repository.NoRepositoryBean
import org.springframework.jdbc.core.JdbcTemplate
import java.sql.ResultSet

@NoRepositoryBean
abstract class QuestionRepository<REQUEST : QuestionRequest, TYPE : Question>(
    private val vectorStore: VectorStore,
    private val jdbcTemplate: JdbcTemplate,
    private val additionalColumns: Map<String, (REQUEST) -> Any> = emptyMap(),
) {
    abstract val tableName: String

    abstract fun buildQuestion(rs: ResultSet): TYPE

    open fun save(
        newQuestions: List<REQUEST>,
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

    private fun findAllByStoryName(storyName: String): List<TYPE> {
        val query =
            """
            SELECT id, content, metadata->>'storyName' as storyName 
            ${additionalColumns.keys.joinToString { ", metadata->>'$it' as $it" }}
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

    private fun getDocumentMetadata(
        question: REQUEST,
        storyName: String,
    ): Map<String, Any> =
        buildMap {
            put("storyName", storyName)
            putAll(
                additionalColumns.entries
                    .associate { (key, value) -> key to value(question) },
            )
        }
}
