package code.nebula.cipherquest.service

import code.nebula.cipherquest.configuration.properties.UniqueCodeMailProperties
import code.nebula.cipherquest.controller.request.ScoreboardEntry
import code.nebula.cipherquest.exceptions.UserAlreadyExistsException
import code.nebula.cipherquest.models.dto.BotMessage
import code.nebula.cipherquest.models.requests.CreateUserLevelRequest
import code.nebula.cipherquest.repository.UserLevelRepository
import code.nebula.cipherquest.repository.entities.UserLevel
import jakarta.persistence.EntityNotFoundException
import org.apache.commons.lang3.RandomStringUtils
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.Resource
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service
import org.stringtemplate.v4.ST
import java.time.OffsetDateTime
import java.time.temporal.ChronoUnit
import kotlin.random.Random.Default.nextLong

@Service
@Suppress("TooManyFunctions")
class UserLevelService(
    private val userLevelRepository: UserLevelRepository,
    private val emailSender: JavaMailSender,
    private val uniqueCodeMailProperties: UniqueCodeMailProperties,
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
    }

    @Value("classpath:/emails/unique-code.st")
    private lateinit var uniqueCodeEmail: Resource

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

    fun createUserLevel(request: CreateUserLevelRequest): UserLevel {
        userLevelRepository.findFirstByEmail(request.email)?.let {
            throw UserAlreadyExistsException("User '${request.email}' already exists.")
        }

        return saveUser(request)
    }

    fun getLevelByUser(userId: String): UserLevel =
        userLevelRepository.findById(userId).orElseThrow {
            EntityNotFoundException("User with ID $userId not found")
        }

    fun increaseQuestionCounter(userId: String) {
        getLevelByUser(userId)
            .let(userLevelRepository::save)
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

    fun calculateScoreboard(): List<ScoreboardEntry> =
        userLevelRepository
            .findAll()
            .filter { it.score > 0 }
            .sortedByDescending(UserLevel::score)
            .mapIndexed { index, userLevel ->
                ScoreboardEntry(
                    index = index,
                    username = userLevel.username,
                    score = userLevel.score.toInt(),
                    userId = userLevel.userId,
                    time =
                        ChronoUnit.MINUTES
                            .between(
                                userLevel.createdAt,
                                userLevel.terminatedAt
                                    ?: userLevel.updatedAt,
                            ).toInt(),
                )
            }

    fun saveUser(request: CreateUserLevelRequest): UserLevel {
        val user =
            userLevelRepository
                .save(
                    UserLevel(
                        userId = nextLong(MIN_USER_ID, MAX_USER_ID).toString(),
                        email = request.email,
                        username = request.email.substringBefore("@"),
                        level = BotMessage.DEFAULT_LEVEL,
                        uniqueCode = RandomStringUtils.randomAlphanumeric(UNIQUE_CODE_SIZE).uppercase(),
                    ),
                ).also { sendUniqueCodeEmail(it) }

        return user
    }

    private fun sendUniqueCodeEmail(user: UserLevel) {
        val templateString = uniqueCodeEmail.getContentAsString(Charsets.UTF_8)
        val template = ST(templateString)
        template.add("username", user.username)
        template.add("uniqueCode", user.uniqueCode)
        val result = template.render()

        SimpleMailMessage()
            .apply {
                from = uniqueCodeMailProperties.from
                setTo(user.email)
                subject = uniqueCodeMailProperties.subject
                text = result
            }.also { emailSender.send(it) }
    }
}
