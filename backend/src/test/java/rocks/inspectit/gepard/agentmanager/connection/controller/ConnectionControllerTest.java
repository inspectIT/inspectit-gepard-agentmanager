/* (C) 2024 */
package rocks.inspectit.gepard.agentmanager.connection.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import rocks.inspectit.gepard.agentmanager.connection.model.Connection;
import rocks.inspectit.gepard.agentmanager.connection.model.dto.ConnectionDto;
import rocks.inspectit.gepard.agentmanager.connection.model.dto.CreateConnectionRequest;
import rocks.inspectit.gepard.agentmanager.connection.service.ConnectionService;

@WebMvcTest(controllers = ConnectionController.class)
class ConnectionControllerTest {

  @Autowired private MockMvc mockMvc;

  @Autowired private ObjectMapper objectMapper;

  @MockBean private ConnectionService connectionService;

  @Test
  void connect_whenFieldIsMissing_shouldReturnBadRequest() throws Exception {
    String requestBody =
        """
            {
            "serviceName": "customer-service-e",
            "gepardVersion: "0.0.1",
            "otelVersion": "1.26.8"

            }
            """;

    mockMvc
        .perform(
            post("/api/v1/connections")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
        .andExpect(status().isBadRequest());
  }

  @Test
  void connect_whenEverythingIsValid_shouldReturnOk() throws Exception {
    CreateConnectionRequest createConnectionRequest =
        CreateConnectionRequest.of(
            "customer-service-e", "0.0.1", "1.26.8", 67887L, Instant.now().toEpochMilli(), "22");

    Connection connection = CreateConnectionRequest.toConnection(createConnectionRequest);
    when(connectionService.handleConnectRequest(createConnectionRequest)).thenReturn(connection);

    mockMvc
        .perform(
            post("/api/v1/connections")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createConnectionRequest)))
        .andExpect(status().isCreated())
        .andExpect(header().exists("Location"))
        .andExpect(
            header()
                .string("Location", "http://localhost/api/v1/connections/" + connection.getId()));
  }

  @Test
  void get_connections_whenEverythingIsValid_shouldReturnOk() throws Exception {
    mockMvc
        .perform(get("/api/v1/connections"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON));
  }

  @Test
  void get_connection_whenEverythingIsValid_shouldReturnOk() throws Exception {
    UUID uuid = UUID.randomUUID();
    ConnectionDto connectionDto =
        ConnectionDto.of(
            uuid, LocalDateTime.now(), "service name", "5", "7", 42L, 123456789L, "22");
    when(connectionService.getConnection(uuid)).thenReturn(connectionDto);

    mockMvc
        .perform(get("/api/v1/connections/{id}", uuid))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(content().json(objectMapper.writeValueAsString(connectionDto)));
  }
}
