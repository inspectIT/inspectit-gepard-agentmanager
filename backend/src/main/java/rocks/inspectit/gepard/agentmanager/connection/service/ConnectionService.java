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
import rocks.inspectit.gepard.agentmanager.connection.model.ConnectionDtoMapper;
import rocks.inspectit.gepard.agentmanager.connection.model.dto.ConnectionDto;
import rocks.inspectit.gepard.agentmanager.connection.model.dto.CreateConnectionRequest;
import rocks.inspectit.gepard.agentmanager.connection.model.dto.CreateConnectionResponse;

/** Service-Implementation for handling agent connection requests. */
@Slf4j
@Service
@RequiredArgsConstructor
public class ConnectionService {

  private final ConnectionDtoMapper connectionDtoMapper;

  private final HashMap<UUID, Connection> connectionCache = new HashMap<>();

  /**
   * Handles a connection request from an agent.
   *
   * @param connectRequest
   * @return
   */
  public CreateConnectionResponse handleConnectRequest(CreateConnectionRequest connectRequest) {
    Connection connection = connectionDtoMapper.toConnection(connectRequest);
    connectionCache.put(connection.getId(), connection);

    return connectionDtoMapper.toCreateConnectionResponse(connection);
  }

  public List<ConnectionDto> getConnections() {
    return connectionCache.values().stream().map(ConnectionDto::fromConnection).toList();
  }

  /**
   * Returns a connection by its id.
   *
   * @param id The id of the connection.
   * @return ReadConnectionDTO The connection.
   */
  public ConnectionDto getConnection(UUID id) {
    if (!connectionCache.containsKey(id)) {
      throw new NoSuchElementException("No connection with id " + id + " found in cache.");
    }
    return ConnectionDto.fromConnection(connectionCache.get(id));
  }
}
