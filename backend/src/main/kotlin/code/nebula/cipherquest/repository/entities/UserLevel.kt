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
) {
    // coins -> increase (by each coin times a weight)
    // time -> decrease (in minutes)
    // level -> increase - 1: 0, 2: 30, 3: 70
    // win -> increase by 100
    fun updateScore() {

        val levelWeight = when (level) {
            2 -> 30
            3 -> 70
            else -> 0
        }

        val coinsWeight = 10

        val minutes = ChronoUnit.MINUTES.between(createdAt, Optional.ofNullable(terminatedAt).orElse(updatedAt));

        val timeWeight = when  {
            minutes < 15 -> 15
            minutes in 15..29 -> 10
            minutes in 30..45 -> 5
            else -> 0
        }

        score = score
            .plus(level.times(levelWeight))
            .plus(coins.times(coinsWeight))
            .plus(minutes.times(timeWeight))
            .plus(Optional.ofNullable(terminatedAt).map { 100 }.orElse(0))
    }
}
