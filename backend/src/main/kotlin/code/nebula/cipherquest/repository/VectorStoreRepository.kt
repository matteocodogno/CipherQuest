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
    fun existsDocumentWithFileName(source: String?): Boolean {
        val sql = "SELECT EXISTS (SELECT 1 FROM vector_store WHERE metadata->>'file_name' = ?)"
        return jdbcTemplate.queryForObject(sql, Boolean::class.java, source)
    }

    fun getMessageHistoryByUserId(userId: String?): List<Message> {
        val sql =
            "SELECT content as message, metadata->>'messageType' as sender, created_at as timestamp" +
                " FROM vector_store " +
                "WHERE metadata->>'conversationId' = ? ORDER BY created_at"
        return jdbcTemplate.query(
            sql,
            { rs, _ ->
                Message(
                    message = rs.getString("message"),
                    sender = Sender.valueOf(rs.getString("sender")),
                    timestamp = rs.getObject("timestamp", OffsetDateTime::class.java),
                )
            },
            userId,
        )
    }
}
