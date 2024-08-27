/* (C) 2024 */
package rocks.inspectit.gepard.agentmanager.connection.model.dto;

import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Builder;
import rocks.inspectit.gepard.agentmanager.agent.model.Agent;
import rocks.inspectit.gepard.agentmanager.connection.model.Connection;

/** Represents a connection request from an agent. */
@Builder
public record CreateConnectionRequest(
    @NotNull(message = "Service Name missing.") String serviceName,
    @NotNull(message = "Gepard Version missing.") String gepardVersion,
    @NotNull(message = "Open-Telemetry Version missing.") String otelVersion,
    @NotNull(message = "Process ID is missing.") Long pid,
    @NotNull(message = "Start-Time missing.") Long startTime,
    @NotNull(message = "Java Version missing.") String javaVersion) {

  public static Connection toConnection(CreateConnectionRequest createConnectionRequest) {
    return Connection.builder()
        .id(UUID.randomUUID())
        .registrationTime(LocalDateTime.now())
        .agent(
            Agent.builder()
                .gepardVersion(createConnectionRequest.gepardVersion)
                .javaVersion(createConnectionRequest.javaVersion)
                .otelVersion(createConnectionRequest.otelVersion)
                .pid(createConnectionRequest.pid)
                .serviceName(createConnectionRequest.serviceName)
                .startTime(Instant.ofEpochMilli(createConnectionRequest.startTime))
                .build())
        .build();
  }
}
