package code.nebula.cipherquest.service

import code.nebula.cipherquest.models.requests.LevelUpQuestionRequest
import code.nebula.cipherquest.models.requests.ProtectedQuestionRequest
import code.nebula.cipherquest.repository.LevelUpQuestionRepository
import code.nebula.cipherquest.repository.ProtectedQuestionRepository
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.doThrow
import org.mockito.Mockito.verify
import org.mockito.junit.jupiter.MockitoExtension
import kotlin.test.Test

@ExtendWith(MockitoExtension::class)
class ActionableQuestionServiceTest {
    @Mock
    lateinit var levelUpQuestionRepository: LevelUpQuestionRepository

    @Mock
    lateinit var protectedQuestionRepository: ProtectedQuestionRepository

    @InjectMocks
    lateinit var service: ActionableQuestionService

    @Test
    fun addLevelUpQuestionSuccessTest() {
        val storyName = "overmind"
        val questions = listOf(LevelUpQuestionRequest(level = 1, content = "Q1"))

        service.addLevelUpQuestion(questions, storyName)

        verify(levelUpQuestionRepository).save(questions, storyName)
    }

    @Test
    fun addLevelUpQuestionThrowsWhenRepoFailsTest() {
        val storyName = "overmind"
        val questions = listOf(LevelUpQuestionRequest(level = 1, content = "Q1"))

        doThrow(IllegalStateException("DB down"))
            .`when`(levelUpQuestionRepository)
            .save(questions, storyName)

        assertThrows<IllegalStateException> {
            service.addLevelUpQuestion(questions, storyName)
        }
    }

    @Test
    fun addProtectedQuestionSuccessTest() {
        val storyName = "overmind"
        val questions = listOf(ProtectedQuestionRequest(content = "Secret"))

        service.addProtectedQuestion(questions, storyName)

        verify(protectedQuestionRepository).save(questions, storyName)
    }

    @Test
    fun addProtectedQuestionThrowsWhenRepoFailsTest() {
        val storyName = "overmind"
        val questions = listOf(ProtectedQuestionRequest(content = "Secret"))

        doThrow(IllegalArgumentException("Invalid data"))
            .`when`(protectedQuestionRepository)
            .save(questions, storyName)

        assertThrows<IllegalArgumentException> {
            service.addProtectedQuestion(questions, storyName)
        }
    }
}
