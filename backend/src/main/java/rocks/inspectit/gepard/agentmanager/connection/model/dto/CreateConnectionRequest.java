/* (C) 2024 */
package rocks.inspectit.gepard.agentmanager.connection.model.dto;

import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import java.util.Map;
import rocks.inspectit.gepard.commons.model.agent.Agent;
import rocks.inspectit.gepard.agentmanager.connection.model.Connection;
import rocks.inspectit.gepard.agentmanager.connection.model.ConnectionStatus;

/** Represents a connection request from an agent. */
public record CreateConnectionRequest(
    @NotNull(message = "Service Name missing.") String serviceName,
    @NotNull(message = "Gepard Version missing.") String gepardVersion,
    @NotNull(message = "OpenTelemetry Version missing.") String otelVersion,
    @NotNull(message = "VM-ID is missing.") String vmId,
    @NotNull(message = "Start-Time missing.") Long startTime,
    @NotNull(message = "Java Version missing.") String javaVersion,
    Map<String, String> attributes) {

  public static Connection toConnection(CreateConnectionRequest createConnectionRequest) {
    return new Connection(
        Instant.now(),
        ConnectionStatus.CONNECTED,
        new Agent(
            createConnectionRequest.serviceName,
            createConnectionRequest.vmId,
            createConnectionRequest.gepardVersion,
            createConnectionRequest.otelVersion,
            Instant.ofEpochMilli(createConnectionRequest.startTime),
            createConnectionRequest.javaVersion,
            createConnectionRequest.attributes != null
                ? createConnectionRequest.attributes
                : Map.of()));
  }
}
