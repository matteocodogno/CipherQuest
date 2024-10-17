package code.nebula.cipherquest.service

import code.nebula.cipherquest.models.dto.BotAnswer.Companion.DEFAULT_LEVEL
import code.nebula.cipherquest.models.requests.CreateUserLevelRequest
import code.nebula.cipherquest.repository.UserLevelRepository
import code.nebula.cipherquest.repository.entities.UserLevel
import jakarta.persistence.EntityNotFoundException
import org.springframework.stereotype.Service
import java.time.OffsetDateTime
import java.time.temporal.ChronoUnit
import kotlin.random.Random.Default.nextLong

@Service
class UserLevelService(
    private val userLevelRepository: UserLevelRepository,
) {
    companion object {
        private const val LEVEL_UP_COINS = 10
        private const val MIN_USER_ID = 1_000_000_000L
        private const val MAX_USER_ID = 9_999_999_999L
        private const val WIN_SCORE = 500
        private const val COINS_SCORE = 5
        private const val TIME_THRESHOLD = 30
        private const val LEVEL_SCORE = 250
    }

    fun calculateScore(user: UserLevel): UserLevel {
        val win = user.terminatedAt != null
        val minutes = ChronoUnit.MINUTES.between(user.createdAt, user.terminatedAt ?: user.updatedAt)

        val score =
            (user.coins.times(COINS_SCORE))
                .plus(if ((minutes - TIME_THRESHOLD) > 0) TIME_THRESHOLD - minutes else 0)
                .plus((user.level.minus(1)).times(LEVEL_SCORE))
                .plus(if (win) WIN_SCORE else 0)

        user.score = score
        return user
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
        userLevelRepository.findById(userId).orElseThrow {
            EntityNotFoundException("User with ID $userId not found")
        }

    fun decreaseCoins(userId: String): UserLevel =
        getLevelByUser(userId)
            .apply { coins -= 1 }
            .also(::calculateScore)
            .let(userLevelRepository::save)

    fun increaseLevelTo(
        userId: String,
        newLevel: Int,
    ): UserLevel =
        getLevelByUser(userId)
            .apply {
                level = newLevel
                coins += LEVEL_UP_COINS
            }.also(::calculateScore)
            .let(userLevelRepository::save)

    fun hasWon(userId: String): UserLevel =
        getLevelByUser(userId)
            .apply { terminatedAt = OffsetDateTime.now() }
            .also(::calculateScore)
            .let(userLevelRepository::save)
}
