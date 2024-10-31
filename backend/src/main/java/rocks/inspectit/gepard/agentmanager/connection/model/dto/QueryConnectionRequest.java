/* (C) 2024 */
package rocks.inspectit.gepard.agentmanager.connection.model.dto;

import jakarta.validation.Valid;
import java.util.Map;
import rocks.inspectit.gepard.agentmanager.connection.validation.ValidRegexPattern;

/**
 * Represents a request against the {@code ConnectionController} Query-Endpoint.
 *
 * <p>All fields are optional. If a field is not set, it is not considered in the query.
 */
public record QueryConnectionRequest(
    @ValidRegexPattern(message = "Invalid connection ID pattern") String id,
    @ValidRegexPattern(message = "Invalid registration time pattern") String registrationTime,
    @Valid QueryAgentRequest agent) {

  public record QueryAgentRequest(
      @ValidRegexPattern(message = "Invalid service name pattern") String serviceName,
      @ValidRegexPattern(message = "Invalid process ID pattern")
          String pid, // pid just has to be a number
      @ValidRegexPattern(message = "Invalid Gepard version pattern") String gepardVersion,
      @ValidRegexPattern(message = "Invalid OpenTelemetry version pattern") String otelVersion,
      @ValidRegexPattern(message = "Invalid start time pattern")
          String startTime, // startTime just has to be a number
      @ValidRegexPattern(message = "Invalid Java version pattern") String javaVersion,
      Map<String, String> attributes) {}
}
