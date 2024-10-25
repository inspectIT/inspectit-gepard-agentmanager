/* (C) 2024 */
package rocks.inspectit.gepard.agentmanager.connection.model.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;
import rocks.inspectit.gepard.agentmanager.connection.model.Connection;

/** Represents a connection response. */
public record ConnectionDto(
    @NotNull(message = "ID missing.") UUID id,
    @NotNull(message = "Registration Time missing.") LocalDateTime registrationTime,
    @NotNull(message = "Service Name missing.") String serviceName,
    @NotNull(message = "Gepard Version missing.") String gepardVersion,
    @NotNull(message = "Open-Telemetry Version missing.") String otelVersion,
    @NotNull(message = "Process ID is missing.") Long pid,
    @NotNull(message = "Start-Time missing.") Long startTime,
    @NotNull(message = "Java Version missing.") String javaVersion,
    @NotNull(message = "Attributes are missing.") Map<String, String> attributes) {

  public static ConnectionDto of(
      UUID id,
      LocalDateTime registrationTime,
      String serviceName,
      String gepardVersion,
      String otelVersion,
      Long pid,
      Long startTime,
      String javaVersion) {
    return new ConnectionDto(
        id,
        registrationTime,
        serviceName,
        gepardVersion,
        otelVersion,
        pid,
        startTime,
        javaVersion,
        Map.of());
  }

  public static ConnectionDto fromConnection(Connection connection) {
    return new ConnectionDto(
        connection.getId(),
        connection.getRegistrationTime(),
        connection.getAgent().getServiceName(),
        connection.getAgent().getGepardVersion(),
        connection.getAgent().getOtelVersion(),
        connection.getAgent().getPid(),
        connection.getAgent().getStartTime().toEpochMilli(),
        connection.getAgent().getJavaVersion(),
        connection.getAgent().getAttributes());
  }
}
