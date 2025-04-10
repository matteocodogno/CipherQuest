package code.nebula.cipherquest.service

import code.nebula.cipherquest.models.requests.PrizeRequest
import code.nebula.cipherquest.repository.PrizeRepository
import code.nebula.cipherquest.repository.entities.Prize
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@Service
class PrizeService(
    private val prizeRepository: PrizeRepository,
) {
    fun findAllByCurrentDate(storyName: String): List<Prize> =
        prizeRepository.findAllByStoryNameAndDateOrderByPositionAsc(storyName, LocalDate.now())

    @Transactional
    fun addPrizes(
        prizes: List<PrizeRequest>,
        storyName: String,
    ): List<Prize> =
        prizes
            .filter {
                prizeRepository.findByNameAndPositionAndDateAndStoryName(
                    it.name,
                    it.position,
                    it.date,
                    storyName,
                ) == null
            }.map {
                Prize(
                    name = it.name,
                    position = it.position,
                    date = it.date,
                    storyName = storyName,
                )
            }.let { prizeRepository.saveAll(it) }
}
