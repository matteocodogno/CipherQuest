package code.nebula.cipherquest.repository.entities

import jakarta.persistence.Entity
import jakarta.persistence.Id

@Entity
data class ProtectedQuestion(
    @Id
    var id: String,
    var question: String,
    var storyName: String,
)
