package code.nebula.cipherquest.repository.entities

import jakarta.persistence.Entity
import jakarta.persistence.Id

@Entity
data class AdditionalDocument(
    @Id
    var id: String,
    var type: String,
    var source: String,
    var content: String,
    var level: Int,
    var storyName: String,
)
