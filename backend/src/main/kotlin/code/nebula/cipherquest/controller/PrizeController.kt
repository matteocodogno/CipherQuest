package code.nebula.cipherquest.controller

import code.nebula.cipherquest.models.requests.PrizeRequest
import code.nebula.cipherquest.repository.entities.Prize
import code.nebula.cipherquest.service.PrizeService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/prize")
class PrizeController(
    private val prizeService: PrizeService,
) {
    @GetMapping("/{storyName}")
    fun getPrizes(
        @PathVariable storyName: String,
    ): List<Prize> = prizeService.findAllByCurrentDate(storyName)

    @PostMapping("/{storyName}")
    @ResponseStatus(HttpStatus.CREATED)
    fun addPrizes(
        @PathVariable storyName: String,
        @RequestBody @Valid prizes: PrizeRequest,
    ): List<Prize> = prizeService.addPrizes(prizes, storyName)
}
