/* (C) 2024 */
package rocks.inspectit.gepard.agentmanager.connection.service;

import static rocks.inspectit.gepard.agentmanager.configuration.validation.ConfigurationRequestHeaderValidator.validateConfigurationRequestHeaders;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import rocks.inspectit.gepard.agentmanager.connection.model.Connection;
import rocks.inspectit.gepard.agentmanager.connection.model.ConnectionStatus;
import rocks.inspectit.gepard.agentmanager.connection.model.dto.ConnectionDto;
import rocks.inspectit.gepard.agentmanager.connection.model.dto.QueryConnectionRequest;
import rocks.inspectit.gepard.agentmanager.connection.model.dto.UpdateConnectionRequest;
import rocks.inspectit.gepard.agentmanager.connection.validation.RegexQueryService;
import rocks.inspectit.gepard.commons.model.agent.Agent;

/** Service-Implementation for handling agent connection requests. */
@Slf4j
@Service
@RequiredArgsConstructor
public class ConnectionService {

  public static final String X_GEPARD_SERVICE_NAME = "x-gepard-service-name";
  public static final String X_GEPARD_VM_ID = "x-gepard-vm-id";
  public static final String X_GEPARD_GEPARD_VERSION = "x-gepard-gepard-version";
  public static final String X_GEPARD_OTEL_VERSION = "x-gepard-otel-version";
  public static final String X_GEPARD_JAVA_VERSION = "x-gepard-java-version";
  public static final String X_GEPARD_START_TIME = "x-gepard-start-time";

  private final ConcurrentHashMap<String, Connection> connectionCache;
  private final RegexQueryService regexQueryService;

  /**
   * Handles a ConfigurationRequest. If the agent is not connected, it will be connected. If it is
   * already connected, the last fetch time of the agent will be updated.
   *
   * @param agentId The id of the agent to be connected.
   * @param headers The request headers, which should contain the agent information.
   */
  public boolean handleConfigurationRequest(String agentId, Map<String, String> headers) {
    boolean isNewRegistration;
    if (!isAgentConnected(agentId)) {
      connectAgent(agentId, headers);
      isNewRegistration = true;
    } else {
      updateConnectionLastFetch(agentId);
      isNewRegistration = false;
    }
    return isNewRegistration;
  }

  /**
   * Handles an update request from an agent. Currently, we can only update the connection status.
   *
   * @param connectionId The id of the connection to be updated.
   * @param updateRequest The request to update an existing connection.
   * @return Connection The updated connection.
   * @throws NoSuchElementException if no connection with the given id is found in the cache.
   */
  public ConnectionDto handleUpdateRequest(
      String connectionId, UpdateConnectionRequest updateRequest) {
    Connection connection = connectionCache.get(connectionId);
    if (connection == null)
      throw new NoSuchElementException("Connection not found for agent: " + connectionId);

    connection.setConnectionStatus(updateRequest.connectionStatus());
    return ConnectionDto.fromConnection(connectionId, connection);
  }

  /**
   * Returns all connections in the cache.
   *
   * @return List<ConnectionDto> All connections from the cache.
   */
  public List<ConnectionDto> getConnections() {
    return connectionCache.entrySet().stream()
        .map(entry -> ConnectionDto.fromConnection(entry.getKey(), entry.getValue()))
        .toList();
  }

  /**
   * Query connections based on the query request.
   *
   * @param query The query request.
   * @return List<ConnectionDto> The connections that match the query.
   */
  public List<ConnectionDto> queryConnections(QueryConnectionRequest query) {
    return connectionCache.entrySet().stream()
        .filter(entry -> matchesConnection(entry.getValue(), query))
        .map(entry -> ConnectionDto.fromConnection(entry.getKey(), entry.getValue()))
        .toList();
  }

  /**
   * Returns a connection from the cache by its id.
   *
   * @param id The id of the connection.
   * @return ConnectionDto The connection.
   * @throws NoSuchElementException if no connection with the given id is found in the cache.
   */
  public ConnectionDto getConnection(String id) {
    Connection connection = connectionCache.get(id);
    if (connection == null)
      throw new NoSuchElementException("No connection with id " + id + " found in cache.");

    return ConnectionDto.fromConnection(id, connection);
  }

  /**
   * Determines if an agent is connected.
   *
   * @param agentId The id of the agent to be searched for.
   * @return true if the agent was found in the cache, false otherwise.
   */
  public boolean isAgentConnected(String agentId) {
    return connectionCache.get(agentId) != null;
  }

  /**
   * Updates the last fetch time of an agent connection.
   *
   * @param agentId The id of the agent to be updated.
   */
  private void updateConnectionLastFetch(String agentId) {
    Connection connection = connectionCache.get(agentId);
    if (connection != null) connection.setLastFetch(Instant.now());
    else
      throw new NoSuchElementException(
          "No connection for agent id " + agentId + " found in cache.");
  }

  /**
   * Checks if a connection matches the given query.
   *
   * @param connection The connection to check.
   * @param query The query to match against.
   * @return true if the connection matches the query, false otherwise.
   */
  private boolean matchesConnection(Connection connection, QueryConnectionRequest query) {
    boolean matches = true;

    matches &=
        regexQueryService.matches(
            connection.getRegistrationTime().toString(), query.registrationTime());

    matches &=
        regexQueryService.matches(
            connection.getConnectionStatus().toString(), query.connectionStatus());

    if (query.agent() != null) {

      QueryConnectionRequest.QueryAgentRequest queryAgent = query.agent();
      Agent agent = connection.getAgent();

      matches &= matchesAgent(agent, queryAgent);
    }

    return matches;
  }

  /**
   * Checks if an agent matches the given query.
   *
   * @param agent The agent to check.
   * @param query The query to match against.
   * @return true if the agent matches the query, false otherwise.
   */
  private boolean matchesAgent(Agent agent, QueryConnectionRequest.QueryAgentRequest query) {

    boolean matches = true;

    matches &= regexQueryService.matches(agent.getServiceName(), query.serviceName());
    matches &= regexQueryService.matches(agent.getVmId(), query.vmId());
    matches &= regexQueryService.matches(agent.getGepardVersion(), query.gepardVersion());
    matches &= regexQueryService.matches(agent.getOtelVersion(), query.otelVersion());
    matches &= regexQueryService.matchesInstant(agent.getStartTime(), query.startTime());
    matches &= regexQueryService.matches(agent.getJavaVersion(), query.javaVersion());

    if (query.attributes() != null && !query.attributes().isEmpty()) {
      matches &= matchesAttributes(agent.getAttributes(), query.attributes());
    }
    return matches;
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
            queryEntry -> {
              String actualValue = agentAttributes.get(queryEntry.getKey());
              return actualValue != null
                  && regexQueryService.matches(actualValue, queryEntry.getValue());
            });
  }

  /**
   * Handles a connection request from an agent.
   *
   * @param connectionId The id for the created connection.
   * @param headers The request headers, which should contain the agent information.
   */
  private void connectAgent(String connectionId, Map<String, String> headers) {

    validateConfigurationRequestHeaders(headers);

    String serviceName = headers.get(X_GEPARD_SERVICE_NAME);
    String vmId = headers.get(X_GEPARD_VM_ID);
    String gepardVersion = headers.get(X_GEPARD_GEPARD_VERSION);
    String otelVersion = headers.get(X_GEPARD_OTEL_VERSION);
    String javaVersion = headers.get(X_GEPARD_JAVA_VERSION);
    String startTime = headers.get(X_GEPARD_START_TIME);

    Map<String, String> attributes =
        headers.entrySet().stream()
            .filter(entry -> entry.getKey().startsWith("x-gepard-attribute-"))
            .collect(
                Collectors.toMap(
                    entry ->
                        entry
                            .getKey()
                            .substring("x-gepard-attribute-".length()), // remove the prefix
                    Map.Entry::getValue));

    Agent agent =
        new Agent(
            serviceName,
            vmId,
            gepardVersion,
            otelVersion,
            Instant.parse(startTime),
            javaVersion,
            attributes);

    Connection connection =
        new Connection(Instant.now(), Instant.now(), ConnectionStatus.CONNECTED, agent);

    connectionCache.put(connectionId, connection);
  }
}
