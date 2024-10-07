/* (C) 2024 */
package rocks.inspectit.gepard.agentmanager.application.config;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import rocks.inspectit.gepard.agentmanager.configuration.service.GitService;

@Configuration
@AllArgsConstructor
public class GitConfiguration {

  private final GitService gitService;

  @PostConstruct
  public void initialize() {
    gitService.initializeLocalRepository();
  }
}
