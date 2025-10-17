package code.nebula.cipherquest.security

import code.nebula.cipherquest.models.dto.RecaptchaResponse
import code.nebula.cipherquest.service.RecaptchaService
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.io.IOException

@Component
class RecaptchaFilter(
    private val recaptchaService: RecaptchaService,
) : OncePerRequestFilter() {
    private val log = LoggerFactory.getLogger(RecaptchaFilter::class.java)

    @Throws(ServletException::class, IOException::class)
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        if (request.method.equals("POST", ignoreCase = true) &&
            request.requestURI.startsWith("/api/user/")
        ) {
            val recaptcha = request.getHeader("recaptcha")

            if (recaptcha.isNullOrBlank()) {
                log.info("Missing reCAPTCHA token")
                throw IllegalArgumentException("Missing reCAPTCHA token")
            }

            val recaptchaResponse: RecaptchaResponse? = recaptchaService.validateToken(recaptcha)

            if (!recaptchaResponse?.success!!) {
                log.info("Invalid reCAPTCHA token")
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Invalid reCAPTCHA token")
            }

            if (recaptchaResponse.score!! < 0.5) {
                log.info("Access Denied")
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied")
            }
        }

        filterChain.doFilter(request, response)
    }
}
