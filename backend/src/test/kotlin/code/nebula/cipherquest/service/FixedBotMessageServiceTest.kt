package code.nebula.cipherquest.service

import code.nebula.cipherquest.models.requests.FixedBotMessagesRequest
import code.nebula.cipherquest.repository.FixedBotMessageRepository
import code.nebula.cipherquest.repository.entities.FixedBotMessage
import code.nebula.cipherquest.repository.entities.FixedBotMessageType
import jakarta.persistence.EntityManager
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.test.context.ActiveProfiles
import java.lang.Boolean.FALSE

@ExtendWith(MockitoExtension::class)
@ActiveProfiles("test")
class FixedBotMessageServiceTest {
    @Mock
    lateinit var fixedBotMessageRepository: FixedBotMessageRepository
    private var entityManager: EntityManager = Mockito.mock(EntityManager::class.java)

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

        Mockito.verify(fixedBotMessageRepository).saveAll(entities)
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
}
