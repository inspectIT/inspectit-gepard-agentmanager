/* (C) 2024 */
package rocks.inspectit.gepard.agentmanager.application.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/** Web configuration for the application. */
@Configuration
public class CorsConfiguration implements WebMvcConfigurer {

  // TODO: Get CORS config from application.yaml

  /**
   * Adds CORS mappings to the registry. Allow all origins and methods.
   *
   * @param registry
   */
  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/**").allowedOrigins("*").allowedMethods("*");
  }
}
