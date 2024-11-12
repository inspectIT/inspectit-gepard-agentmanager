/* (C) 2024 */
package rocks.inspectit.gepard.agentmanager.connection.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static rocks.inspectit.gepard.agentmanager.testutils.ConfigurationRequestTestUtils.getGepardHeadersAsMap;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentHashMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import rocks.inspectit.gepard.agentmanager.configuration.events.ConfigurationRequestEvent;
import rocks.inspectit.gepard.agentmanager.connection.model.Connection;
import rocks.inspectit.gepard.agentmanager.connection.model.ConnectionStatus;
import rocks.inspectit.gepard.agentmanager.connection.model.dto.ConnectionDto;
import rocks.inspectit.gepard.agentmanager.connection.model.dto.QueryConnectionRequest;
import rocks.inspectit.gepard.agentmanager.connection.model.dto.UpdateConnectionRequest;
import rocks.inspectit.gepard.agentmanager.connection.validation.RegexQueryService;
import rocks.inspectit.gepard.commons.model.agent.Agent;

@ExtendWith(MockitoExtension.class)
class ConnectionServiceTest {

  @InjectMocks private ConnectionService connectionService;
  private ConcurrentHashMap<String, Connection> connectionCache;

  @MockBean private RegexQueryService regexQueryService;

  @BeforeEach
  public void setUp() {
    connectionCache = new ConcurrentHashMap<>();
    regexQueryService = new RegexQueryService();
    connectionService = new ConnectionService(connectionCache, regexQueryService);
  }

  @Nested
  class GetConnection {

    @Test
    void testGetConnectionsEmptyResult() {
      List<ConnectionDto> response = connectionService.getConnections();
      assertEquals(0, response.size());
    }

    @Test
    void testGetConnectionNotFound() {
      String id = "7e4686b";
      Exception exception =
          assertThrows(NoSuchElementException.class, () -> connectionService.getConnection(id));
      assertEquals("No connection with id " + id + " found in cache.", exception.getMessage());
    }

    @Test
    void testGetConnections() {
      String id = "7e4686b7998c88427b14700f1c2aa69304a1c2fdb899067efe8ba9542fc02029";

      connectionCache.put(id, createTestConnection());

      List<ConnectionDto> response = connectionService.getConnections();

      assertEquals(1, response.size());
    }

    @Test
    void testGetConnection() {
      String id = "7e4686b7998c88427b14700f1c2aa69304a1c2fdb899067efe8ba9542fc02029";

      Connection connection = createTestConnection();
      connectionCache.put(id, connection);

      ConnectionDto connectionDto = connectionService.getConnection(id);

      assertEquals(connection.getAgent().getStartTime(), connectionDto.startTime());
      assertEquals(connection.getAgent().getJavaVersion(), connectionDto.javaVersion());
      assertEquals(connection.getAgent().getOtelVersion(), connectionDto.otelVersion());
      assertEquals(connection.getAgent().getGepardVersion(), connectionDto.gepardVersion());
      assertEquals(connection.getAgent().getVmId(), connectionDto.vmId());
      assertEquals(connection.getAgent().getServiceName(), connectionDto.serviceName());
    }
  }

  @Nested
  class HandleUpdateRequest {

    @Test
    void testHandleUpdateRequest() {
      String id = "7e4686b7998c88427b14700f1c2aa69304a1c2fdb899067efe8ba9542fc02029";
      Connection connection = createTestConnection(id);
      UpdateConnectionRequest updateConnectionRequest =
          new UpdateConnectionRequest(ConnectionStatus.DISCONNECTED);
      connectionCache.put(id, connection);

      ConnectionDto response = connectionService.handleUpdateRequest(id, updateConnectionRequest);

      assertEquals(connection.getRegistrationTime(), response.registrationTime());
      assertEquals(updateConnectionRequest.connectionStatus(), response.connectionStatus());
      assertEquals(connection.getAgent().getServiceName(), response.serviceName());
      assertEquals(connection.getAgent().getGepardVersion(), response.gepardVersion());
      assertEquals(connection.getAgent().getOtelVersion(), response.otelVersion());
      assertEquals(connection.getAgent().getVmId(), response.vmId());
      assertEquals(connection.getAgent().getStartTime(), response.startTime());
      assertEquals(connection.getAgent().getJavaVersion(), response.javaVersion());
      assertEquals(connection.getAgent().getAttributes(), response.attributes());
    }

    @Test
    void testHandleUpdateRequestNoSuchElement() {
      String id = "7e4686b7998c88427b14700f1c2aa69304a1c2fdb899067efe8ba9542fc02029";
      UpdateConnectionRequest updateConnectionRequest =
          new UpdateConnectionRequest(ConnectionStatus.DISCONNECTED);

      assertThrows(
          NoSuchElementException.class,
          () -> connectionService.handleUpdateRequest(id, updateConnectionRequest));
    }
  }

  @Nested
  class QueryConnections {

    @Test
    void testQueryShouldReturnEmptyListWhenNoConnections() {
      QueryConnectionRequest query = new QueryConnectionRequest(null, null, null);
      List<ConnectionDto> result = connectionService.queryConnections(query);
      assertTrue(result.isEmpty());
    }

    @Test
    void testQueryShouldFindConnectionById() {
      // given
      String id = "7e4686b";
      Connection connection = createTestConnection();
      connectionCache.put(id, connection);
      QueryConnectionRequest query = new QueryConnectionRequest(null, null, null);

      // when
      List<ConnectionDto> result = connectionService.queryConnections(query);

      // then
      assertThat(result).hasSize(1);
    }

    @Test
    void testQueryShouldFindConnectionByRegistrationTime() {
      // given
      String id = "7e4686b";
      Instant registrationTime = Instant.now();
      Connection connection = createTestConnection(registrationTime);
      connectionCache.put(id, connection);

      QueryConnectionRequest query =
          new QueryConnectionRequest(registrationTime.toString(), null, null);

      // when
      List<ConnectionDto> result = connectionService.queryConnections(query);

      // then
      assertThat(result).hasSize(1);
      assertThat(result.get(0).registrationTime()).isEqualTo(registrationTime);
    }

    @Test
    void testQueryShouldFindConnectionByAgentServiceName() {
      // given
      String id = "7e4686b";
      Connection connection = createTestConnection();
      connectionCache.put(id, connection);

      QueryConnectionRequest query =
          new QueryConnectionRequest(
              null,
              null,
              new QueryConnectionRequest.QueryAgentRequest(
                  "testService", null, null, null, null, null, null));

      // when
      List<ConnectionDto> result = connectionService.queryConnections(query);

      // then
      assertThat(result).hasSize(1);
      assertThat(result.get(0).serviceName()).isEqualTo("testService");
    }

    @Test
    void testQueryShouldFindConnectionByAgentAttributes() {
      // given
      String id = "7e4686b";
      Map<String, String> attributes = Map.of("custom", "attribute");
      Connection connection = createTestConnectionWithAttributes(attributes);
      connectionCache.put(id, connection);

      QueryConnectionRequest query =
          new QueryConnectionRequest(
              null,
              null,
              new QueryConnectionRequest.QueryAgentRequest(
                  null, null, null, null, null, null, attributes));

      // when
      List<ConnectionDto> result = connectionService.queryConnections(query);

      // then
      assertThat(result).hasSize(1);
      assertThat(result.get(0).attributes()).containsAllEntriesOf(attributes);
    }

    @Test
    void testQueryShouldNotFindConnectionWhenAttributesDontMatch() {
      // given
      String id = "7e4686b";
      Connection connection = createTestConnectionWithAttributes(Map.of("key1", "value1"));
      connectionCache.put(id, connection);

      QueryConnectionRequest query =
          new QueryConnectionRequest(
              null,
              null,
              new QueryConnectionRequest.QueryAgentRequest(
                  null, null, null, null, null, null, Map.of("key2", "value2")));

      // when
      List<ConnectionDto> result = connectionService.queryConnections(query);

      // then
      assertThat(result).isEmpty();
    }

    @Test
    void testQueryShouldFindConnectionWithMultipleCriteria() {
      // given
      String id = "7e4686b";
      Instant registrationTime = Instant.now();
      Map<String, String> attributes = Map.of("custom", "attribute");
      Connection connection = createTestConnectionWithAttributes(registrationTime, attributes);
      connectionCache.put(id, connection);

      QueryConnectionRequest query =
          new QueryConnectionRequest(
              registrationTime.toString(),
              ConnectionStatus.CONNECTED.toString(),
              new QueryConnectionRequest.QueryAgentRequest(
                  "testService", "1234@localhost", "1.0", "1.0", null, "17", attributes));

      // when
      List<ConnectionDto> result = connectionService.queryConnections(query);

      // then
      assertThat(result).hasSize(1);
      ConnectionDto dto = result.get(0);
      assertThat(dto.registrationTime()).isEqualTo(registrationTime);
      assertThat(dto.serviceName()).isEqualTo("testService");
      assertThat(dto.attributes()).containsAllEntriesOf(attributes);
    }

    @Test
    void testQueryShouldFindAllConnectionsWithoutCriterias() {
      // given
      String id1 = "7e4686b";
      String id2 = "7e4686c";
      Connection connection1 = createTestConnection();
      Connection connection2 = createTestConnection();
      connectionCache.put(id1, connection1);
      connectionCache.put(id2, connection2);

      QueryConnectionRequest query = new QueryConnectionRequest(null, null, null);

      // when
      List<ConnectionDto> result = connectionService.queryConnections(query);

      // then
      assertThat(result).hasSize(2);
    }

    @Test
    void testQueryShouldFindConnectionsByRegexRegistrationTime() {
      // given
      Instant registrationTime1 = Instant.parse("2023-04-15T12:34:56Z");
      Instant registrationTime2 = Instant.parse("2023-04-16T12:34:56Z");
      String id1 = "7e4686b";
      String id2 = "7e4686c";
      Connection connection1 = createTestConnection(registrationTime1);
      Connection connection2 = createTestConnection(registrationTime2);
      connectionCache.put(id1, connection1);
      connectionCache.put(id2, connection2);

      QueryConnectionRequest query =
          new QueryConnectionRequest("regex:^2023-04-[0-9]+T[0-9:]+Z$", null, null);

      // when
      List<ConnectionDto> result = connectionService.queryConnections(query);

      // then
      assertThat(result).hasSize(2);
      assertThat(result)
          .extracting(ConnectionDto::registrationTime)
          .containsExactlyInAnyOrder(registrationTime1, registrationTime2);
    }

    @Test
    void testQueryShouldFindConnectionsByRegexAgentServiceName() {
      // given
      String id1 = "7e4686b";
      String id2 = "7e4686c";
      Connection connection1 = createTestConnection("service-a");
      Connection connection2 = createTestConnection("service-b");
      connectionCache.put(id1, connection1);
      connectionCache.put(id2, connection2);

      QueryConnectionRequest query =
          new QueryConnectionRequest(
              null,
              null,
              new QueryConnectionRequest.QueryAgentRequest(
                  "regex:^service-.*", null, null, null, null, null, null));

      // when
      List<ConnectionDto> result = connectionService.queryConnections(query);

      // then
      assertThat(result).hasSize(2);
      assertThat(result)
          .extracting(ConnectionDto::serviceName)
          .containsExactlyInAnyOrder("service-a", "service-b");
    }

    @Test
    void testQueryShouldFindConnectionByRegexAgentStartTime() {
      // given
      String id = "7e4686b";
      Connection connection = createTestConnection();

      Instant nearStartTime = Instant.now();

      connectionCache.put(id, connection);

      // Create regex pattern that matches ISO-8601 format for the current time
      // Format: 2024-10-29T10:15:30.123Z
      String timeRegex = "regex:" + nearStartTime.toString().substring(0, 16) + ".*Z";

      QueryConnectionRequest query =
          new QueryConnectionRequest(
              null,
              null,
              new QueryConnectionRequest.QueryAgentRequest(
                  null, null, null, null, timeRegex, null, null));

      // when
      List<ConnectionDto> result = connectionService.queryConnections(query);

      // then
      assertThat(result).hasSize(1);

      // Assert that start time of result is within 1 second of nearStartTime
      Instant resultStartTime = result.get(0).startTime();
      assertThat(resultStartTime)
          .isBetween(nearStartTime.minusSeconds(1), nearStartTime.plusSeconds(1));
    }

    @Test
    void testQueryShouldFindConnectionsByRegexAgentAttributes() {
      // given
      String id1 = "7e4686b";
      String id2 = "7e4686c";
      Map<String, String> attributes1 = Map.of("key1", "value-123");
      Map<String, String> attributes2 = Map.of("key2", "value-456");
      Connection connection1 = createTestConnectionWithAttributes(attributes1);
      Connection connection2 = createTestConnectionWithAttributes(attributes2);
      connectionCache.put(id1, connection1);
      connectionCache.put(id2, connection2);

      QueryConnectionRequest query =
          new QueryConnectionRequest(
              null,
              null,
              new QueryConnectionRequest.QueryAgentRequest(
                  null, null, null, null, null, null, Map.of("key1", "regex:^value-.*")));

      // when
      List<ConnectionDto> result = connectionService.queryConnections(query);

      // then
      assertThat(result).hasSize(1);
      assertThat(result.get(0).attributes()).containsEntry("key1", "value-123");
    }

    @Nested
    class handleConfigurationRequestEvent {
      @Test
      void testHandleConfigurationRequestCreatesNewConnectionForUnknownService()
          throws InterruptedException {
        String id = "7e4686b7998c88427b14700f1c2aa69304a1c2fdb899067efe8ba9542fc02029";

        Map<String, String> headers = getGepardHeadersAsMap();

        ConfigurationRequestEvent event = new ConfigurationRequestEvent(this, id, headers);
        connectionService.handleConfigurationRequestEvent(event);

        Connection response = connectionCache.get(id);

        assertEquals(
            Instant.parse(headers.get("x-gepard-start-time")), response.getAgent().getStartTime());
        assertEquals(headers.get("x-gepard-java-version"), response.getAgent().getJavaVersion());
        assertEquals(headers.get("x-gepard-otel-version"), response.getAgent().getOtelVersion());
        assertEquals(
            headers.get("x-gepard-gepard-version"), response.getAgent().getGepardVersion());
        assertEquals(headers.get("x-gepard-vm-id"), response.getAgent().getVmId());
        assertEquals(headers.get("x-gepard-service-name"), response.getAgent().getServiceName());
      }

      @Test
      void testHandleConfigurationRequestUpdatesLastFetchTimeForKnownService() {
        String id = "7e4686b7998c88427b14700f1c2aa69304a1c2fdb899067efe8ba9542fc02029";

        Map<String, String> headers = getGepardHeadersAsMap();

        connectionService.handleConfigurationRequestEvent(
            new ConfigurationRequestEvent(this, id, headers));

        Duration firstFetchTime = connectionService.getConnection(id).timeSinceLastFetch();
        connectionService.handleConfigurationRequestEvent(
            new ConfigurationRequestEvent(this, id, headers));
        Duration secondFetchTime = connectionService.getConnection(id).timeSinceLastFetch();
        assertTrue(secondFetchTime.compareTo(firstFetchTime) < 0);
      }
    }
  }

  private Connection createTestConnection() {
    return createTestConnection(Instant.now(), "testService");
  }

  private Connection createTestConnection(Instant registrationTime) {
    return createTestConnection(registrationTime, "testService");
  }

  private Connection createTestConnection(String serviceName) {
    return createTestConnection(Instant.now(), serviceName);
  }

  private Connection createTestConnection(Instant registrationTime, String serviceName) {
    return new Connection(
        registrationTime,
        Instant.now(),
        ConnectionStatus.CONNECTED,
        new Agent(serviceName, "1234@localhost", "1.0", "1.0", Instant.now(), "17", Map.of()));
  }

  private Connection createTestConnectionWithAttributes(Map<String, String> attributes) {
    return createTestConnectionWithAttributes(Instant.now(), attributes);
  }

  private Connection createTestConnectionWithAttributes(
      Instant registrationTime, Map<String, String> attributes) {
    return new Connection(
        registrationTime,
        Instant.now(),
        ConnectionStatus.CONNECTED,
        new Agent("testService", "1234@localhost", "1.0", "1.0", Instant.now(), "17", attributes));
  }
}
