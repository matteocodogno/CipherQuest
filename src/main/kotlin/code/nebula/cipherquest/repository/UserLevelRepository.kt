package code.nebula.cipherquest.repository

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository

@Repository
class UserLevelRepository(
    val jdbcTemplate: JdbcTemplate,
) {
    fun createIfNotExist(userId: String): Int {
        val sql = "INSERT INTO user_level (user_id) VALUES (?) ON CONFLICT (user_id) DO NOTHING;"
        return jdbcTemplate.update(sql, userId)
    }

    fun getLevelByUser(userId: String): Int {
        val sql = "SELECT ul.level from user_level ul WHERE ul.user_id = ?"
        return jdbcTemplate.queryForObject(sql, Int::class.java, userId)
    }
}
