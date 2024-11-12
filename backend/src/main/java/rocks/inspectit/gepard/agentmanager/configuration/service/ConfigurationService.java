/* (C) 2024 */
package rocks.inspectit.gepard.agentmanager.configuration.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import rocks.inspectit.gepard.agentmanager.configuration.events.ConfigurationRequestEvent;
import rocks.inspectit.gepard.agentmanager.exception.JsonParseException;
import rocks.inspectit.gepard.config.model.InspectitConfiguration;

@Service
@AllArgsConstructor
public class ConfigurationService {

  private final GitService gitService;

  private final ObjectMapper objectMapper;

  private final ApplicationEventPublisher applicationEventPublisher;

  /**
   * Retrieves the current configuration from the local Git repository.
   *
   * @return a valid {@link InspectitConfiguration}
   */
  public InspectitConfiguration getConfiguration() {
    try {
      return objectMapper.readValue(gitService.getFileContent(), InspectitConfiguration.class);
    } catch (JsonProcessingException e) {
      throw new JsonParseException(
          "Failed to deserialize JSON content to InspectitConfiguration", e);
    }
  }

  /**
   * Updates or creates an {@link InspectitConfiguration }
   *
   * @param configuration The configuration to be saved
   */
  public void updateConfiguration(InspectitConfiguration configuration) {
    try {
      String jsonContent = objectMapper.writeValueAsString(configuration);
      gitService.updateFileContent(jsonContent);
      gitService.commit();
    } catch (JsonProcessingException e) {
      throw new JsonParseException("Failed to serialize InspectitConfiguration to JSON", e);
    }
  }

  /**
   * Handles a configuration request from an agent. If the agent is not connected, it will be
   * connected. The last fetch time of the agent will be updated. The configuration will be
   * returned.
   *
   * @param agentId the id of the agent requesting the configuration
   * @param headers the request headers, which should contain the agent information
   * @return the inspectit configuration for this agent
   */
  public InspectitConfiguration handleConfigurationRequest(
      String agentId, Map<String, String> headers) {

    // Event Emitter instead
    ConfigurationRequestEvent event = new ConfigurationRequestEvent(this, agentId, headers);
    applicationEventPublisher.publishEvent(event);

    return getConfiguration();
  }
}
