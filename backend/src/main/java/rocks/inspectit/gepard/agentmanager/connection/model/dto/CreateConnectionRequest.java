/* (C) 2024 */
package rocks.inspectit.gepard.agentmanager.connection.model.dto;

import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;
import rocks.inspectit.gepard.agentmanager.agent.model.Agent;
import rocks.inspectit.gepard.agentmanager.connection.model.Connection;

/** Represents a connection request from an agent. */
public record CreateConnectionRequest(
    @NotNull(message = "Service Name missing.") String serviceName,
    @NotNull(message = "Gepard Version missing.") String gepardVersion,
    @NotNull(message = "Open-Telemetry Version missing.") String otelVersion,
    @NotNull(message = "Process ID is missing.") Long pid,
    @NotNull(message = "Start-Time missing.") Long startTime,
    @NotNull(message = "Java Version missing.") String javaVersion,
    Map<String, String> attributes) {

  public static Connection toConnection(CreateConnectionRequest createConnectionRequest) {
    return new Connection(
        UUID.randomUUID(),
        LocalDateTime.now(),
        new Agent(
            createConnectionRequest.serviceName,
            createConnectionRequest.pid,
            createConnectionRequest.gepardVersion,
            createConnectionRequest.otelVersion,
            Instant.ofEpochMilli(createConnectionRequest.startTime),
            createConnectionRequest.javaVersion,
            createConnectionRequest.attributes != null
                ? createConnectionRequest.attributes
                : Map.of()));
  }
}
