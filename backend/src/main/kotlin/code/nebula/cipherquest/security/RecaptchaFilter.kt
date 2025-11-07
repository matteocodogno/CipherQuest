package code.nebula.cipherquest.security

import code.nebula.cipherquest.models.RecaptchaVersion
import code.nebula.cipherquest.models.dto.RecaptchaResponse
import code.nebula.cipherquest.service.RecaptchaService
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class RecaptchaFilter(
    private val recaptchaService: RecaptchaService,
) : OncePerRequestFilter() {
    companion object {
        private const val HIGH_THRESHOLD = 0.8
        private const val MID_THRESHOLD = 0.6
        private const val PLACEHOLDER_SCORE = 0.0
        private const val PRECONDITION_REQUIRED = 428
    }

    public override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        if (!(request.method.equals("POST", true) && request.requestURI.startsWith("/api/user/"))) {
            filterChain.doFilter(request, response)
            return
        }

        val token = request.getHeader("recaptcha")
        if (token.isNullOrBlank()) {
            sendJson(
                response,
                HttpServletResponse.SC_BAD_REQUEST,
                "RECAPTCHA_MISSING",
                "Missing reCAPTCHA token",
            )
            return
        }

        val versionHeader = request.getHeader("recaptcha-version")?.lowercase()
        val version = if (versionHeader == "v2") RecaptchaVersion.V2 else RecaptchaVersion.V3

        val recaptchaResponse = recaptchaService.validateToken(token, version)

        if (recaptchaFail(recaptchaResponse)) {
            sendJson(
                response,
                HttpServletResponse.SC_FORBIDDEN,
                "RECAPTCHA_INVALID",
                "Invalid reCAPTCHA token",
            )
            return
        }

        if (version == RecaptchaVersion.V3) {
            val score = recaptchaResponse?.score ?: PLACEHOLDER_SCORE

            if (score in MID_THRESHOLD..<HIGH_THRESHOLD) {
                sendJson(
                    response,
                    PRECONDITION_REQUIRED,
                    "RECAPTCHA_V2_REQUIRED",
                    "Please complete reCAPTCHA v2.",
                )
                return
            }

            if (score < MID_THRESHOLD) {
                sendJson(
                    response,
                    HttpServletResponse.SC_FORBIDDEN,
                    "RECAPTCHA_DENIED",
                    "Access denied",
                )
                return
            }
        }

        filterChain.doFilter(request, response)

        return
    }

    private fun sendJson(
        res: HttpServletResponse,
        status: Int,
        code: String,
        message: String,
    ) {
        res.status = status
        res.contentType = "application/json"
        res.characterEncoding = "UTF-8"
        val json = """{"error":"$code","message":"$message"}"""
        res.writer.use { it.write(json) }
    }

    private fun recaptchaFail(recaptchaResponse: RecaptchaResponse?) =
        recaptchaResponse == null ||
            !recaptchaResponse.success
}
