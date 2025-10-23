package code.nebula.cipherquest.service

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.web.client.MockRestServiceServer
import org.springframework.test.web.client.match.MockRestRequestMatchers.content
import org.springframework.test.web.client.match.MockRestRequestMatchers.method
import org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo
import org.springframework.test.web.client.response.MockRestResponseCreators.withStatus
import org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess
import org.springframework.web.client.RestTemplate

class RecaptchaServiceTest {
    private lateinit var restTemplate: RestTemplate
    private lateinit var mockServer: MockRestServiceServer
    private lateinit var recaptchaService: RecaptchaService

    private val secretKey = "test-secret-key"
    private val verifyUrl = "https://www.google.com/recaptcha/api/siteverify"

    @BeforeEach
    fun setUp() {
        restTemplate = RestTemplate()
        mockServer = MockRestServiceServer.createServer(restTemplate)
        recaptchaService =
            RecaptchaService(secretKey, verifyUrl).apply {
                val field = this::class.java.getDeclaredField("restTemplate")
                field.isAccessible = true
                field.set(this, restTemplate)
            }
    }

    @Test
    fun googleVerificationTest() {
        val responseJson = """{"success": true, "score": 0.9, "action": "login"}"""

        mockServer
            .expect(requestTo(verifyUrl))
            .andExpect(method(HttpMethod.POST))
            .andExpect(content().string(org.hamcrest.Matchers.containsString("secret=$secretKey")))
            .andExpect(content().string(org.hamcrest.Matchers.containsString("response=valid-token")))
            .andRespond(withSuccess(responseJson, MediaType.APPLICATION_JSON))

        val response = recaptchaService.validateToken("valid-token")

        assertNotNull(response)
        assertTrue(response!!.success)
        assertEquals(0.9, response.score)
        mockServer.verify()
    }

    @Test
    fun failWhenTokenDenied() {
        val responseJson = """{"success": false, "score": 0.1}"""

        mockServer
            .expect(requestTo(verifyUrl))
            .andExpect(method(HttpMethod.POST))
            .andRespond(withSuccess(responseJson, MediaType.APPLICATION_JSON))

        val response = recaptchaService.validateToken("invalid-token")

        assertNotNull(response)
        assertFalse(response!!.success)
        assertEquals(0.1, response.score)
    }

    @Test
    fun failWithMalformedToken() {
        val invalidJson = """{"success": tru"""

        mockServer
            .expect(requestTo(verifyUrl))
            .andRespond(withSuccess(invalidJson, MediaType.APPLICATION_JSON))

        assertThrows(Exception::class.java) {
            recaptchaService.validateToken("token")
        }
    }

    @Test
    fun serverErrorsHandlingTest() {
        mockServer
            .expect(requestTo(verifyUrl))
            .andRespond(withStatus(HttpStatus.INTERNAL_SERVER_ERROR))

        val ex =
            assertThrows(Exception::class.java) {
                recaptchaService.validateToken("token")
            }
        assertTrue(ex.message!!.contains("500"))
    }
}
