package code.nebula.cipherquest.service

import code.nebula.cipherquest.models.dto.RecaptchaVersion
import org.hamcrest.Matchers.containsString
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.test.web.client.MockRestServiceServer
import org.springframework.test.web.client.match.MockRestRequestMatchers.content
import org.springframework.test.web.client.match.MockRestRequestMatchers.header
import org.springframework.test.web.client.match.MockRestRequestMatchers.method
import org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo
import org.springframework.test.web.client.response.MockRestResponseCreators.withBadRequest
import org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestTemplate

class RecaptchaServiceTest {
    private lateinit var service: RecaptchaService
    private lateinit var restTemplate: RestTemplate
    private lateinit var server: MockRestServiceServer

    private val v3Secret = "v3-test-secret"
    private val v2Secret = "v2-test-secret"
    private val verifyUrl = "https://verify.local/recaptcha"

    @BeforeEach
    fun setUp() {
        service = RecaptchaService(v3Secret, v2Secret, verifyUrl)

        restTemplate = RestTemplate()
        val field = RecaptchaService::class.java.getDeclaredField("restTemplate")
        field.isAccessible = true
        field.set(service, restTemplate)

        server = MockRestServiceServer.bindTo(restTemplate).build()
    }

    @Test
    fun v3SuccessTest() {
        val token = "tkn-v3"
        server
            .expect(requestTo(verifyUrl))
            .andExpect(method(HttpMethod.POST))
            .andExpect(header(HttpHeaders.CONTENT_TYPE, containsString(MediaType.APPLICATION_FORM_URLENCODED_VALUE)))
            .andExpect(content().string(containsString("secret=$v3Secret")))
            .andExpect(content().string(containsString("response=$token")))
            .andRespond(
                withSuccess(
                    """{"success":true,"score":0.91,"action":"login"}""",
                    MediaType.APPLICATION_JSON,
                ),
            )

        val res = service.validateToken(token, RecaptchaVersion.V3)

        server.verify()
        assertNotNull(res)
        assertTrue(res!!.success)
        assertEquals(0.91, res.score)
    }

    @Test
    fun v2SuccessTest() {
        val token = "tkn-v2"
        server
            .expect(requestTo(verifyUrl))
            .andExpect(method(HttpMethod.POST))
            .andExpect(header(HttpHeaders.CONTENT_TYPE, containsString(MediaType.APPLICATION_FORM_URLENCODED_VALUE)))
            .andExpect(content().string(containsString("secret=$v2Secret")))
            .andExpect(content().string(containsString("response=$token")))
            .andRespond(
                withSuccess(
                    """{"success":true}""",
                    MediaType.APPLICATION_JSON,
                ),
            )

        val res = service.validateToken(token, RecaptchaVersion.V2)

        server.verify()
        assertNotNull(res)
        assertTrue(res!!.success)
        assertNull(res.score)
    }

    @Test
    fun invalidTokenSuccessIsFalseTest() {
        val token = "bad-token"
        server
            .expect(requestTo(verifyUrl))
            .andExpect(method(HttpMethod.POST))
            .andRespond(
                withSuccess(
                    """{"success":false,"error-codes":["invalid-input-response"]}""",
                    MediaType.APPLICATION_JSON,
                ),
            )

        val res = service.validateToken(token, RecaptchaVersion.V3)

        server.verify()
        assertNotNull(res)
        assertFalse(res!!.success)
    }

    @Test
    fun badRequestTest() {
        val token = "tkn"
        server
            .expect(requestTo(verifyUrl))
            .andExpect(method(HttpMethod.POST))
            .andRespond(withBadRequest())

        assertThrows(HttpClientErrorException.BadRequest::class.java) {
            service.validateToken(token, RecaptchaVersion.V3)
        }

        server.verify()
    }
}
