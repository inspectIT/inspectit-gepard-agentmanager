/* (C) 2024 */
package rocks.inspectit.gepard.agentmanager.connection.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import rocks.inspectit.gepard.agentmanager.connection.model.Connection;
import rocks.inspectit.gepard.agentmanager.connection.model.dto.ConnectionDto;
import rocks.inspectit.gepard.agentmanager.connection.model.dto.CreateConnectionRequest;

/** Service-Implementation for handling agent connection requests. */
@Slf4j
@Service
@RequiredArgsConstructor
public class ConnectionService {

  private final ConcurrentHashMap<UUID, Connection> connectionCache = new ConcurrentHashMap<>();

  /**
   * Handles a connection request from an agent.
   *
   * @param connectRequest The request for the new connection to be created.
   * @return Connection The response containing all saved information.
   */
  public Connection handleConnectRequest(CreateConnectionRequest connectRequest) {
    Connection connection = CreateConnectionRequest.toConnection(connectRequest);
    connectionCache.put(connection.getId(), connection);

    return connection;
  }

  /**
   * Returns all connections in the cache.
   *
   * @return List<ConnectionDto> All connections from the cache.
   */
  public List<ConnectionDto> getConnections() {
    return connectionCache.values().stream().map(ConnectionDto::fromConnection).toList();
  }

  /**
   * Returns a connection from the cache by its id.
   *
   * @param id The id of the connection.
   * @return ConnectionDto The connection.
   */
  public ConnectionDto getConnection(UUID id) {
    if (!connectionCache.containsKey(id)) {
      throw new NoSuchElementException("No connection with id " + id + " found in cache.");
    }
    return ConnectionDto.fromConnection(connectionCache.get(id));
  }
}
