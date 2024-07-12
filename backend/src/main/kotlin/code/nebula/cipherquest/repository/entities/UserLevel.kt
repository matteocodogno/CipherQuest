package code.nebula.cipherquest.repository.entities

import jakarta.persistence.Entity
import jakarta.persistence.Id
import java.time.OffsetDateTime

@Entity
data class UserLevel(
    @Id
    var userId: String,
    var level: Int,
    var username: String? = null,
    var coins: Int = 25,
    var terminatedAt: OffsetDateTime? = null,
)
