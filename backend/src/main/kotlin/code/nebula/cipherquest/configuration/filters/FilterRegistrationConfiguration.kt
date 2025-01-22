package code.nebula.cipherquest.configuration.filters

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class FilterRegistrationConfiguration(
    @Value("\${overmind.security.token}")
    private var token: String,
) {
    @Bean
    fun authFilter(): FilterRegistrationBean<StaticTokenFilter> {
        val registrationBean: FilterRegistrationBean<StaticTokenFilter> =
            FilterRegistrationBean()

        registrationBean.setFilter(StaticTokenFilter(token))
        registrationBean.addUrlPatterns("/private/*")
        registrationBean.order = 1

        return registrationBean
    }
}
