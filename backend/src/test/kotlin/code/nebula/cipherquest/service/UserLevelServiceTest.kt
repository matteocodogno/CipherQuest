package code.nebula.cipherquest.service

import code.nebula.cipherquest.repository.UserLevelRepository
import code.nebula.cipherquest.repository.entities.UserLevel
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import java.time.OffsetDateTime

class UserLevelServiceTest {
    private val userLevelRepository: UserLevelRepository = mock()
    private val userLevelService = UserLevelService(userLevelRepository)

    @Test
    fun `calculateScoreboard should return a list of ScoreboardEntry with correct values`() {
        val users =
            listOf(
                UserLevel(
                    username = "user3",
                    score = 500,
                    userId = "3",
                    createdAt = OffsetDateTime.now().minusMinutes(15),
                    terminatedAt = OffsetDateTime.now(),
                    updatedAt = OffsetDateTime.now(),
                    level = 3,
                ),
                UserLevel(
                    username = "user1",
                    score = 100,
                    userId = "1",
                    createdAt = OffsetDateTime.now().minusMinutes(30),
                    terminatedAt = OffsetDateTime.now(),
                    level = 1,
                ),
                UserLevel(
                    username = "user2",
                    score = 200,
                    userId = "2",
                    createdAt = OffsetDateTime.now().minusMinutes(45),
                    terminatedAt = null,
                    updatedAt = OffsetDateTime.now(),
                    level = 2,
                ),
            )
        `when`(userLevelRepository.findAll()).thenReturn(users)

        val result = userLevelService.calculateScoreboard()

        assertEquals(3, result.size)

        assertEquals("user3", result[0].username)
        assertEquals(500, result[0].score)
        assertEquals("3", result[0].userId)
        assertEquals(15, result[0].time)

        assertEquals("user2", result[1].username)
        assertEquals(200, result[1].score)
        assertEquals("2", result[1].userId)
        assertEquals(45, result[1].time)

        assertEquals("user1", result[2].username)
        assertEquals(100, result[2].score)
        assertEquals("1", result[2].userId)
        assertEquals(30, result[2].time)

        assertTrue(result.zipWithNext { a, b -> a.score >= b.score }.all { it })
    }
}
