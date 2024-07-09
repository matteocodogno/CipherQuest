package code.nebula.cipherquest.repository.entities

import jakarta.persistence.Entity
import jakarta.persistence.Id

@Entity
data class UserLevel(
    @Id
    var userId: String,
    var level: Int,
)
