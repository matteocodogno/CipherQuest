package code.nebula.cipherquest.controller

import code.nebula.cipherquest.repository.entities.Prize
import code.nebula.cipherquest.service.PrizeService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/prize")
class PrizeController(
    private val prizeService: PrizeService,
) {
    @GetMapping
    fun getPrizes(): List<Prize> = prizeService.findAllByCurrentDate()
}
