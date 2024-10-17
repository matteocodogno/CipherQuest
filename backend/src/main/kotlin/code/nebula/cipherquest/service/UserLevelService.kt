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
        private const val MIN_USER_ID = 1_000_000_000L
        private const val MAX_USER_ID = 9_999_999_999L
    }

    fun createUserLevel(request: CreateUserLevelRequest): UserLevel =
        userLevelRepository.save(
            UserLevel(
                nextLong(MIN_USER_ID, MAX_USER_ID).toString(),
                username = request.username,
                level = DEFAULT_LEVEL,
            ),
        )

    fun getLevelByUser(userId: String): UserLevel =
        userLevelRepository
            .findById(userId)
            .orElseThrow()

    fun decreaseCoins(userId: String): UserLevel = getLevelByUser(userId).apply {
            coins -= 1
        }.also(UserLevel::updateScore)
            .let(userLevelRepository::save)

    fun increaseLevelTo(
        userId: String,
        newLevel: Int,
    ): UserLevel =
        getLevelByUser(userId)
            .apply {
                level = newLevel
                coins += LEVEL_UP_COINS
            }.also(UserLevel::updateScore).let(userLevelRepository::save)

    fun hasWon(userId: String): UserLevel =
        getLevelByUser(userId)
            .apply {
                terminatedAt = OffsetDateTime.now()
            }.also(UserLevel::updateScore).let(userLevelRepository::save)
}
