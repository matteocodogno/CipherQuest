package code.nebula.cipherquest.repository.entities

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne

@Entity
data class LevelUpQuestion(
    @Id
    var id: String,
    var level: Int,
    var question: String,
    @ManyToOne
    @JoinColumn(name = "story_id", nullable = false)
    var story: Story,
)
