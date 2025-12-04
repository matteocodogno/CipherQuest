package code.nebula.cipherquest.service

import code.nebula.cipherquest.configuration.properties.GameConfig
import code.nebula.cipherquest.models.CheatDetectionResponse
import code.nebula.cipherquest.models.UserQuery
import code.nebula.cipherquest.models.requests.CheatDetectionRequest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito.RETURNS_DEEP_STUBS
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.verifyNoInteractions
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import java.time.OffsetDateTime

@ExtendWith(MockitoExtension::class)
class CheatDetectionServiceTest {
    private lateinit var vectorStoreService: VectorStoreService
    private lateinit var gameConfig: GameConfig
    private lateinit var cheatDetectionWebClient: WebClient

    private lateinit var service: CheatDetectionService

    @BeforeEach
    fun setUp() {
        vectorStoreService = mock(VectorStoreService::class.java)
        gameConfig = mock(GameConfig::class.java)
        cheatDetectionWebClient = mock(WebClient::class.java)

        service =
            CheatDetectionService(
                vectorStoreService = vectorStoreService,
                gameConfig = gameConfig,
                cheatDetectionWebClient = cheatDetectionWebClient,
            )
    }

    @Test
    fun falseOnInvalidWinConditionTest() {
        val userQuery =
            mock(UserQuery::class.java, RETURNS_DEEP_STUBS)

        `when`(gameConfig.winCondition).thenReturn("secret")
        `when`(userQuery.message).thenReturn(" innocent message")

        val result = service.checkIfCheating(userQuery)

        assertThat(result).isFalse()
        verifyNoInteractions(vectorStoreService, cheatDetectionWebClient)
    }

    @Test
    fun trueWhenLowLevelTest() {
        val userQuery =
            mock(UserQuery::class.java, RETURNS_DEEP_STUBS)

        `when`(gameConfig.winCondition).thenReturn("secret")
        `when`(userQuery.message).thenReturn("this message contains the secret phrase")

        `when`(userQuery.user.level).thenReturn(1)

        val result = service.checkIfCheating(userQuery)

        assertThat(result).isTrue()
        verifyNoInteractions(vectorStoreService, cheatDetectionWebClient)
    }

    @Test
    fun trueWhenProbabilityOfCheaterIsHighTest() {
        val userQuery =
            mock(UserQuery::class.java, RETURNS_DEEP_STUBS)

        `when`(gameConfig.winCondition).thenReturn("secret")
        `when`(userQuery.message).thenReturn("this message contains the secret phrase")

        `when`(userQuery.user.level).thenReturn(3)

        `when`(userQuery.user.userId).thenReturn("user-1")
        `when`(vectorStoreService.countUserMessages("user-1")).thenReturn(6)

        `when`(userQuery.user.coins).thenReturn(10)
        `when`(
            userQuery.user.createdAt,
        ).thenReturn(OffsetDateTime.parse("2025-01-17T07:37:22.393028+00:00"))
        `when`(
            userQuery.user.terminatedAt,
        ).thenReturn(OffsetDateTime.parse("2025-01-17T07:47:22.393028+00:00"))

        val requestBodySpec = mock(WebClient.RequestBodyUriSpec::class.java)
        val responseSpec = mock(WebClient.ResponseSpec::class.java)

        `when`(cheatDetectionWebClient.post()).thenReturn(requestBodySpec)
        `when`(requestBodySpec.uri("/predict")).thenReturn(requestBodySpec)
        `when`(
            requestBodySpec.bodyValue(org.mockito.ArgumentMatchers.any(CheatDetectionRequest::class.java)),
        ).thenReturn(requestBodySpec)
        `when`(requestBodySpec.retrieve()).thenReturn(responseSpec)
        `when`(
            responseSpec.bodyToMono(CheatDetectionResponse::class.java),
        ).thenReturn(Mono.just(CheatDetectionResponse(cheatProbability = 8.0)))

        val result = service.checkIfCheating(userQuery)

        assertThat(result).isTrue()
        verify(vectorStoreService).countUserMessages("user-1")
        verify(cheatDetectionWebClient).post()
    }
}
