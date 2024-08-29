/* (C) 2024 */
package rocks.inspectit.gepard.agentmanager.application.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/** Web configuration for the application. */
@Configuration
public class CorsConfiguration implements WebMvcConfigurer {

  @Value("${inspectit-config-server.security.cors.path-pattern}")
  private String pathPattern;

  @Value("${inspectit-config-server.security.cors.allowed-origins}")
  private String allowedOrigins;

  @Value("${inspectit-config-server.security.cors.allowed-methods}")
  private String allowedMethods;

  /**
   * Adds CORS mappings to the registry. Allow all origins and methods.
   *
   * @param registry The CorsRegistry to add mappings and allowed headers, methods and origins to.
   */
  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping(pathPattern).allowedOrigins(allowedOrigins).allowedMethods(allowedMethods);
  }
}
