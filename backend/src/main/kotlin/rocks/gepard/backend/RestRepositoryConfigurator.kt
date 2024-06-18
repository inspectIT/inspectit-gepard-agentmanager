package rocks.gepard.backend

import org.springframework.data.rest.core.config.RepositoryRestConfiguration
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer
import org.springframework.stereotype.Component
import org.springframework.web.servlet.config.annotation.CorsRegistry

@Component
class RestRepositoryConfigurator : RepositoryRestConfigurer {
    override fun configureRepositoryRestConfiguration(config: RepositoryRestConfiguration?, cors: CorsRegistry?) {
        config?.exposeIdsFor(Agent::class.java)
    }
}