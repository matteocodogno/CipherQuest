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
    // coins -> increase by each coin twice
    // time -> decrease for each minute after a threshold of 25 mins
    // level -> increase - 1: 0, 2: 30, 3: 70
    // win -> increase by 100
    fun updateScore() {

        val levelScore = when (level) {
            2 -> 30
            3 -> 70
            else -> 0
        }

        val coinsWeight = 2
        val timeThresholdInMinutes = 25
        val minutes = ChronoUnit.MINUTES.between(createdAt, Optional.ofNullable(terminatedAt).orElse(updatedAt))

        score = levelScore
            .plus(coins.times(coinsWeight))
            .minus(if(minutes > timeThresholdInMinutes) minutes.minus(timeThresholdInMinutes) else 0)
            .plus(Optional.ofNullable(terminatedAt).map { 100 }.orElse(0))
    }
}
