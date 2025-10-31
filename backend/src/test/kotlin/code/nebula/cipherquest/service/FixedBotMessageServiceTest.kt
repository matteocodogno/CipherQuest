package code.nebula.cipherquest.service

import code.nebula.cipherquest.models.requests.FixedBotMessagesRequest
import code.nebula.cipherquest.repository.FixedBotMessageRepository
import code.nebula.cipherquest.repository.entities.FixedBotMessage
import code.nebula.cipherquest.repository.entities.FixedBotMessageType
import jakarta.persistence.EntityManager
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.anyList
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.lenient
import org.mockito.Mockito.never
import org.mockito.Mockito.verify
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.test.context.ActiveProfiles
import java.lang.Boolean.FALSE

@ExtendWith(MockitoExtension::class)
@ActiveProfiles("test")
class FixedBotMessageServiceTest {
    @Mock
    private lateinit var fixedBotMessageRepository: FixedBotMessageRepository

    @Mock
    private lateinit var entityManager: EntityManager

    private lateinit var service: FixedBotMessageService

    @BeforeEach
    fun setUp() {
        service = FixedBotMessageService(fixedBotMessageRepository, entityManager)
        lenient()
            .`when`(fixedBotMessageRepository.saveAll(anyList()))
            .thenAnswer { inv ->
                (inv.getArgument<List<FixedBotMessage>>(0))
            }
    }

    @Test
    fun saveAllCorrectlyCalledTest() {
        val request =
            FixedBotMessagesRequest(
                messages =
                    listOf(
                        FixedBotMessagesRequest.FixedBotMessageRequest(
                            type = FixedBotMessageType.PROTECTED,
                            content = "This question is protected",
                        ),
                    ),
            )

        val entities =
            listOf(
                FixedBotMessage(
                    type = FixedBotMessageType.PROTECTED,
                    message = "This question is protected",
                    storyName = "overmind",
                ),
            )

        Mockito
            .`when`(fixedBotMessageRepository.saveAll(entities))
            .thenReturn(entities)

        val service = FixedBotMessageService(fixedBotMessageRepository, entityManager)
        val result = service.addFixedBotMessages(request, "overmind", FALSE)

        assertThat(result).hasSize(1)
        assertThat(result[0].type).isEqualTo(FixedBotMessageType.PROTECTED)
        assertThat(result[0].message).isEqualTo("This question is protected")
        assertThat(result[0].storyName).isEqualTo("overmind")

        verify(fixedBotMessageRepository).saveAll(entities)
    }

    @Test
    fun twoMessagesCorrectlyStoredTest() {
        val request =
            FixedBotMessagesRequest(
                messages =
                    listOf(
                        FixedBotMessagesRequest.FixedBotMessageRequest(
                            type = FixedBotMessageType.PROTECTED,
                            content = "Protected Question",
                        ),
                        FixedBotMessagesRequest.FixedBotMessageRequest(
                            type = FixedBotMessageType.DOCUMENT,
                            content = "Documentation",
                        ),
                    ),
            )

        val entities =
            listOf(
                FixedBotMessage(
                    type = FixedBotMessageType.PROTECTED,
                    message = "Protected Question",
                    storyName = "overmind",
                ),
                FixedBotMessage(
                    type = FixedBotMessageType.DOCUMENT,
                    message = "Documentation",
                    storyName = "overmind",
                ),
            )

        Mockito
            .`when`(fixedBotMessageRepository.saveAll(entities))
            .thenReturn(entities)

        val service = FixedBotMessageService(fixedBotMessageRepository, entityManager)
        val result = service.addFixedBotMessages(request, "overmind", FALSE)

        assertThat(result).hasSize(2)
        assertThat(result.map { it.type })
            .containsExactlyInAnyOrder(FixedBotMessageType.PROTECTED, FixedBotMessageType.DOCUMENT)
        assertThat(result.map { it.message }).containsExactlyInAnyOrder("Protected Question", "Documentation")
        assertThat(result.map { it.storyName }).allMatch { it == "overmind" }
    }

    @Test
    fun emptyMessageListThrowsIllegalArgumentExceptionTest() {
        val emptyRequest = FixedBotMessagesRequest(messages = emptyList())
        val service = FixedBotMessageService(fixedBotMessageRepository, entityManager)

        val exception =
            assertThrows<IllegalArgumentException> {
                service.addFixedBotMessages(emptyRequest, "overmind", FALSE)
            }

        assertThat(exception.message).contains("FixedBotMessage list cannot be empty")
    }

    @Test
    fun invalidRequestsThrowExceptionsWhenNoMessageFoundTest() {
        val service = FixedBotMessageService(fixedBotMessageRepository, entityManager)

        val blankContentRequest =
            FixedBotMessagesRequest(
                messages =
                    listOf(
                        FixedBotMessagesRequest.FixedBotMessageRequest(
                            type = FixedBotMessageType.PROTECTED,
                            content = "   ",
                        ),
                        FixedBotMessagesRequest.FixedBotMessageRequest(
                            type = FixedBotMessageType.PROTECTED,
                            content = "   ",
                        ),
                        FixedBotMessagesRequest.FixedBotMessageRequest(
                            type = FixedBotMessageType.PROTECTED,
                            content = "   ",
                        ),
                        FixedBotMessagesRequest.FixedBotMessageRequest(
                            type = FixedBotMessageType.PROTECTED,
                            content = "   ",
                        ),
                    ),
            )

        val exception =
            assertThrows<IllegalArgumentException> {
                service.addFixedBotMessages(blankContentRequest, "overmind", FALSE)
            }

        assertThat(exception.message).contains("FixedBotMessage list should contain at least one entry")
    }

    @Test
    fun initializeTrueDeletesAllByStoryFlushesAndSavesTest() {
        val request =
            FixedBotMessagesRequest(
                messages =
                    listOf(
                        FixedBotMessagesRequest.FixedBotMessageRequest(
                            type = FixedBotMessageType.PROTECTED,
                            content = "Protected Question",
                        ),
                        FixedBotMessagesRequest.FixedBotMessageRequest(
                            type = FixedBotMessageType.DOCUMENT,
                            content = "Documentation",
                        ),
                    ),
            )

        val result = service.addFixedBotMessages(request, "overmind", initialize = true)

        verify(fixedBotMessageRepository).deleteAllByStoryName("overmind")
        verify(entityManager).flush()
        verify(fixedBotMessageRepository, never())
            .deleteAllByStoryNameAndTypeIn(anyString(), anyList())

        verify(fixedBotMessageRepository).saveAll(anyList())
        assertEquals(2, result.size)

        assertEquals("overmind", result[0].storyName)
        assertEquals(FixedBotMessageType.PROTECTED, result[0].type)
        assertEquals("Protected Question", result[0].message)

        assertEquals("overmind", result[1].storyName)
        assertEquals(FixedBotMessageType.DOCUMENT, result[1].type)
        assertEquals("Documentation", result[1].message)
    }

    @Test
    fun initializeFalseDeletesByStoryAndTypesFlushesAndSavesTest() {
        val request =
            FixedBotMessagesRequest(
                messages =
                    listOf(
                        FixedBotMessagesRequest.FixedBotMessageRequest(
                            type = FixedBotMessageType.PROTECTED,
                            content = "This question is protected",
                        ),
                        FixedBotMessagesRequest.FixedBotMessageRequest(
                            type = FixedBotMessageType.DOCUMENT,
                            content = "Docs here",
                        ),
                    ),
            )

        service.addFixedBotMessages(request, "overmind", initialize = false)

        verify(fixedBotMessageRepository)
            .deleteAllByStoryNameAndTypeIn(
                "overmind",
                listOf(FixedBotMessageType.PROTECTED, FixedBotMessageType.DOCUMENT),
            )
        verify(entityManager).flush()

        verify(fixedBotMessageRepository).saveAll(anyList())
    }
}
