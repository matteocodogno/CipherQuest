package code.nebula.cipherquest.service

import code.nebula.cipherquest.repository.PrizeRepository
import code.nebula.cipherquest.repository.entities.Prize
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class PrizeService(
    private val prizeRepository: PrizeRepository,
) {
    fun findAllByCurrentDate(storyName: String): List<Prize> =
        prizeRepository.findAllByStoryNameAndDateOrderByPositionAsc(storyName, LocalDate.now())
}
