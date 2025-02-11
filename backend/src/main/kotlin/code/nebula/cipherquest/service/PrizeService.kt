package code.nebula.cipherquest.service

import code.nebula.cipherquest.repository.PrizeRepository
import code.nebula.cipherquest.repository.entities.Prize
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class PrizeService(
    private val prizeRepository: PrizeRepository,
) {
    fun findAllByCurrentDate(): List<Prize> = prizeRepository.findAllByDateOrderByPositionAsc(LocalDate.now())
}
