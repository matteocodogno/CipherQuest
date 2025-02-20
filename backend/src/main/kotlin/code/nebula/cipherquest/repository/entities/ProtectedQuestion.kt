package code.nebula.cipherquest.repository.entities

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne

@Entity
data class ProtectedQuestion(
    @Id
    var id: String,
    var question: String,
    var storyName: String,
    @ManyToOne
    @JoinColumn(name = "story_id", nullable = false)
    var story: Story,
)
