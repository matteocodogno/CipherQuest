package code.nebula.cipherquest.service

import code.nebula.cipherquest.controller.request.ScoreboardEntry
import code.nebula.cipherquest.exceptions.UserAlreadyExistsException
import code.nebula.cipherquest.models.TimeFrameFilter
import code.nebula.cipherquest.models.dto.BotMessage
import code.nebula.cipherquest.models.requests.CreateUserLevelRequest
import code.nebula.cipherquest.repository.UserLevelRepository
import code.nebula.cipherquest.repository.entities.UserLevel
import jakarta.persistence.EntityNotFoundException
import org.apache.commons.lang3.RandomStringUtils
import org.springframework.stereotype.Service
import java.time.OffsetDateTime
import java.time.temporal.ChronoUnit
import kotlin.random.Random
import kotlin.random.Random.Default.nextLong

@Service
class UserLevelService(
    private val userLevelRepository: UserLevelRepository,
    private val mailService: MailService,
) {
    companion object {
        private const val LEVEL_UP_COINS = 10
        private const val MIN_USER_ID = 1_000_000_000L
        private const val MAX_USER_ID = 9_999_999_999L
        private const val WIN_SCORE = 500
        private const val COINS_SCORE = 5
        private const val TIME_THRESHOLD = 30
        private const val LEVEL_SCORE = 250
        private const val UNIQUE_CODE_SIZE = 8
        private const val LOWER_LIMIT_USERNAME_RANDOM_ID = 0
        private const val UPPER_LIMIT_USERNAME_RANDOM_ID = 100
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

    private fun setScoreCheated(user: UserLevel): UserLevel {
        user.score = -1
        return user
    }

    fun createUserLevel(
        request: CreateUserLevelRequest,
        storyName: String,
    ): UserLevel {
        userLevelRepository.findFirstByEmail(request.email)?.let {
            throw UserAlreadyExistsException("User '${request.email}' already exists.")
        }

        return saveUser(request, storyName)
    }

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

    fun hasCheated(userId: String): UserLevel =
        getLevelByUser(userId)
            .apply { terminatedAt = OffsetDateTime.now() }
            .also(::setScoreCheated)
            .let(userLevelRepository::save)

    fun calculateScoreboard(timeFrameFilter: TimeFrameFilter): List<ScoreboardEntry> {
        val cutoff = timeFrameFilter.startDate()

        return userLevelRepository
            .findByUpdatedAtAfterAndScoreGreaterThanOrderByScoreDesc(cutoff, 0)
            .asSequence()
            .mapIndexed { index, user ->
                ScoreboardEntry(
                    index = index,
                    username = user.username,
                    score = user.score.toInt(),
                    userId = user.userId,
                    time = ChronoUnit.MINUTES.between(user.createdAt, user.terminatedAt ?: user.updatedAt).toInt(),
                )
            }.toList()
    }

    fun saveUser(
        request: CreateUserLevelRequest,
        storyName: String,
    ): UserLevel {
        val baseUsername = request.email.substringBefore("@")

        val username =
            generateSequence(
                baseUsername,
            ) { "$baseUsername${Random.nextInt(LOWER_LIMIT_USERNAME_RANDOM_ID, UPPER_LIMIT_USERNAME_RANDOM_ID)}" }
                .first { !userLevelRepository.existsUserLevelByUsername(it) }

        val uniqueCode = RandomStringUtils.randomAlphanumeric(UNIQUE_CODE_SIZE).uppercase()

        return userLevelRepository
            .save(
                UserLevel(
                    userId = nextLong(MIN_USER_ID, MAX_USER_ID).toString(),
                    email = request.email,
                    username = username,
                    level = BotMessage.DEFAULT_LEVEL,
                    uniqueCode = uniqueCode,
                ),
            ).also { user ->
                mailService.sendUniqueCodeEmail(request.email, storyName, username, user.uniqueCode)
            }
    }
}
