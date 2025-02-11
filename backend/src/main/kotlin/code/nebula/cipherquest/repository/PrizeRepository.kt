package code.nebula.cipherquest.repository

import code.nebula.cipherquest.repository.entities.Prize
import org.springframework.data.repository.ListCrudRepository
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
interface PrizeRepository : ListCrudRepository<Prize, String> {
    fun findAllByDateOrderByPositionAsc(date: LocalDate): List<Prize>
}
