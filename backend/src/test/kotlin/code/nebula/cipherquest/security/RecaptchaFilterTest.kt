package code.nebula.cipherquest.security

import code.nebula.cipherquest.models.dto.RecaptchaResponse
import code.nebula.cipherquest.service.RecaptchaService
import jakarta.servlet.http.HttpServletResponse
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.mock
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
import kotlin.test.assertTrue

@SpringJUnitConfig
class RecaptchaFilterTest {
    private lateinit var recaptchaService: RecaptchaService
    private lateinit var filter: RecaptchaFilter

    @BeforeEach
    fun setup() {
        recaptchaService = mock()
        filter = RecaptchaFilter(recaptchaService)
    }

    @Test
    fun failWithInvalidHeaderTest() {
        val request = MockHttpServletRequest("POST", "/api/user/signup")
        val response = MockHttpServletResponse()
        val chain = MockFilterChain()

        val exception =
            assertThrows<IllegalArgumentException> {
                filter.doFilterInternal(request, response, chain)
            }

        assertEquals("Missing reCAPTCHA token", exception.message)
    }

    @Test
    fun unsuccessfulTokenFails403Test() {
        val request = MockHttpServletRequest("POST", "/api/user/signup")
        val response = MockHttpServletResponse()
        val chain = MockFilterChain()

        request.addHeader("recaptcha", "invalid-token")
        `when`(recaptchaService.validateToken("invalid-token"))
            .thenReturn(
                RecaptchaResponse(
                    success = false,
                    score = 0.0,
                    challege_ts = null,
                    hostname = null,
                    action = null,
                ),
            )

        filter.doFilterInternal(request, response, chain)

        assertEquals(HttpServletResponse.SC_FORBIDDEN, response.status)
        assertTrue(response.errorMessage!!.contains("Invalid reCAPTCHA token"))
    }

    @Test
    fun tooLowScoreFails403Test() {
        val request = MockHttpServletRequest("POST", "/api/user/signup")
        val response = MockHttpServletResponse()
        val chain = MockFilterChain()

        request.addHeader("recaptcha", "low-score-token")
        `when`(recaptchaService.validateToken("low-score-token"))
            .thenReturn(
                RecaptchaResponse(
                    success = true,
                    score = 0.4,
                    challege_ts = null,
                    hostname = null,
                    action = null,
                ),
            )

        filter.doFilterInternal(request, response, chain)

        assertEquals(HttpServletResponse.SC_FORBIDDEN, response.status)
        assertTrue(response.errorMessage!!.contains("Access denied"))
    }

    @Test
    fun passWithValidTokenTest() {
        val request = MockHttpServletRequest("POST", "/api/user/signup")
        val response = MockHttpServletResponse()
        val chain = spy(MockFilterChain())

        request.addHeader("recaptcha", "valid-token")
        `when`(recaptchaService.validateToken("valid-token"))
            .thenReturn(
                RecaptchaResponse(
                    success = true,
                    score = 0.9,
                    challege_ts = null,
                    hostname = null,
                    action = null,
                ),
            )

        filter.doFilterInternal(request, response, chain)

        // Should not modify response
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
}
