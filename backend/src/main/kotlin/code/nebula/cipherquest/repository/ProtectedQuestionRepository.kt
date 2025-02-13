package code.nebula.cipherquest.repository

import code.nebula.cipherquest.repository.entities.ProtectedQuestion
import org.springframework.data.repository.ListCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface ProtectedQuestionRepository : ListCrudRepository<ProtectedQuestion, String> {
    fun findAllByStoryName(storyName: String): List<ProtectedQuestion>
}
