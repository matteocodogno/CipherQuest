package code.nebula.cipherquest.repository.entities

import code.nebula.cipherquest.service.UserLevelService
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.utility.DockerImageName
import java.time.OffsetDateTime
import java.time.temporal.ChronoUnit
import java.util.stream.Collectors
import java.util.stream.Stream
import kotlin.test.Test
import kotlin.test.assertEquals

@Testcontainers
@SpringBootTest
@Suppress("LongParameterList", "DestructuringDeclarationWithTooManyEntries")
class UserLevelServiceTest {
    @Autowired
    private lateinit var userLevelService: UserLevelService

    companion object {
        @Container
        @JvmStatic
        @ServiceConnection
        var postgresContainer: PostgreSQLContainer<*> =
            PostgreSQLContainer(DockerImageName.parse("pgvector/pgvector:pg16"))

        @JvmStatic
        fun userLevelProvider(): Stream<Arguments> =
            Stream.of(
                Arguments.of(0, true, 3, 36, 20, 1180),
                Arguments.of(1, true, 3, 30, 27, 1150),
                Arguments.of(2, true, 3, 5, 40, 1015),
                Arguments.of(3, true, 3, 36, 200, 1010),
                Arguments.of(4, true, 3, 6, 60, 1000),
                Arguments.of(5, false, 3, 35, 30, 675),
                Arguments.of(6, false, 3, 15, 40, 565),
                Arguments.of(7, false, 3, 0, 20, 500),
                Arguments.of(8, false, 2, 34, 5, 420),
                Arguments.of(9, false, 2, 32, 5, 410),
                Arguments.of(10, false, 2, 10, 40, 290),
                Arguments.of(11, false, 1, 25, 0, 125),
                Arguments.of(12, false, 1, 24, 1, 120),
                Arguments.of(13, false, 1, 23, 2, 115),
            )
    }

    @ParameterizedTest
    @MethodSource("userLevelProvider")
    fun updateScore(
        id: Int,
        hasWon: Boolean,
        level: Int,
        coins: Int,
        time: Int,
        expectedScore: Long,
    ) {
        val userLevel = generateUser(id, hasWon, level, coins, time)
        val updatedUserLevel = userLevelService.calculateScore(userLevel)
        assertEquals(expectedScore, updatedUserLevel.score)
    }

    @Test
    fun testUsersSortedByScoreMatchesIdOrder() {
        val users =
            userLevelProvider()
                .map { args ->
                    val (id, hasWon, level, coins, time) = args.get()
                    generateUser(id as Int, hasWon as Boolean, level as Int, coins as Int, time as Int)
                }.collect(Collectors.toList())

        val sortedUsers =
            users
                .map { userLevelService.calculateScore(it) }
                .sortedByDescending { it.score }

        for (i in sortedUsers.indices) {
            assertEquals(i, sortedUsers[i].userId.toInt())
        }
    }

    private fun generateUser(
        id: Int,
        hasWon: Boolean,
        level: Int,
        coins: Int,
        time: Int,
    ): UserLevel {
        val createdAt = OffsetDateTime.now().minus(time.toLong(), ChronoUnit.MINUTES)
        return if (hasWon) {
            UserLevel(
                userId = id.toString(),
                level = level,
                username = id.toString(),
                coins = coins,
                createdAt = createdAt,
                terminatedAt = OffsetDateTime.now(),
            )
        } else {
            UserLevel(
                userId = id.toString(),
                level = level,
                username = id.toString(),
                coins = coins,
                createdAt = createdAt,
                updatedAt = OffsetDateTime.now(),
            )
        }
    }
}
