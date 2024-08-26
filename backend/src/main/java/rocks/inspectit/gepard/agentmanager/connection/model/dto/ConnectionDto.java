/* (C) 2024 */
package rocks.inspectit.gepard.agentmanager.connection.model.dto;

import java.util.UUID;
import rocks.inspectit.gepard.agentmanager.connection.model.Connection;

/**
 * Represents a connection response for the UI, when reading connections.
 *
 * @param id
 * @param serviceName
 * @param gepardVersion
 * @param otelVersion
 * @param pid
 * @param startTime
 * @param javaVersion
 */
public record ConnectionDto(
    UUID id,
    String serviceName,
    String gepardVersion,
    String otelVersion,
    Long pid,
    Long startTime,
    String javaVersion) {

  public static ConnectionDto fromConnection(Connection connection) {
    return new ConnectionDto(
        connection.getId(),
        connection.getAgent().getServiceName(),
        connection.getAgent().getGepardVersion(),
        connection.getAgent().getOtelVersion(),
        connection.getAgent().getPid(),
        connection.getAgent().getStartTime().toEpochMilli(),
        connection.getAgent().getJavaVersion());
  }
}
