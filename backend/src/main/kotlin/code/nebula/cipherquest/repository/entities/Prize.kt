package code.nebula.cipherquest.repository.entities

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import java.time.LocalDate
import java.util.UUID

@Entity
data class Prize(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    var id: UUID? = null,
    var name: String,
    var storyName: String,
    var position: Int,
    var date: LocalDate = LocalDate.now(),
)
