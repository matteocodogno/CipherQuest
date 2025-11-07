package code.nebula.cipherquest.service

import code.nebula.cipherquest.models.dto.RecaptchaResponse
import code.nebula.cipherquest.models.dto.RecaptchaVersion
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
    @param:Value("\${recaptcha.v3.secret-key:dummy-v3-secret}") private val v3SecretKey: String,
    @param:Value("\${recaptcha.v2.secret-key:dummy-v2-secret}") private val v2SecretKey: String,
    @param:Value("\${recaptcha.verify-url:https://www.google.com/recaptcha/api/siteverify}")
    private val verifyUrl: String,
) {
    private val restTemplate = RestTemplate()

    fun validateToken(
        recaptchaToken: String,
        version: RecaptchaVersion,
    ): RecaptchaResponse? {
        val headers = HttpHeaders().apply { contentType = MediaType.APPLICATION_FORM_URLENCODED }
        val body =
            LinkedMultiValueMap<String, String>().apply {
                add("secret", if (version == RecaptchaVersion.V3) v3SecretKey else v2SecretKey)
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
        return response.body
    }
}
