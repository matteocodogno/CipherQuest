package code.nebula.cipherquest.repository

import code.nebula.cipherquest.repository.entities.FixedBotMessage
import code.nebula.cipherquest.repository.entities.FixedBotMessageType
import org.springframework.data.repository.ListCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface FixedBotMessageRepository : ListCrudRepository<FixedBotMessage, String> {
    fun findByTypeAndStoryName(type: FixedBotMessageType, storyName: String): FixedBotMessage?
}
