package code.nebula.cipherquest.repository.entities

import jakarta.persistence.Entity
import java.util.UUID

@Entity
class ProtectedQuestion(
    override val id: UUID? = null,
    override val content: String,
    override val storyName: String,
) : Question(id, content, storyName)
