package code.nebula.cipherquest.repository.entities

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.OffsetDateTime

@Entity
data class UserLevel(
    @Id
    var userId: String,
    var level: Int,
    var username: String,
    var email: String,
    var coins: Int = 25,
    @CreationTimestamp
    var createdAt: OffsetDateTime = OffsetDateTime.now(),
    @UpdateTimestamp
    var updatedAt: OffsetDateTime = OffsetDateTime.now(),
    var terminatedAt: OffsetDateTime? = null,
    var score: Long = 0,
    var uniqueCode: String = "",
    @ManyToOne
    @JoinColumn(name = "story_id", nullable = false)
    var story: Story,
)
