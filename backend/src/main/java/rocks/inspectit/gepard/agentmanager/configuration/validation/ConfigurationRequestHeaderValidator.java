/* (C) 2024 */
package rocks.inspectit.gepard.agentmanager.configuration.validation;

import java.util.Map;
import rocks.inspectit.gepard.agentmanager.exception.MissingHeaderException;

public class ConfigurationRequestHeaderValidator {
  public static void validateConfigurationRequestHeaders(Map<String, String> headers) {
    validateHeader(headers, "x-gepard-service-name");
    validateHeader(headers, "x-gepard-vm-id");
    validateHeader(headers, "x-gepard-gepard-version");
    validateHeader(headers, "x-gepard-otel-version");
    validateHeader(headers, "x-gepard-java-version");
    validateHeader(headers, "x-gepard-start-time");
  }

  private static void validateHeader(Map<String, String> headers, String headerName) {
    String value = headers.get(headerName);
    if (value == null) {
      throw new MissingHeaderException(headerName + " header is required");
    }
  }
}
