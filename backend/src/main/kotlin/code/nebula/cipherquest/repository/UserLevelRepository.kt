package code.nebula.cipherquest.repository

import code.nebula.cipherquest.repository.entities.UserLevel
import org.springframework.data.repository.ListCrudRepository
import org.springframework.stereotype.Repository
import java.time.OffsetDateTime

@Repository
interface UserLevelRepository : ListCrudRepository<UserLevel, String> {
    fun findFirstByEmail(email: String): UserLevel?

    fun existsUserLevelByUsername(username: String): Boolean

    fun findByUpdatedAtAfterAndScoreGreaterThanOrderByScoreDesc(
        updatedAtAfter: OffsetDateTime,
        scoreIsGreaterThan: Long,
    ): List<UserLevel>
}
