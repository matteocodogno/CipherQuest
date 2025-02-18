package code.nebula.cipherquest.repository

import code.nebula.cipherquest.exceptions.DocumentNotFoundException
import code.nebula.cipherquest.models.dto.Info
import code.nebula.cipherquest.models.dto.Message
import code.nebula.cipherquest.models.dto.Sender
import code.nebula.cipherquest.models.dto.Source
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository
import java.time.OffsetDateTime
import java.util.UUID

@Repository
@Suppress("SwallowedException")
class VectorStoreRepository(
    val jdbcTemplate: JdbcTemplate,
) {
    fun getDocumentById(id: String?): String? {
        val sql = "SELECT content FROM vector_store WHERE id = ?"
        return try {
            jdbcTemplate.queryForObject(sql, String::class.java, UUID.fromString(id))
        } catch (e: EmptyResultDataAccessException) {
            throw DocumentNotFoundException("Document with ID $id not found")
        } catch (e: IllegalArgumentException) {
            throw DocumentNotFoundException("Invalid UUID $id")
        }
    }

    fun existsDocumentWithSource(source: String?): Boolean {
        val sql = "SELECT EXISTS (SELECT 1 FROM vector_store WHERE metadata->>'source' = ?)"
        return jdbcTemplate.queryForObject(sql, Boolean::class.java, source)
    }

    fun updateInfo(
        id: String,
        info: String,
    ): Boolean {
        val sql = "UPDATE vector_store SET info = to_json(?::json) WHERE id = ?"
        return jdbcTemplate.update(sql, info, UUID.fromString(id)) > 0
    }

    fun getDocumentByFilename(
        source: String?,
        level: Int?,
    ): String? {
        val sql =
            """
            SELECT string_agg(content, '') FROM vector_store WHERE metadata->>'type' = 'DOCUMENT'
            and metadata->>'source' LIKE ? and (metadata->>'level')::integer <= ?;
            """.trimIndent()
        return jdbcTemplate.queryForObject(sql, String::class.java, "%$source%", level)
    }

    fun getAllDiaryPages(level: Int?): List<Source> {
        val sql =
            """
            SELECT id, metadata->>'source' as title FROM vector_store
            WHERE metadata->>'type' = 'DIARY'
            and (metadata->>'level')::integer <= ?;
            """.trimIndent()
        return jdbcTemplate.query(
            sql,
            { rs, _ ->
                Source(
                    id = rs.getString("id"),
                    title =
                        rs
                            .getString("title")
                            .split(".")
                            .getOrNull(1)
                            .orEmpty(),
                )
            },
            level,
        )
    }

    fun getMessageHistoryByUserId(userId: String?): List<Message> {
        val sql =
            """
                SELECT
                    id as id,
                    ROW_NUMBER() OVER (ORDER BY created_at) - 1 AS index,
                    content as message,
                    metadata->>'messageType' as sender,
                    created_at as timestamp,
                    info as info
                FROM vector_store
                WHERE metadata->>'conversationId' = ? ORDER BY created_at
            """
        val objectMapper = jacksonObjectMapper()
        return jdbcTemplate.query(
            sql,
            { rs, _ ->
                Message(
                    id = rs.getString("id"),
                    index = rs.getInt("index"),
                    message = rs.getString("message"),
                    sender = Sender.valueOf(rs.getString("sender")),
                    timestamp = rs.getObject("timestamp", OffsetDateTime::class.java),
                    info =
                        rs.getString("info")?.let { json ->
                            objectMapper.readValue(json, Info::class.java)
                        } ?: Info(),
                )
            },
            userId,
        )
    }

    fun countUserMessages(id: String): Int {
        val sql =
            """
                SELECT COUNT(*)
                FROM vector_store
                WHERE metadata->>'conversationId' = ? and metadata->>'messageType' = 'USER'
            """

        return jdbcTemplate.queryForObject(sql, Int::class.java, id)
    }
}
