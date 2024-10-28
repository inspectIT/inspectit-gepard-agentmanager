/* (C) 2024 */
package rocks.inspectit.gepard.agentmanager.connection.service;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import rocks.inspectit.gepard.agentmanager.agent.model.Agent;
import rocks.inspectit.gepard.agentmanager.connection.model.Connection;
import rocks.inspectit.gepard.agentmanager.connection.model.dto.ConnectionDto;
import rocks.inspectit.gepard.agentmanager.connection.model.dto.CreateConnectionRequest;
import rocks.inspectit.gepard.agentmanager.connection.model.dto.QueryConnectionRequest;

/** Service-Implementation for handling agent connection requests. */
@Slf4j
@Service
@RequiredArgsConstructor
public class ConnectionService {

  private final ConcurrentHashMap<UUID, Connection> connectionCache;

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
   * Query connections based on the query request.
   *
   * @param query The query request.
   * @return List<ConnectionDto> The connections that match the query.
   */
  public List<ConnectionDto> queryConnections(QueryConnectionRequest query) {
    return connectionCache.values().stream()
        .filter(connection -> matchesConnection(connection, query))
        .map(ConnectionDto::fromConnection)
        .collect(Collectors.toList());
  }

  /**
   * Returns a connection from the cache by its id.
   *
   * @param id The id of the connection.
   * @return ConnectionDto The connection.
   * @throws NoSuchElementException if no connection with the given id is found in the cache.
   */
  public ConnectionDto getConnection(UUID id) {
    if (!connectionCache.containsKey(id)) {
      throw new NoSuchElementException("No connection with id " + id + " found in cache.");
    }
    return ConnectionDto.fromConnection(connectionCache.get(id));
  }

  /**
   * Checks if a connection matches the given query.
   *
   * @param connection The connection to check.
   * @param query The query to match against.
   * @return true if the connection matches the query, false otherwise.
   */
  private boolean matchesConnection(Connection connection, QueryConnectionRequest query) {
    if (query.id() != null && !connection.getId().toString().matches(query.id())) {
      return false;
    }

    if (query.registrationTime() != null
        && !connection.getRegistrationTime().toString().matches(query.registrationTime())) {
      return false;
    }

    if (query.agent() != null) {
      return matchesAgent(connection.getAgent(), query.agent());
    }

    return true;
  }

  /**
   * Checks if an agent matches the given query.
   *
   * @param agent The agent to check.
   * @param query The query to match against.
   * @return true if the agent matches the query, false otherwise.
   */
  private boolean matchesAgent(Agent agent, QueryConnectionRequest.QueryAgentRequest query) {
    if (query.serviceName() != null && !agent.getServiceName().matches(query.serviceName())) {
      return false;
    }

    if (query.gepardVersion() != null && !agent.getGepardVersion().matches(query.gepardVersion())) {
      return false;
    }

    if (query.otelVersion() != null && !agent.getOtelVersion().matches(query.otelVersion())) {
      return false;
    }

    if (query.javaVersion() != null && !agent.getJavaVersion().matches(query.javaVersion())) {
      return false;
    }

    if (query.attributes() != null && !query.attributes().isEmpty()) {
      Map<String, String> agentAttributes = agent.getAttributes();

      return matchesAttributes(agentAttributes, query.attributes());
    }

    return true;
  }

  /**
   * Checks if a set of agent attributes matches the given query attributes.
   *
   * @param agentAttributes The agent attributes to check.
   * @param queryAttributes The query attributes to match against.
   * @return true if the agent attributes match the query attributes, false otherwise.
   */
  private boolean matchesAttributes(
      Map<String, String> agentAttributes, Map<String, String> queryAttributes) {
    return queryAttributes.entrySet().stream()
        .allMatch(
            entry -> {
              String key = entry.getKey();
              String pattern = entry.getValue();
              String agentValue = agentAttributes.get(key);
              return agentValue != null && agentValue.matches(pattern);
            });
  }
}
