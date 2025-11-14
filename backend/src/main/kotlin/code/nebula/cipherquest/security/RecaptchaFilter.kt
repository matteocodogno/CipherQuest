package code.nebula.cipherquest.security

import code.nebula.cipherquest.exceptions.RecaptchaException
import code.nebula.cipherquest.models.ErrorType
import code.nebula.cipherquest.models.RecaptchaVersion
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
        private const val GOOGLE_RECAPTCHA_HEADER = "recaptcha"
    }

    public override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        if (!(
                request.method.equals("POST", true) &&
                    request.requestURI.startsWith("/api/user/")
            )
        ) {
            filterChain.doFilter(request, response)
            return
        }

        try {
            val token =
                request.getHeader(GOOGLE_RECAPTCHA_HEADER)
                    ?: throw RecaptchaException(
                        HttpServletResponse.SC_BAD_REQUEST,
                        ErrorType.RECAPTCHA_MISSING,
                        "Missing reCAPTCHA token",
                    )

            val versionHeader = request.getHeader("recaptcha-version")?.lowercase()
            val version = if (versionHeader == "v2") RecaptchaVersion.V2 else RecaptchaVersion.V3

            val recaptchaResponse = recaptchaService.validateToken(token, version)

            if (recaptchaResponse == null ||
                !recaptchaResponse.success
            ) {
                throw RecaptchaException(
                    HttpServletResponse.SC_FORBIDDEN,
                    ErrorType.RECAPTCHA_INVALID,
                    "Invalid reCAPTCHA token",
                )
            }

            if (version == RecaptchaVersion.V3) {
                validateV3Score(recaptchaResponse.score ?: PLACEHOLDER_SCORE)
            }

            filterChain.doFilter(request, response)
        } catch (ex: RecaptchaException) {
            sendJson(response, ex.status, ex.type, ex.message)
        }
        return
    }

    private fun sendJson(
        res: HttpServletResponse,
        status: Int,
        code: ErrorType,
        message: String,
    ) {
        res.status = status
        res.contentType = "application/json"
        res.characterEncoding = "UTF-8"
        val json = """{"error":"$code","message":"$message"}"""
        res.writer.write(json)
        res.writer.flush()
    }

    private fun validateV3Score(score: Double) {
        when {
            score < MID_THRESHOLD -> throw RecaptchaException(
                HttpServletResponse.SC_FORBIDDEN,
                ErrorType.RECAPTCHA_DENIED,
                "Access denied",
            )
            score < HIGH_THRESHOLD && score > MID_THRESHOLD -> throw RecaptchaException(
                PRECONDITION_REQUIRED,
                ErrorType.RECAPTCHA_V2_REQUIRED,
                "Please complete reCAPTCHA v2.",
            )
        }
    }
}
