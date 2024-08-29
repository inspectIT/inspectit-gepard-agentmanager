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
import rocks.inspectit.gepard.agentmanager.connection.model.Connection;
import rocks.inspectit.gepard.agentmanager.connection.model.dto.ConnectionDto;
import rocks.inspectit.gepard.agentmanager.connection.model.dto.CreateConnectionRequest;

@ExtendWith(MockitoExtension.class)
class ConnectionServiceTest {

  @InjectMocks private ConnectionService connectionService;

  @Test
  void testHandleConnectRequest() {
    CreateConnectionRequest createConnectionRequest =
        new CreateConnectionRequest(
            "customer-service-e", "0.0.1", "1.26.8", 67887L, Instant.now().toEpochMilli(), "22");

    Connection response = connectionService.handleConnectRequest(createConnectionRequest);

    assertEquals(
        createConnectionRequest.startTime(), response.getAgent().getStartTime().toEpochMilli());
    assertEquals(createConnectionRequest.javaVersion(), response.getAgent().getJavaVersion());
    assertEquals(createConnectionRequest.otelVersion(), response.getAgent().getOtelVersion());
    assertEquals(createConnectionRequest.gepardVersion(), response.getAgent().getGepardVersion());
    assertEquals(createConnectionRequest.pid(), response.getAgent().getPid());
    assertEquals(createConnectionRequest.serviceName(), response.getAgent().getServiceName());
    assertNotNull(response.getId());
  }

  @Test
  void testGetConnections() {
    CreateConnectionRequest createConnectionRequest =
        new CreateConnectionRequest(
            "customer-service-e", "0.0.1", "1.26.8", 67887L, Instant.now().toEpochMilli(), "22");
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
            "customer-service-e", "0.0.1", "1.26.8", 67887L, Instant.now().toEpochMilli(), "22");
    Connection connection = connectionService.handleConnectRequest(createConnectionRequest);

    ConnectionDto connectionDto = connectionService.getConnection(connection.getId());

    assertEquals(connection.getId(), connectionDto.id());
    assertEquals(createConnectionRequest.startTime(), connectionDto.startTime());
    assertEquals(createConnectionRequest.javaVersion(), connectionDto.javaVersion());
    assertEquals(createConnectionRequest.otelVersion(), connectionDto.otelVersion());
    assertEquals(createConnectionRequest.gepardVersion(), connectionDto.gepardVersion());
    assertEquals(createConnectionRequest.pid(), connectionDto.pid());
    assertEquals(createConnectionRequest.serviceName(), connectionDto.serviceName());
  }

  @Test
  void testGetConnectionNotFound() {
    UUID id = UUID.randomUUID();
    Exception exception =
        assertThrows(NoSuchElementException.class, () -> connectionService.getConnection(id));
    assertEquals("No connection with id " + id + " found in cache.", exception.getMessage());
  }
}
