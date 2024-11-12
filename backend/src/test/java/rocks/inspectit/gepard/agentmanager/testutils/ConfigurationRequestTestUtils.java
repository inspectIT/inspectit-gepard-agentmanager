/* (C) 2024 */
package rocks.inspectit.gepard.agentmanager.testutils;

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
    headers.add("x-gepard-service-name", "test-service");
    headers.add("x-gepard-vm-id", "test-vm-id");
    headers.add("x-gepard-gepard-version", "test-gepard-version");
    headers.add("x-gepard-otel-version", "test-otel-version");
    headers.add("x-gepard-java-version", "test-java-version");
    headers.add("x-gepard-start-time", Instant.now().toString());
    headers.add("x-gepard-attribute-test-attribute", "test-attribute-value");
    return headers;
  }
}
