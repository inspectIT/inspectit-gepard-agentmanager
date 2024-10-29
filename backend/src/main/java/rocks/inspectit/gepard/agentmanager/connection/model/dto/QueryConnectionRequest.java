/* (C) 2024 */
package rocks.inspectit.gepard.agentmanager.connection.model.dto;

import java.util.Map;

/**
 * Represents a request against the {@code ConnectionController} Query-Endpoint.
 *
 * <p>All fields are optional. If a field is not set, it is not considered in the query.
 */
public record QueryConnectionRequest(String id, String registrationTime, QueryAgentRequest agent) {

  public record QueryAgentRequest(
      String serviceName,
      Long pid,
      String gepardVersion,
      String otelVersion,
      Long startTime,
      String javaVersion,
      Map<String, String> attributes) {}
}
