package code.nebula.cipherquest.repository

import code.nebula.cipherquest.models.dto.Info
import code.nebula.cipherquest.models.dto.Message
import code.nebula.cipherquest.models.dto.Sender
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository
import java.time.OffsetDateTime
import java.util.UUID

@Repository
class VectorStoreRepository(
    val jdbcTemplate: JdbcTemplate,
) {
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
}
