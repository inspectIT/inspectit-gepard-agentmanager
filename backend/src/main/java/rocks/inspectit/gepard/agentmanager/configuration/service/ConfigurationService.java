/* (C) 2024 */
package rocks.inspectit.gepard.agentmanager.configuration.service;

import org.springframework.stereotype.Service;
import rocks.inspectit.gepard.agentmanager.configuration.model.InspectitConfiguration;

@Service
public class ConfigurationService {

  private InspectitConfiguration inspectitConfiguration;

  public InspectitConfiguration getConfiguration() {
    return inspectitConfiguration;
  }

  public void updateConfiguration(InspectitConfiguration configuration) {
    inspectitConfiguration = configuration;
  }
}
