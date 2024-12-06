package code.nebula.cipherquest.repository

import code.nebula.cipherquest.models.dto.Message
import code.nebula.cipherquest.models.dto.Sender
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository
import java.time.OffsetDateTime

@Repository
class VectorStoreRepository(
    val jdbcTemplate: JdbcTemplate,
) {
    fun existsDocumentWithSource(source: String?): Boolean {
        val sql = "SELECT EXISTS (SELECT 1 FROM vector_store WHERE metadata->>'source' = ?)"
        return jdbcTemplate.queryForObject(sql, Boolean::class.java, source)
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
                SELECT ROW_NUMBER() OVER (ORDER BY created_at) - 1 AS index,
                    content as message,
                    metadata->>'messageType' as sender,
                    created_at as timestamp
                FROM vector_store
                WHERE metadata->>'conversationId' = ? ORDER BY created_at
            """
        return jdbcTemplate.query(
            sql,
            { rs, _ ->
                Message(
                    index = rs.getInt("index"),
                    message = rs.getString("message"),
                    sender = Sender.valueOf(rs.getString("sender")),
                    timestamp = rs.getObject("timestamp", OffsetDateTime::class.java),
                )
            },
            userId,
        )
    }
}
