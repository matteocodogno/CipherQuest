package code.nebula.cipherquest.repository.entities

import jakarta.persistence.Entity
import jakarta.persistence.Id

@Entity
data class LevelUpQuestion(
    @Id
    var id: String,
    var level: Int,
    var question: String,
)
