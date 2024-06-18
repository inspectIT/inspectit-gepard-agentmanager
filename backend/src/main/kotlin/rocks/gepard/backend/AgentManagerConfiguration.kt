package rocks.gepard.backend

import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.Ordered
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.web.filter.CorsFilter
import java.util.function.Consumer

@Configuration
class AgentManagerConfiguration {
    // Bootstrap some test data into the in-memory database
    @Bean
    fun init(repository: AgentRepository): ApplicationRunner {
        return ApplicationRunner { _: ApplicationArguments? ->
            arrayOf("Affe", "Häschen", "Schildkröte", "Hund", "Katze").forEach {
                val agent = Agent(it)
                repository.save(agent)
            }
            repository.findAll().forEach(/* action = */ Consumer { x: Agent? -> println(x) })
        }
    }

    // Fix the CORS errors
    @Bean
    fun simpleCorsFilter(): FilterRegistrationBean<*> {
        val source = UrlBasedCorsConfigurationSource()
        val config = CorsConfiguration()
        config.allowCredentials = true
        // *** URL below needs to match the Vue client URL and port ***
        config.allowedOrigins = listOf("http://localhost:3000")
        config.allowedMethods = listOf("*")
        config.allowedHeaders = listOf("*")
        source.registerCorsConfiguration("/**", config)
        val bean: FilterRegistrationBean<*> = FilterRegistrationBean(CorsFilter(source))
        bean.order = Ordered.HIGHEST_PRECEDENCE
        return bean
    }
}