package code.nebula.cipherquest.service.converter

import code.nebula.cipherquest.models.dto.RecaptchaResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.client.RestTemplate

@Service
class RecaptchaService(
    @Value("\${recaptcha.secret-key}") private val secretKey: String,
    @Value("\${recaptcha.verify-url}") private val verifyUrl: String,
) {
    private val restTemplate = RestTemplate()

    fun validateToken(recaptchaToken: String): RecaptchaResponse? {
        val headers =
            HttpHeaders().apply {
                contentType = MediaType.APPLICATION_FORM_URLENCODED
            }

        val body =
            LinkedMultiValueMap<String, String>().apply {
                add("secret", secretKey)
                add("response", recaptchaToken)
            }

        val requestEntity = HttpEntity(body, headers)

        val response =
            restTemplate.exchange(
                verifyUrl,
                HttpMethod.POST,
                requestEntity,
                RecaptchaResponse::class.java,
            )
        println(response.body)
        return response.body
    }
}
