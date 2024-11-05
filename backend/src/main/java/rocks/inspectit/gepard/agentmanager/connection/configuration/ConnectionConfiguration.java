/* (C) 2024 */
package rocks.inspectit.gepard.agentmanager.connection.configuration;

import java.util.concurrent.ConcurrentHashMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import rocks.inspectit.gepard.agentmanager.connection.model.Connection;

@Configuration
public class ConnectionConfiguration {
  @Bean
  public ConcurrentHashMap<String, Connection> connectionCache() {
    return new ConcurrentHashMap<>();
  }
}
