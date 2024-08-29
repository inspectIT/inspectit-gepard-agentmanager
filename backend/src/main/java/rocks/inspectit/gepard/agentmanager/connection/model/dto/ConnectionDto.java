/* (C) 2024 */
package rocks.inspectit.gepard.agentmanager.connection.model.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Builder;
import rocks.inspectit.gepard.agentmanager.connection.model.Connection;

/** Represents a connection response. */
@Builder
public record ConnectionDto(
    @NotNull(message = "ID missing.") UUID id,
    @NotNull(message = "Registration Time missing.") LocalDateTime registrationTime,
    @NotNull(message = "Service Name missing.") String serviceName,
    @NotNull(message = "Gepard Version missing.") String gepardVersion,
    @NotNull(message = "Open-Telemetry Version missing.") String otelVersion,
    @NotNull(message = "Process ID is missing.") Long pid,
    @NotNull(message = "Start-Time missing.") Long startTime,
    @NotNull(message = "Java Version missing.") String javaVersion) {

  public static ConnectionDto fromConnection(Connection connection) {
    return ConnectionDto.builder()
        .id(connection.getId())
        .registrationTime(connection.getRegistrationTime())
        .serviceName(connection.getAgent().getServiceName())
        .gepardVersion(connection.getAgent().getGepardVersion())
        .otelVersion(connection.getAgent().getOtelVersion())
        .pid(connection.getAgent().getPid())
        .startTime(connection.getAgent().getStartTime().toEpochMilli())
        .javaVersion(connection.getAgent().getJavaVersion())
        .build();
  }
}
