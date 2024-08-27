/* (C) 2024 */
package rocks.inspectit.gepard.agentmanager.connection.model.dto;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Builder;
import rocks.inspectit.gepard.agentmanager.connection.model.Connection;

/** Represents a connection response. */
@Builder
public record ConnectionDto(
    UUID id,
    LocalDateTime registrationTime,
    String serviceName,
    String gepardVersion,
    String otelVersion,
    Long pid,
    Long startTime,
    String javaVersion) {

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
