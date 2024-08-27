/* (C) 2024 */
package rocks.inspectit.gepard.agentmanager.configuration.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rocks.inspectit.gepard.agentmanager.configuration.model.InspectitConfiguration;
import rocks.inspectit.gepard.agentmanager.configuration.service.ConfigurationService;

@RestController
@RequestMapping("/api/v1/agent-configuration")
@RequiredArgsConstructor
public class ConfigurationController {

  private final ConfigurationService configurationService;

  @GetMapping
  @Operation(summary = "Get the agent configuration.")
  public ResponseEntity<InspectitConfiguration> getAgentConfiguration() {
    InspectitConfiguration configuration = configurationService.getConfiguration();

    // No config available
    if (Objects.isNull(configuration)) {
      return ResponseEntity.noContent().build();
    }

    return ResponseEntity.ok().body(configurationService.getConfiguration());
  }

  @PutMapping
  @Operation(summary = "Update the agent configuration.")
  public ResponseEntity<InspectitConfiguration> updateAgentConfiguration(
      @Valid @RequestBody InspectitConfiguration configuration) {
    configurationService.updateConfiguration(configuration);
    return ResponseEntity.ok().build();
  }
}
