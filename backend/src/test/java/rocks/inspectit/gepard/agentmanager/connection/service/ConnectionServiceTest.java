/* (C) 2024 */
package rocks.inspectit.gepard.agentmanager.connection.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import rocks.inspectit.gepard.agentmanager.agent.model.Agent;
import rocks.inspectit.gepard.agentmanager.connection.model.Connection;
import rocks.inspectit.gepard.agentmanager.connection.model.ConnectionStatus;
import rocks.inspectit.gepard.agentmanager.connection.model.dto.ConnectionDto;
import rocks.inspectit.gepard.agentmanager.connection.model.dto.CreateConnectionRequest;
import rocks.inspectit.gepard.agentmanager.connection.model.dto.QueryConnectionRequest;
import rocks.inspectit.gepard.agentmanager.connection.validation.RegexQueryService;

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

  @Test
  void testHandleConnectRequest() {
    CreateConnectionRequest createConnectionRequest =
        new CreateConnectionRequest(
            "customer-service-e",
            "0.0.1",
            "1.26.8",
            "67887@localhost",
            "7e4686b7998c88427b14700f1c2aa69304a1c2fdb899067efe8ba9542fc02029",
            Instant.now().toEpochMilli(),
            "22",
            Map.of());

    Connection response = connectionService.handleConnectRequest(createConnectionRequest);

    assertEquals(
        createConnectionRequest.startTime(), response.getAgent().getStartTime().toEpochMilli());
    assertEquals(createConnectionRequest.javaVersion(), response.getAgent().getJavaVersion());
    assertEquals(createConnectionRequest.otelVersion(), response.getAgent().getOtelVersion());
    assertEquals(createConnectionRequest.gepardVersion(), response.getAgent().getGepardVersion());
    assertEquals(createConnectionRequest.vmId(), response.getAgent().getVmId());
    assertEquals(createConnectionRequest.serviceName(), response.getAgent().getServiceName());
    assertEquals(createConnectionRequest.agentId(), response.getAgent().getAgentId());
  }

  @Test
  void testGetConnections() {
    CreateConnectionRequest createConnectionRequest =
        new CreateConnectionRequest(
            "customer-service-e",
            "0.0.1",
            "1.26.8",
            "67887@localhost",
            "7e4686b7998c88427b14700f1c2aa69304a1c2fdb899067efe8ba9542fc02029",
            Instant.now().toEpochMilli(),
            "22",
            Map.of());
    connectionService.handleConnectRequest(createConnectionRequest);
    connectionService.handleConnectRequest(createConnectionRequest);

    List<ConnectionDto> response = connectionService.getConnections();

    assertEquals(2, response.size());
  }

  @Test
  void testGetConnectionsEmptyResult() {
    List<ConnectionDto> response = connectionService.getConnections();
    assertEquals(0, response.size());
  }

  @Test
  void testGetConnection() {
    CreateConnectionRequest createConnectionRequest =
        new CreateConnectionRequest(
            "customer-service-e",
            "0.0.1",
            "1.26.8",
            "67887@localhost",
            "7e4686b7998c88427b14700f1c2aa69304a1c2fdb899067efe8ba9542fc02029",
            Instant.now().toEpochMilli(),
            "22",
            Map.of());
    Connection connection = connectionService.handleConnectRequest(createConnectionRequest);

    ConnectionDto connectionDto =
        connectionService.getConnection(connection.getAgent().getAgentId());

    assertEquals(createConnectionRequest.startTime(), connectionDto.startTime());
    assertEquals(createConnectionRequest.javaVersion(), connectionDto.javaVersion());
    assertEquals(createConnectionRequest.otelVersion(), connectionDto.otelVersion());
    assertEquals(createConnectionRequest.gepardVersion(), connectionDto.gepardVersion());
    assertEquals(createConnectionRequest.vmId(), connectionDto.vmId());
    assertEquals(createConnectionRequest.agentId(), connectionDto.agentId());
    assertEquals(createConnectionRequest.serviceName(), connectionDto.serviceName());
  }

  @Test
  void testGetConnectionNotFound() {
    UUID id = UUID.randomUUID();
    Exception exception =
        assertThrows(NoSuchElementException.class, () -> connectionService.getConnection(id));
    assertEquals("No connection with id " + id + " found in cache.", exception.getMessage());
  }

  @Test
  void testQueryShouldReturnEmptyListWhenNoConnections() {
    QueryConnectionRequest query = new QueryConnectionRequest(null, null, null);
    List<ConnectionDto> result = connectionService.queryConnections(query);
    assertTrue(result.isEmpty());
  }

  @Test
  void testQueryShouldFindConnectionById() {
    // given
    UUID id = UUID.randomUUID();
    Connection connection = createTestConnection(id);
    connectionCache.put(id, connection);
    QueryConnectionRequest query = new QueryConnectionRequest(id.toString(), null, null);

    // when
    List<ConnectionDto> result = connectionService.queryConnections(query);

    // then
    assertThat(result).hasSize(1);
    assertThat(result.get(0).id()).isEqualTo(id);
  }

  @Test
  void testQueryShouldFindConnectionByRegistrationTime() {
    // given
    UUID id = UUID.randomUUID();
    LocalDateTime registrationTime = LocalDateTime.now();
    Connection connection = createTestConnection(id, registrationTime);
    connectionCache.put(id, connection);

    QueryConnectionRequest query =
        new QueryConnectionRequest(null, registrationTime.toString(), null);

    // when
    List<ConnectionDto> result = connectionService.queryConnections(query);

    // then
    assertThat(result).hasSize(1);
    assertThat(result.get(0).registrationTime()).isEqualTo(registrationTime);
  }

  @Test
  void testQueryShouldFindConnectionByAgentServiceName() {
    // given
    UUID id = UUID.randomUUID();
    Connection connection = createTestConnection(id);
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
    UUID id = UUID.randomUUID();
    Map<String, String> attributes = Map.of("custom", "attribute");
    Connection connection = createTestConnectionWithAttributes(id, attributes);
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
    UUID id = UUID.randomUUID();
    Connection connection = createTestConnectionWithAttributes(id, Map.of("key1", "value1"));
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
    UUID id = UUID.randomUUID();
    LocalDateTime registrationTime = LocalDateTime.now();
    Map<String, String> attributes = Map.of("custom", "attribute");
    Connection connection = createTestConnectionWithAttributes(id, registrationTime, attributes);
    connectionCache.put(id, connection);

    QueryConnectionRequest query =
        new QueryConnectionRequest(
            id.toString(),
            registrationTime.toString(),
            new QueryConnectionRequest.QueryAgentRequest(
                "testService", "1234@localhost", "1.0", "1.0", null, "17", attributes));

    // when
    List<ConnectionDto> result = connectionService.queryConnections(query);

    // then
    assertThat(result).hasSize(1);
    ConnectionDto dto = result.get(0);
    assertThat(dto.id()).isEqualTo(id);
    assertThat(dto.registrationTime()).isEqualTo(registrationTime);
    assertThat(dto.serviceName()).isEqualTo("testService");
    assertThat(dto.attributes()).containsAllEntriesOf(attributes);
  }

  @Test
  void testQueryShouldFindAllConnectionsWithoutCriterias() {
    // given
    UUID id1 = UUID.randomUUID();
    UUID id2 = UUID.randomUUID();
    Connection connection1 = createTestConnection(id1);
    Connection connection2 = createTestConnection(id2);
    connectionCache.put(id1, connection1);
    connectionCache.put(id2, connection2);

    QueryConnectionRequest query = new QueryConnectionRequest(null, null, null);

    // when
    List<ConnectionDto> result = connectionService.queryConnections(query);

    // then
    assertThat(result).hasSize(2);
    assertThat(result).extracting(ConnectionDto::id).containsExactlyInAnyOrder(id1, id2);
  }

  @Test
  void testQueryShouldFindConnectionsByRegexId() {
    // given
    UUID id1 = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
    UUID id2 = UUID.fromString("123e4567-e89b-12d3-a456-426614174001");
    Connection connection1 = createTestConnection(id1);
    Connection connection2 = createTestConnection(id2);
    connectionCache.put(id1, connection1);
    connectionCache.put(id2, connection2);

    QueryConnectionRequest query =
        new QueryConnectionRequest("regex:^123e4567-e89b-12d3-a456-[0-9a-f]+$", null, null);

    // when
    List<ConnectionDto> result = connectionService.queryConnections(query);

    // then
    assertThat(result).hasSize(2);
    assertThat(result).extracting(ConnectionDto::id).containsExactlyInAnyOrder(id1, id2);
  }

  @Test
  void testQueryShouldFindConnectionsByRegexRegistrationTime() {
    // given
    LocalDateTime registrationTime1 = LocalDateTime.parse("2023-04-15T12:34:56");
    LocalDateTime registrationTime2 = LocalDateTime.parse("2023-04-16T12:34:56");
    Connection connection1 = createTestConnection(UUID.randomUUID(), registrationTime1);
    Connection connection2 = createTestConnection(UUID.randomUUID(), registrationTime2);
    connectionCache.put(connection1.getId(), connection1);
    connectionCache.put(connection2.getId(), connection2);

    QueryConnectionRequest query =
        new QueryConnectionRequest(null, "regex:^2023-04-[0-9]+T[0-9:]+$", null);

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
    UUID id1 = UUID.randomUUID();
    UUID id2 = UUID.randomUUID();
    Connection connection1 = createTestConnection(id1, "service-a");
    Connection connection2 = createTestConnection(id2, "service-b");
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
    UUID id = UUID.randomUUID();
    Connection connection = createTestConnection(id);

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
    Instant resultStartTime = Instant.ofEpochMilli(result.get(0).startTime());
    assertThat(resultStartTime)
        .isBetween(nearStartTime.minusSeconds(1), nearStartTime.plusSeconds(1));
  }

  @Test
  void testQueryShouldFindConnectionsByRegexAgentAttributes() {
    // given
    UUID id1 = UUID.randomUUID();
    UUID id2 = UUID.randomUUID();
    Map<String, String> attributes1 = Map.of("key1", "value-123");
    Map<String, String> attributes2 = Map.of("key2", "value-456");
    Connection connection1 = createTestConnectionWithAttributes(id1, attributes1);
    Connection connection2 = createTestConnectionWithAttributes(id2, attributes2);
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
    assertThat(result).extracting(ConnectionDto::id).containsExactly(id1);
    assertThat(result.get(0).attributes()).containsEntry("key1", "value-123");
  }

  private Connection createTestConnection() {
    return createTestConnection(LocalDateTime.now(), "testService");
  }

  private Connection createTestConnection(LocalDateTime registrationTime) {
    return createTestConnection(registrationTime, "testService");
  }

  private Connection createTestConnection(String serviceName) {
    return createTestConnection(LocalDateTime.now(), serviceName);
  }

  private Connection createTestConnection(LocalDateTime registrationTime, String serviceName) {
    return new Connection(
        registrationTime,
        ConnectionStatus.CONNECTED,
        new Agent(
            serviceName, "1234@localhost", "12345", "1.0", "1.0", Instant.now(), "17", Map.of()));
  }

  private Connection createTestConnectionWithAttributes(Map<String, String> attributes) {
    return createTestConnectionWithAttributes(LocalDateTime.now(), attributes);
  }

  private Connection createTestConnectionWithAttributes(
      LocalDateTime registrationTime, Map<String, String> attributes) {
    return new Connection(
        registrationTime,
        ConnectionStatus.CONNECTED,
        new Agent(
            "testService",
            "1234@localhost",
            "12345",
            "1.0",
            "1.0",
            Instant.now(),
            "17",
            attributes));
  }
}
