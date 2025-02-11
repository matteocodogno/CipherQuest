package code.nebula.cipherquest.repository.entities

import jakarta.persistence.Entity
import jakarta.persistence.Id
import java.time.LocalDate

@Entity
data class Prize(
    @Id
    var id: String,
    var name: String,
    var position: Int,
    var date: LocalDate = LocalDate.now(),
)
