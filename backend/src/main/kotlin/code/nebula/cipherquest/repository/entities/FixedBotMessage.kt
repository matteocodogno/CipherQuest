package code.nebula.cipherquest.repository.entities

import jakarta.persistence.Entity
import jakarta.persistence.Id

@Entity
data class FixedBotMessage(
    @Id
    var id: String,
    var type: FixedBotMessageType,
    var message: String,
    var storyName: String,
)
