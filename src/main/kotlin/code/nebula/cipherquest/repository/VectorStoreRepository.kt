package code.nebula.cipherquest.repository

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository

@Repository
class VectorStoreRepository(
    val jdbcTemplate: JdbcTemplate,
) {
    fun existsDocumentWithFileName(source: String?): Boolean {
        val sql = "SELECT EXISTS (SELECT 1 FROM vector_store WHERE metadata->>'source' = ?)"
        return jdbcTemplate.queryForObject(sql, Boolean::class.java, source)
    }
}
