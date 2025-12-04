package code.nebula.cipherquest.service

import code.nebula.cipherquest.models.requests.FixedBotMessagesRequest
import code.nebula.cipherquest.repository.FixedBotMessageRepository
import code.nebula.cipherquest.repository.entities.FixedBotMessage
import code.nebula.cipherquest.repository.entities.FixedBotMessageType
import jakarta.persistence.EntityManager
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.tuple
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.anyList
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.Mockito.doReturn
import org.mockito.Mockito.lenient
import org.mockito.Mockito.never
import org.mockito.Mockito.verify
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.test.context.ActiveProfiles
import java.lang.Boolean.TRUE

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

        doReturn(entities).`when`(fixedBotMessageRepository).saveAll(entities)

        val service = FixedBotMessageService(fixedBotMessageRepository, entityManager)
        val result = service.addFixedBotMessages(request, "overmind")

        assertThat(result)
            .extracting("type", "message", "storyName")
            .containsExactly(
                tuple(
                    FixedBotMessageType.PROTECTED,
                    "This question is protected",
                    "overmind",
                ),
            )

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

        doReturn(entities).`when`(fixedBotMessageRepository).saveAll(entities)

        val service = FixedBotMessageService(fixedBotMessageRepository, entityManager)
        val result = service.addFixedBotMessages(request, "overmind")

        assertThat(result).extracting("type", "message", "storyName").containsExactlyInAnyOrder(
            tuple(FixedBotMessageType.PROTECTED, "Protected Question", "overmind"),
            tuple(FixedBotMessageType.DOCUMENT, "Documentation", "overmind"),
        )
    }

    @Test
    fun emptyMessageListThrowsIllegalArgumentExceptionTest() {
        val emptyRequest = FixedBotMessagesRequest(messages = emptyList())
        val service = FixedBotMessageService(fixedBotMessageRepository, entityManager)

        val exception =
            assertThrows<IllegalArgumentException> {
                service.addFixedBotMessages(emptyRequest, "overmind")
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
                service.addFixedBotMessages(blankContentRequest, "overmind")
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

        val result =
            service.addFixedBotMessages(request, "overmind", TRUE)

        verify(fixedBotMessageRepository).deleteAllByStoryName("overmind")
        verify(entityManager).flush()
        verify(fixedBotMessageRepository, never())
            .deleteAllByStoryNameAndTypeIn(anyString(), anyList())

        verify(fixedBotMessageRepository).saveAll(anyList())

        assertThat(result)
            .extracting("storyName", "type", "message")
            .containsExactly(
                tuple("overmind", FixedBotMessageType.PROTECTED, "Protected Question"),
                tuple("overmind", FixedBotMessageType.DOCUMENT, "Documentation"),
            )
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

        service.addFixedBotMessages(request, "overmind")

        verify(fixedBotMessageRepository)
            .deleteAllByStoryNameAndTypeIn(
                "overmind",
                listOf(FixedBotMessageType.PROTECTED, FixedBotMessageType.DOCUMENT),
            )
        verify(entityManager).flush()

        verify(fixedBotMessageRepository).saveAll(anyList())
    }
}
