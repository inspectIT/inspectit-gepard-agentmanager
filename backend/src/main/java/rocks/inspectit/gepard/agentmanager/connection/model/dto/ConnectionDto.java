/* (C) 2024 */
package rocks.inspectit.gepard.agentmanager.connection.model.dto;

import jakarta.validation.constraints.NotNull;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import rocks.inspectit.gepard.agentmanager.connection.model.Connection;
import rocks.inspectit.gepard.agentmanager.connection.model.ConnectionStatus;

/** Represents a connection response. */
public record ConnectionDto(
    @NotNull(message = "Connection ID missing.") String connectionId,
    @NotNull(message = "Registration Time missing.") Instant registrationTime,
    @NotNull(message = "Connection status is missing") ConnectionStatus connectionStatus,
    @NotNull(message = "Time since last fetch is missing.") Duration timeSinceLastFetch,
    @NotNull(message = "Service Name missing.") String serviceName,
    @NotNull(message = "Gepard Version missing.") String gepardVersion,
    @NotNull(message = "OpenTelemetry Version missing.") String otelVersion,
    @NotNull(message = "VM-ID is missing.") String vmId,
    @NotNull(message = "Start-Time missing.") Instant startTime,
    @NotNull(message = "Java Version missing.") String javaVersion,
    @NotNull(message = "Attributes are missing.") Map<String, String> attributes) {

  public static ConnectionDto fromConnection(String id, Connection connection) {
    return new ConnectionDto(
        id,
        connection.getRegistrationTime(),
        connection.getConnectionStatus(),
        Duration.between(connection.getLastFetch(), Instant.now()),
        connection.getAgent().getServiceName(),
        connection.getAgent().getGepardVersion(),
        connection.getAgent().getOtelVersion(),
        connection.getAgent().getVmId(),
        connection.getAgent().getStartTime(),
        connection.getAgent().getJavaVersion(),
        connection.getAgent().getAttributes());
  }
}
