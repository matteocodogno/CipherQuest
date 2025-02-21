package code.nebula.cipherquest.controller

import code.nebula.cipherquest.models.requests.CreateStoryRequest
import code.nebula.cipherquest.repository.entities.Story
import code.nebula.cipherquest.service.StoryService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/story")
class StoryController(
    private val storyService: StoryService,
) {
    @PostMapping
    fun save(
        @RequestBody story: CreateStoryRequest,
    ): Story = storyService.save(story)
}
