package code.nebula.cipherquest.repository

import code.nebula.cipherquest.repository.entities.FixedBotMessage
import code.nebula.cipherquest.repository.entities.FixedBotMessageType
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.repository.ListCrudRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
interface FixedBotMessageRepository : ListCrudRepository<FixedBotMessage, String> {
    fun findByTypeAndStoryName(
        type: FixedBotMessageType,
        storyName: String,
    ): FixedBotMessage?

    fun findByStoryName(storyName: String): List<FixedBotMessage>

    fun deleteAllByStoryName(storyName: String)

    @Modifying
    @Transactional
    fun deleteAllByStoryNameAndTypeIn(
        storyName: String,
        types: Collection<FixedBotMessageType>,
    ): Long
}
