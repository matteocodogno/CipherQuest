package code.nebula.cipherquest.service

import code.nebula.cipherquest.models.RecaptchaVersion
import code.nebula.cipherquest.models.dto.RecaptchaResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.client.RestClient

@Service
class RecaptchaService(
    @param:Value("\${recaptcha.v3.secret-key:dummy-v3-secret}") private val v3SecretKey: String,
    @param:Value("\${recaptcha.v2.secret-key:dummy-v2-secret}") private val v2SecretKey: String,
    @param:Value("\${recaptcha.verify-url:https://www.google.com/recaptcha/api/siteverify}")
    private val verifyUrl: String,
    restClientBuilder: RestClient.Builder,
) {
    private val restClient = restClientBuilder.build()

    fun validateToken(
        recaptchaToken: String,
        version: RecaptchaVersion,
    ): RecaptchaResponse? {
        val body =
            LinkedMultiValueMap<String, String>().apply {
                add("secret", if (version == RecaptchaVersion.V3) v3SecretKey else v2SecretKey)
                add("response", recaptchaToken)
            }
        return restClient
            .post()
            .uri(verifyUrl)
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(body)
            .retrieve()
            .body(RecaptchaResponse::class.java)
    }
}
