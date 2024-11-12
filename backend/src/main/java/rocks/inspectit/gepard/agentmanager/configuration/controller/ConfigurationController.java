/* (C) 2024 */
package rocks.inspectit.gepard.agentmanager.configuration.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import java.util.Map;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rocks.inspectit.gepard.agentmanager.configuration.service.ConfigurationService;
import rocks.inspectit.gepard.config.model.InspectitConfiguration;

@RestController
@RequestMapping("/api/v1/agent-configuration")
@RequiredArgsConstructor
public class ConfigurationController {

  private final ConfigurationService configurationService;

  @GetMapping("/{agentId}")
  @Operation(
      summary =
          "Get the agent configuration and register the agent with the given id and agent info in the configuration server.")
  public ResponseEntity<InspectitConfiguration> getAgentConfiguration(
      @PathVariable String agentId, @RequestHeader Map<String, String> headers) {

    InspectitConfiguration configuration =
        configurationService.handleConfigurationRequest(agentId, headers);

    // No config available
    if (Objects.isNull(configuration)) {
      return ResponseEntity.noContent().build();
    }

    return ResponseEntity.ok().body(configuration);
  }

  @PutMapping
  @Operation(summary = "Update the agent configuration.")
  public ResponseEntity<InspectitConfiguration> updateAgentConfiguration(
      @Valid @RequestBody InspectitConfiguration configuration) {
    configurationService.updateConfiguration(configuration);
    return ResponseEntity.ok().build();
  }
}
