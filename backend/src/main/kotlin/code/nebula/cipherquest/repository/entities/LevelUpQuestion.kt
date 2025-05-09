package code.nebula.cipherquest.repository.entities

import jakarta.persistence.Entity
import java.util.UUID

@Entity
data class LevelUpQuestion(
    override val id: UUID? = null,
    override val content: String,
    override val storyName: String,
    val level: Int,
) : Question(id, content, storyName)
