package code.nebula.cipherquest.security

import code.nebula.cipherquest.models.RecaptchaVersion
import code.nebula.cipherquest.models.dto.RecaptchaResponse
import code.nebula.cipherquest.service.RecaptchaService
import jakarta.servlet.http.HttpServletResponse
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.Mock
import org.mockito.Mockito.spy
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.Mockito.verifyNoInteractions
import org.mockito.Mockito.`when`
import org.springframework.mock.web.MockFilterChain
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.mock.web.MockHttpServletResponse
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig
import kotlin.test.assertEquals

@SpringJUnitConfig
class RecaptchaFilterTest {
    @Mock
    private lateinit var recaptchaService: RecaptchaService
    private lateinit var filter: RecaptchaFilter

    @BeforeEach
    fun setup() {
        filter = RecaptchaFilter(recaptchaService)
    }

    @Test
    fun unsuccessfulV3TokenFails403Test() {
        val request = MockHttpServletRequest("POST", "/api/user/signup")
        val response = MockHttpServletResponse()
        val chain = MockFilterChain()

        request.addHeader("recaptcha", "invalid-token")
        `when`(
            recaptchaService.validateToken(
                "invalid-token",
                version = RecaptchaVersion.V3,
            ),
        ).thenReturn(
            RecaptchaResponse(
                success = false,
                score = 0.0,
                challengeTs = null,
                hostname = null,
                action = null,
            ),
        )

        filter.doFilterInternal(request, response, chain)

        assertEquals(HttpServletResponse.SC_FORBIDDEN, response.status)
    }

    @Test
    fun tooLowScoreV3Fails403Test() {
        val request = MockHttpServletRequest("POST", "/api/user/signup")
        val response = MockHttpServletResponse()
        val chain = MockFilterChain()

        request.addHeader("recaptcha", "low-score-token")
        `when`(
            recaptchaService.validateToken(
                "low-score-token",
                version = RecaptchaVersion.V3,
            ),
        ).thenReturn(
            RecaptchaResponse(
                success = true,
                score = 0.4,
                challengeTs = null,
                hostname = null,
                action = null,
            ),
        )

        filter.doFilterInternal(request, response, chain)

        assertEquals(HttpServletResponse.SC_FORBIDDEN, response.status)
    }

    @Test
    fun passWithValidV3TokenTest() {
        val request = MockHttpServletRequest("POST", "/api/user/signup")
        val response = MockHttpServletResponse()
        val chain = spy(MockFilterChain())

        request.addHeader("recaptcha", "valid-token")
        `when`(
            recaptchaService.validateToken(
                "valid-token",
                version = RecaptchaVersion.V3,
            ),
        ).thenReturn(
            RecaptchaResponse(
                success = true,
                score = 0.9,
                challengeTs = null,
                hostname = null,
                action = null,
            ),
        )

        filter.doFilterInternal(request, response, chain)

        assertEquals(200, response.status)
        verify(chain, times(1)).doFilter(any(), any())
    }

    @Test
    fun ignoreNonPostRequestsTest() {
        val request = MockHttpServletRequest("GET", "/api/user/info")
        val response = MockHttpServletResponse()
        val chain = spy(MockFilterChain())

        filter.doFilterInternal(request, response, chain)

        verify(chain, times(1)).doFilter(any(), any())
        assertEquals(200, response.status)
        verifyNoInteractions(recaptchaService)
    }

    @Test
    fun ignoreOtherPostsTest() {
        val request = MockHttpServletRequest("POST", "/api/materials")
        val response = MockHttpServletResponse()
        val chain = spy(MockFilterChain())

        filter.doFilterInternal(request, response, chain)

        verify(chain, times(1)).doFilter(any(), any())
        verifyNoInteractions(recaptchaService)
    }

    @Test
    fun v2SuccessPassesThroughTest() {
        val request = MockHttpServletRequest("POST", "/api/user/signup")
        val response = MockHttpServletResponse()
        val chain = spy(MockFilterChain())

        request.addHeader("recaptcha", "v2-good-token")
        request.addHeader("recaptcha-version", "v2")

        `when`(
            recaptchaService.validateToken(
                "v2-good-token",
                version = RecaptchaVersion.V2,
            ),
        ).thenReturn(
            RecaptchaResponse(
                success = true,
                score = null,
                challengeTs = null,
                hostname = null,
                action = null,
            ),
        )

        filter.doFilterInternal(request, response, chain)

        assertEquals(200, response.status)
        verify(chain, times(1)).doFilter(any(), any())
    }

    @Test
    fun v2InvalidTokenFails403Test() {
        val request = MockHttpServletRequest("POST", "/api/user/signup")
        val response = MockHttpServletResponse()
        val chain = spy(MockFilterChain())

        request.addHeader("recaptcha", "v2-bad-token")
        request.addHeader("recaptcha-version", "v2")

        `when`(
            recaptchaService.validateToken(
                "v2-bad-token",
                version = RecaptchaVersion.V2,
            ),
        ).thenReturn(
            RecaptchaResponse(
                success = false,
                score = null,
                challengeTs = null,
                hostname = null,
                action = null,
            ),
        )

        filter.doFilterInternal(request, response, chain)

        assertEquals(HttpServletResponse.SC_FORBIDDEN, response.status)
        verify(chain, times(0)).doFilter(any(), any())
    }

    @Test
    fun v2SuccessIgnoresScoreAndPassesTest() {
        val request = MockHttpServletRequest("POST", "/api/user/signup")
        val response = MockHttpServletResponse()
        val chain = spy(MockFilterChain())

        request.addHeader("recaptcha", "v2-token-with-score")
        request.addHeader("recaptcha-version", "v2")

        `when`(
            recaptchaService.validateToken(
                "v2-token-with-score",
                version = RecaptchaVersion.V2,
            ),
        ).thenReturn(
            RecaptchaResponse(
                success = true,
                score = 0.1,
                challengeTs = null,
                hostname = null,
                action = null,
            ),
        )

        filter.doFilterInternal(request, response, chain)

        assertEquals(200, response.status)
        verify(chain, times(1)).doFilter(any(), any())
    }

    @Test
    fun v2HeaderCaseInsensitiveTest() {
        val request = MockHttpServletRequest("POST", "/api/user/signup")
        val response = MockHttpServletResponse()
        val chain = spy(MockFilterChain())

        request.addHeader("recaptcha", "v2-token-upcase")
        request.addHeader("recaptcha-version", "V2")

        `when`(
            recaptchaService.validateToken(
                "v2-token-upcase",
                version = RecaptchaVersion.V2,
            ),
        ).thenReturn(
            RecaptchaResponse(
                success = true,
                score = null,
                challengeTs = null,
                hostname = null,
                action = null,
            ),
        )

        filter.doFilterInternal(request, response, chain)

        assertEquals(200, response.status)
        verify(chain, times(1)).doFilter(any(), any())
    }
}
