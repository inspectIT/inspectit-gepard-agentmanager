/* (C) 2024 */
package rocks.inspectit.gepard.agentmanager.configuration.validation;

import static rocks.inspectit.gepard.agentmanager.connection.service.ConnectionService.*;

import java.util.Map;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import rocks.inspectit.gepard.agentmanager.exception.MissingHeaderException;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ConfigurationRequestHeaderValidator {
  public static void validateConfigurationRequestHeaders(Map<String, String> headers) {
    validateHeader(headers, X_GEPARD_SERVICE_NAME);
    validateHeader(headers, X_GEPARD_VM_ID);
    validateHeader(headers, X_GEPARD_GEPARD_VERSION);
    validateHeader(headers, X_GEPARD_OTEL_VERSION);
    validateHeader(headers, X_GEPARD_JAVA_VERSION);
    validateHeader(headers, X_GEPARD_START_TIME);
  }

  private static void validateHeader(Map<String, String> headers, String headerName) {
    String value = headers.get(headerName);
    if (value == null) {
      throw new MissingHeaderException(headerName + " header is required");
    }
  }
}
