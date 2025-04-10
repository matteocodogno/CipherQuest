package code.nebula.cipherquest.repository.entities

import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.MappedSuperclass
import java.util.UUID

@MappedSuperclass
abstract class Question(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    open val id: UUID? = null,
    open val content: String,
    open val storyName: String,
)
