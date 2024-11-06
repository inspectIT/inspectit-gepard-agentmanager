/* (C) 2024 */
package rocks.inspectit.gepard.agentmanager.connection.model.dto;

import jakarta.validation.constraints.NotNull;
import rocks.inspectit.gepard.agentmanager.connection.model.ConnectionStatus;

/**
 * Represents an update request from an agent.
 *
 * @param connectionStatus the updated status
 */
public record UpdateConnectionRequest(
    @NotNull(message = "Connection status is missing") ConnectionStatus connectionStatus) {}
