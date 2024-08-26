/* (C) 2024 */
package rocks.inspectit.gepard.agentmanager.connection.model.dto;

import java.util.UUID;

/**
 * Represents a successful connection response.
 *
 * @param id
 * @param serviceName
 * @param gepardVersion
 * @param otelVersion
 * @param pid
 * @param startTime
 * @param javaVersion
 */
public record CreateConnectionResponse(
    UUID id,
    String serviceName,
    String gepardVersion,
    String otelVersion,
    Long pid,
    Long startTime,
    String javaVersion) {}
