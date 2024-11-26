/* (C) 2024 */
package rocks.inspectit.gepard.agentmanager.testutils;

import static rocks.inspectit.gepard.agentmanager.connection.service.ConnectionService.*;
import static rocks.inspectit.gepard.agentmanager.connection.service.ConnectionService.X_GEPARD_START_TIME;

import java.time.Instant;
import java.util.Map;
import org.springframework.http.HttpHeaders;

public class ConfigurationRequestTestUtils {

  /**
   * Creates a map with headers for a Gepard request.
   *
   * @return the headers
   */
  public static Map<String, String> getGepardHeadersAsMap() {
    HttpHeaders headers = getGepardHeaders();
    return headers.toSingleValueMap();
  }

  /**
   * Creates a HttpHeaders object with headers for a Gepard request.
   *
   * @return the headers
   */
  public static HttpHeaders getGepardHeaders() {
    HttpHeaders headers = new HttpHeaders();
    headers.add(X_GEPARD_SERVICE_NAME, "test-service");
    headers.add(X_GEPARD_VM_ID, "test-vm-id");
    headers.add(X_GEPARD_GEPARD_VERSION, "test-gepard-version");
    headers.add(X_GEPARD_OTEL_VERSION, "test-otel-version");
    headers.add(X_GEPARD_JAVA_VERSION, "test-java-version");
    headers.add(X_GEPARD_START_TIME, Instant.now().toString());
    headers.add(X_GEPARD_ATTRIBUTE + "test-attribute", "test-attribute-value");
    return headers;
  }
}
