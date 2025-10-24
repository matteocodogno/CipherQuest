package code.nebula.cipherquest.security

import code.nebula.cipherquest.models.dto.RecaptchaVersion
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
        private const val HIGH_THRESHOLD = 1
        private const val MID_THRESHOLD = 0.6
    }

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        if (request.method.equals("POST", ignoreCase = true) &&
            request.requestURI.startsWith("/api/user/")
        ) {
            val token = request.getHeader("recaptcha")
            if (token.isNullOrBlank()) {
                sendJson(response, HttpServletResponse.SC_BAD_REQUEST, "RECAPTCHA_MISSING", "Missing reCAPTCHA token")
                return
            }

            val versionHeader = request.getHeader("recaptcha-version")?.lowercase()
            val version = if (versionHeader == "v2") RecaptchaVersion.V2 else RecaptchaVersion.V3

            val recaptchaResponse = recaptchaService.validateToken(token, version)
            if (recaptchaResponse == null || !recaptchaResponse.success) {
                sendJson(response, HttpServletResponse.SC_FORBIDDEN, "RECAPTCHA_INVALID", "Invalid reCAPTCHA token")
                return
            }

            if (version == RecaptchaVersion.V3) {
                val score = recaptchaResponse.score ?: 0.0
                when {
                    score >= HIGH_THRESHOLD -> { /* allow */ }
                    score >= MID_THRESHOLD -> {
                        sendJson(response, 428, "RECAPTCHA_V2_REQUIRED", "Please complete reCAPTCHA v2.")
                        return
                    }
                    else -> {
                        sendJson(response, HttpServletResponse.SC_FORBIDDEN, "RECAPTCHA_DENIED", "Access denied")
                        return
                    }
                }
            } else {
                // v2 success → allow
            }
        }

        filterChain.doFilter(request, response)
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
}
