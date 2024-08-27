/* (C) 2024 */
package rocks.inspectit.gepard.agentmanager.connection.service;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import rocks.inspectit.gepard.agentmanager.connection.model.dto.CreateConnectionRequest;
import rocks.inspectit.gepard.agentmanager.connection.model.dto.CreateConnectionResponse;

@ExtendWith(MockitoExtension.class)
class ConnectionServiceTest {

  @InjectMocks private ConnectionService connectionService;

  @Test
  void testHandleConnectRequest() {
    CreateConnectionRequest createConnectionRequest =
        CreateConnectionRequest.builder()
            .startTime(Instant.now().toEpochMilli())
            .javaVersion("22")
            .otelVersion("3")
            .gepardVersion("4")
            .pid(4435L)
            .serviceName("ServiceName")
            .build();

    CreateConnectionResponse response =
        connectionService.handleConnectRequest(createConnectionRequest);

    assertEquals(createConnectionRequest.startTime(), response.startTime());
    assertEquals(createConnectionRequest.javaVersion(), response.javaVersion());
    assertEquals(createConnectionRequest.otelVersion(), response.otelVersion());
    assertEquals(createConnectionRequest.gepardVersion(), response.gepardVersion());
    assertEquals(createConnectionRequest.pid(), response.pid());
    assertEquals(createConnectionRequest.serviceName(), response.serviceName());
    assertNotNull(response.id());
  }

  @Test
  void testGetConnections() {
    CreateConnectionRequest createConnectionRequest =
        CreateConnectionRequest.builder()
            .startTime(Instant.now().toEpochMilli())
            .javaVersion("22")
            .otelVersion("3")
            .gepardVersion("4")
            .pid(4435L)
            .serviceName("ServiceName")
            .build();
    connectionService.handleConnectRequest(createConnectionRequest);
    connectionService.handleConnectRequest(createConnectionRequest);

    List<CreateConnectionResponse> response = connectionService.getConnections();

    assertEquals(2, response.size());
  }

  @Test
  void testGetConnectionsEmptyResult() {
    List<CreateConnectionResponse> response = connectionService.getConnections();
    assertEquals(0, response.size());
  }

  @Test
  void testGetConnection() {
    CreateConnectionRequest createConnectionRequest =
        CreateConnectionRequest.builder()
            .startTime(Instant.now().toEpochMilli())
            .javaVersion("22")
            .otelVersion("3")
            .gepardVersion("4")
            .pid(4435L)
            .serviceName("ServiceName")
            .build();
    CreateConnectionResponse createConnectionResponse =
        connectionService.handleConnectRequest(createConnectionRequest);

    CreateConnectionResponse response =
        connectionService.getConnection(createConnectionResponse.id());

    assertEquals(createConnectionResponse.id(), response.id());
    assertEquals(createConnectionRequest.startTime(), response.startTime());
    assertEquals(createConnectionRequest.javaVersion(), response.javaVersion());
    assertEquals(createConnectionRequest.otelVersion(), response.otelVersion());
    assertEquals(createConnectionRequest.gepardVersion(), response.gepardVersion());
    assertEquals(createConnectionRequest.pid(), response.pid());
    assertEquals(createConnectionRequest.serviceName(), response.serviceName());
  }

  @Test
  void testGetConnectionNotFound() {
    UUID id = UUID.randomUUID();
    Exception exception =
        assertThrows(NoSuchElementException.class, () -> connectionService.getConnection(id));
    assertEquals("No connection with id " + id + " found in cache.", exception.getMessage());
  }
}
