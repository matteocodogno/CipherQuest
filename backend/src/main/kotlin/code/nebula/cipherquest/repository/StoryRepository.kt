package code.nebula.cipherquest.repository

import code.nebula.cipherquest.repository.entities.Story
import org.springframework.data.repository.ListCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface StoryRepository : ListCrudRepository<Story, String> {
    fun findFirstByName(name: String): Story?
}
