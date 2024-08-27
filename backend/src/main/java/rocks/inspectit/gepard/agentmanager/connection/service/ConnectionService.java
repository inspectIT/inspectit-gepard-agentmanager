/* (C) 2024 */
package rocks.inspectit.gepard.agentmanager.connection.service;

import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import rocks.inspectit.gepard.agentmanager.connection.model.Connection;
import rocks.inspectit.gepard.agentmanager.connection.model.dto.CreateConnectionRequest;
import rocks.inspectit.gepard.agentmanager.connection.model.dto.CreateConnectionResponse;

/** Service-Implementation for handling agent connection requests. */
@Slf4j
@Service
@RequiredArgsConstructor
public class ConnectionService {

  private final HashMap<UUID, Connection> connectionCache = new HashMap<>();

  /**
   * Handles a connection request from an agent.
   *
   * @param connectRequest The request for the new connection to be created.
   * @return CreateConnectionResponse The response containing all saved information.
   */
  public CreateConnectionResponse handleConnectRequest(CreateConnectionRequest connectRequest) {
    Connection connection = CreateConnectionRequest.toConnection(connectRequest);
    connectionCache.put(connection.getId(), connection);

    return CreateConnectionResponse.fromConnection(connection);
  }

  /**
   * Returns all connections in the cache.
   *
   * @return List<CreateConnectionResponse> All connections from the cache.
   */
  public List<CreateConnectionResponse> getConnections() {
    return connectionCache.values().stream().map(CreateConnectionResponse::fromConnection).toList();
  }

  /**
   * Returns a connection from the cache by its id.
   *
   * @param id The id of the connection.
   * @return ConnectionDto The connection.
   */
  public CreateConnectionResponse getConnection(UUID id) {
    if (!connectionCache.containsKey(id)) {
      throw new NoSuchElementException("No connection with id " + id + " found in cache.");
    }
    return CreateConnectionResponse.fromConnection(connectionCache.get(id));
  }
}
