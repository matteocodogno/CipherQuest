package code.nebula.cipherquest.repository.entities

import jakarta.persistence.Entity
import jakarta.persistence.Id
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.OffsetDateTime
import java.time.temporal.ChronoUnit
import java.util.*

@Entity
data class UserLevel(
    @Id
    var userId: String,
    var level: Int,
    var username: String,
    var coins: Int = 25,
    @CreationTimestamp
    var createdAt: OffsetDateTime = OffsetDateTime.now(),
    @UpdateTimestamp
    var updatedAt: OffsetDateTime = OffsetDateTime.now(),
    var terminatedAt: OffsetDateTime? = null,
    var score: Long = 0,
    var questionCounter: Int = 0,
) {
    companion object {
        private const val WIN_SCORE = 500
        private const val COINS_SCORE = 5
        private const val TIME_THRESHOLD = 30
        private const val LEVEL_SCORE = 250
    }

    fun updateScore(): UserLevel {
        val win: Boolean = terminatedAt != null
        val minutes = ChronoUnit.MINUTES.between(createdAt, Optional.ofNullable(terminatedAt).orElse(updatedAt))

        score =
            (coins.times(COINS_SCORE))
                .plus(if ((minutes - TIME_THRESHOLD) > 0) TIME_THRESHOLD - minutes else 0)
                .plus((level.minus(1)).times(LEVEL_SCORE))
                .plus(if (win) WIN_SCORE else 0)

        return this
    }
}
