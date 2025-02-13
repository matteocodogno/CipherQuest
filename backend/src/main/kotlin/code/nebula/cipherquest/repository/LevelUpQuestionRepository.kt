package code.nebula.cipherquest.repository

import code.nebula.cipherquest.repository.entities.LevelUpQuestion
import org.springframework.data.repository.ListCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface LevelUpQuestionRepository : ListCrudRepository<LevelUpQuestion, String> {
    fun findAllByStoryName(storyName: String): List<LevelUpQuestion>
}
