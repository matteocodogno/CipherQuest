package code.nebula.cipherquest.configuration.filters

import jakarta.servlet.Filter
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse

class StaticTokenFilter(
    private var token: String,
) : Filter {
    override fun doFilter(
        request: ServletRequest,
        response: ServletResponse,
        chain: FilterChain,
    ) {
        val httpRequest: HttpServletRequest = request as HttpServletRequest
        val httpResponse: HttpServletResponse = response as HttpServletResponse

        val customHeader: String = httpRequest.getHeader("X-Authorization").orEmpty()
        if (customHeader != token) {
            httpResponse.status = HttpServletResponse.SC_UNAUTHORIZED
            return
        }

        chain.doFilter(request, response)
    }
}
