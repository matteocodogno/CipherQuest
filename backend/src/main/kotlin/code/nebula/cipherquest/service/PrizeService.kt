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
        prizeRequest: PrizeRequest,
        storyName: String,
    ): List<Prize> {
        require(prizeRequest.prizes.isNotEmpty()) {
            "Prizes list cannot be empty"
        }

        val takenPositions =
            prizeRepository
                .findAllByStoryNameAndDateOrderByPositionAsc(storyName, prizeRequest.date)
                .map { it.position }

        return prizeRequest.prizes
            .filterNot {
                takenPositions.contains(it.position)
            }.map {
                Prize(
                    name = it.name,
                    position = it.position,
                    date = prizeRequest.date,
                    storyName = storyName,
                )
            }.let { prizeRepository.saveAll(it) }
    }
}
