package code.nebula.cipherquest.service

import code.nebula.cipherquest.models.requests.FixedBotMessageRequest
import code.nebula.cipherquest.repository.FixedBotMessageRepository
import code.nebula.cipherquest.repository.entities.FixedBotMessage
import code.nebula.cipherquest.repository.entities.FixedBotMessageType
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.test.context.ActiveProfiles

@ExtendWith(MockitoExtension::class)
@ActiveProfiles("test")
class FixedBotMessageServiceTest {
    @Mock
    lateinit var fixedBotMessageRepository: FixedBotMessageRepository

    @Test
    fun saveAllCorrectlyCalledTest() {
        val request =
            FixedBotMessageRequest(
                messages =
                    listOf(
                        code.nebula.cipherquest.models.requests.FixedBotMessage(
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

        val service = FixedBotMessageService(fixedBotMessageRepository)
        val result = service.addFixedBotMessages(request, "overmind")

        assertThat(result).hasSize(1)
        assertThat(result[0].type).isEqualTo(FixedBotMessageType.PROTECTED)
        assertThat(result[0].message).isEqualTo("This question is protected")
        assertThat(result[0].storyName).isEqualTo("overmind")

        Mockito.verify(fixedBotMessageRepository).saveAll(entities)
        Mockito.verifyNoMoreInteractions(fixedBotMessageRepository)
    }

    @Test
    fun twoMessagesCorrectlyStoredTest() {
        val request =
            FixedBotMessageRequest(
                messages =
                    listOf(
                        code.nebula.cipherquest.models.requests.FixedBotMessage(
                            type = FixedBotMessageType.PROTECTED,
                            content = "Protected Question",
                        ),
                        code.nebula.cipherquest.models.requests.FixedBotMessage(
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

        val service = FixedBotMessageService(fixedBotMessageRepository)
        val result = service.addFixedBotMessages(request, "overmind")

        assertThat(result).hasSize(2)
        assertThat(result.map { it.type })
            .containsExactlyInAnyOrder(FixedBotMessageType.PROTECTED, FixedBotMessageType.DOCUMENT)
        assertThat(result.map { it.message }).containsExactlyInAnyOrder("Protected Question", "Documentation")
        assertThat(result.map { it.storyName }).allMatch { it == "overmind" }
    }
}
