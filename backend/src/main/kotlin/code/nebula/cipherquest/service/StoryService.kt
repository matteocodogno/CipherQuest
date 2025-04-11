package code.nebula.cipherquest.service

import code.nebula.cipherquest.models.requests.CreateStoryRequest
import code.nebula.cipherquest.repository.StoryRepository
import code.nebula.cipherquest.repository.entities.Story
import org.springframework.stereotype.Service

@Service
class StoryService(
    private val storyRepository: StoryRepository,
) {
    fun findFirstByName(name: String): Story? = storyRepository.findFirstByName(name)

    fun save(story: CreateStoryRequest): Story = storyRepository.save(Story(name = story.name))
}
