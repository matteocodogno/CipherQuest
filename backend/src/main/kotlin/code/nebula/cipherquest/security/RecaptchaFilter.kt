package code.nebula.cipherquest.security

import code.nebula.cipherquest.models.dto.RecaptchaResponse
import code.nebula.cipherquest.service.RecaptchaService
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.io.IOException

@Component
class RecaptchaFilter(
    private val recaptchaService: RecaptchaService,
) : OncePerRequestFilter() {
    companion object {
        private const val THRESHOLD_SCORE = 0.5
    }

    @Throws(ServletException::class, IOException::class)
    public override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        if (request.method.equals("POST", ignoreCase = true) &&
            request.requestURI.startsWith("/api/user/")
        ) {
            val recaptcha = request.getHeader("recaptcha")

            require(!recaptcha.isNullOrBlank()) { "Missing reCAPTCHA token" }

            val recaptchaResponse: RecaptchaResponse? = recaptchaService.validateToken(recaptcha)

            if (!recaptchaResponse?.success!!) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Invalid reCAPTCHA token")
                return
            }

            if (recaptchaResponse.score!! < THRESHOLD_SCORE) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied")
                return
            }
        }

        filterChain.doFilter(request, response)
    }
}
