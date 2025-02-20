package code.nebula.cipherquest.repository.entities

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import java.time.LocalDate

@Entity
data class Prize(
    @Id
    var id: String,
    var name: String,
    var storyName: String,
    var position: Int,
    var date: LocalDate = LocalDate.now(),
    @ManyToOne
    @JoinColumn(name = "story_id", nullable = false)
    var story: Story,
)
