/* (C) 2024 */
package rocks.inspectit.gepard.agentmanager.connection.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

/** Represents a connection request from an agent. */
@Builder
public record CreateConnectionRequest(
    @NotNull(message = "Service Name missing.") String serviceName,
    @NotNull(message = "Gepard Version missing.") String gepardVersion,
    @NotNull(message = "Open-Telemetry Version missing.") String otelVersion,
    @NotNull(message = "Process ID is missing.") Long pid,
    @NotNull(message = "Start-Time missing.") Long startTime,
    @NotNull(message = "Java Version missing.") String javaVersion) {}
