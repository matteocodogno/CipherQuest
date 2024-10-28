package code.nebula.cipherquest.service

import code.nebula.cipherquest.models.dto.BotAnswer.Companion.DEFAULT_LEVEL
import code.nebula.cipherquest.models.requests.CreateUserLevelRequest
import code.nebula.cipherquest.repository.UserLevelRepository
import code.nebula.cipherquest.repository.entities.UserLevel
import org.springframework.stereotype.Service
import java.time.OffsetDateTime
import kotlin.random.Random.Default.nextLong

@Service
class UserLevelService(
    private val userLevelRepository: UserLevelRepository,
) {
    companion object {
        private const val LEVEL_UP_COINS = 10
    }

    fun createUserLevel(request: CreateUserLevelRequest): UserLevel =
        userLevelRepository.save(
            UserLevel(
                nextLong(1_000_000_000, 9_999_999_999).toString(),
                username = request.username,
                level = DEFAULT_LEVEL,
            ),
        )

    fun getLevelByUser(userId: String): UserLevel =
        userLevelRepository
            .findById(userId)
            .orElseThrow()

    fun decreaseCoins(userId: String): UserLevel = getLevelByUser(userId).apply { coins -= 1 }.let(userLevelRepository::save)

    fun increaseLevelTo(
        userId: String,
        newLevel: Int,
    ): UserLevel =
        getLevelByUser(userId)
            .apply {
                level = newLevel
                coins += LEVEL_UP_COINS
            }.let(userLevelRepository::save)

    fun hasWon(userId: String): UserLevel =
        getLevelByUser(userId)
            .apply {
                terminatedAt = OffsetDateTime.now()
            }.let(userLevelRepository::save)
}
