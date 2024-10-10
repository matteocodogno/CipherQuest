package code.nebula.cipherquest.service

import code.nebula.cipherquest.advisor.LevelUpAdvisor.Companion.LEVEL_UP_COINS
import code.nebula.cipherquest.models.dto.BotAnswer.Companion.DEFAULT_LEVEL
import code.nebula.cipherquest.repository.UserLevelRepository
import code.nebula.cipherquest.repository.entities.UserLevel
import org.springframework.stereotype.Service
import java.time.OffsetDateTime
import java.util.UUID

@Service
class UserLevelService(
    private val userLevelRepository: UserLevelRepository,
) {
    fun createUserLevel(username: String): UserLevel =
        userLevelRepository.save(UserLevel(UUID.randomUUID().toString(), username = username, level = DEFAULT_LEVEL))

    fun getLevelByUser(userId: String): UserLevel =
        userLevelRepository
            .findById(userId)
            .orElseThrow()

    fun decreaseCoins(userId: String): UserLevel =
        getLevelByUser(userId).apply { coins -= 1 }.let(userLevelRepository::save)

    fun increaseLevelTo(userId: String, newLevel: Int): UserLevel =
        getLevelByUser(userId).apply {
            level = newLevel
            coins += LEVEL_UP_COINS
        }.let(userLevelRepository::save)


    fun hasWon(userId: String): UserLevel =
        getLevelByUser(userId).apply {
            terminatedAt = OffsetDateTime.now()
        }.let(userLevelRepository::save)
}
