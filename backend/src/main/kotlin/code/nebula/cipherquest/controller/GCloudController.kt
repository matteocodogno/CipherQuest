package code.nebula.cipherquest.controller

import code.nebula.cipherquest.models.dto.GameDataFile
import code.nebula.cipherquest.service.GCloudService
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/gcloud")
class GCloudController(
    private val gCloudService: GCloudService,
) {
    @PostMapping("/loadContent/{storyName}")
    fun loadJSON(
        @PathVariable storyName: String,
    ): GameDataFile = gCloudService.loadContent(storyName)
}
