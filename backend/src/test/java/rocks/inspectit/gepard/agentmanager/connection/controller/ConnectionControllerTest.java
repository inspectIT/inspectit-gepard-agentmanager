/* (C) 2024 */
package rocks.inspectit.gepard.agentmanager.connection.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import rocks.inspectit.gepard.agentmanager.connection.model.ConnectionStatus;
import rocks.inspectit.gepard.agentmanager.connection.model.dto.ConnectionDto;
import rocks.inspectit.gepard.agentmanager.connection.model.dto.QueryConnectionRequest;
import rocks.inspectit.gepard.agentmanager.connection.model.dto.UpdateConnectionRequest;
import rocks.inspectit.gepard.agentmanager.connection.service.ConnectionService;

@WebMvcTest(controllers = ConnectionController.class)
class ConnectionControllerTest {

  @Autowired private MockMvc mockMvc;

  @Autowired private ObjectMapper objectMapper;

  @MockBean private ConnectionService connectionService;

  @Test
  void get_connections_whenEverythingIsValid_shouldReturnOk() throws Exception {
    mockMvc
        .perform(get("/api/v1/connections"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON));
  }

  @Test
  void get_connection_whenEverythingIsValid_shouldReturnOk() throws Exception {
    String agentId = "12345";
    ConnectionDto connectionDto =
        new ConnectionDto(
            "id",
            Instant.now(),
            ConnectionStatus.CONNECTED,
            Duration.ofSeconds(3),
            "service name",
            "5",
            "7",
            "42@localhost",
            Instant.now(),
            "22",
            Map.of());
    when(connectionService.getConnection(agentId)).thenReturn(connectionDto);

    mockMvc
        .perform(get("/api/v1/connections/{id}", agentId))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(content().json(objectMapper.writeValueAsString(connectionDto)));
  }

  @Test
  void update_connection_whenEverythingIsValid_shouldReturnOk() throws Exception {
    UpdateConnectionRequest updateRequest = new UpdateConnectionRequest(ConnectionStatus.CONNECTED);
    String agentId = "12345";
    ConnectionDto connectionDto =
        new ConnectionDto(
            "id",
            Instant.now(),
            ConnectionStatus.CONNECTED,
            Duration.ofSeconds(3),
            "service name",
            "5",
            "7",
            "42@localhost",
            Instant.now(),
            "22",
            Map.of());
    when(connectionService.handleUpdateRequest(agentId, updateRequest)).thenReturn(connectionDto);

    mockMvc
        .perform(
            patch("/api/v1/connections/{id}", agentId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(content().json(objectMapper.writeValueAsString(connectionDto)));
  }

  @Test
  void update_connection_whenFieldIsUnknown_shouldReturnBadRequest() throws Exception {
    String agentId = "12345";
    String updateRequest =
        """
            {
            "update": "delete"
            }
            """;

    mockMvc
        .perform(
            patch("/api/v1/connections/{id}", agentId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateRequest))
        .andExpect(status().isBadRequest());
  }

  @Test
  void queryConnections_whenMultipleParametersAreDefined_shouldReturnOk() throws Exception {
    QueryConnectionRequest queryRequest =
        new QueryConnectionRequest(
            LocalDateTime.now().toString(),
            ConnectionStatus.CONNECTED.toString(),
            new QueryConnectionRequest.QueryAgentRequest(
                "service-name", "12345", "0.0.1", "1.26.8", "67887", "22", Map.of("key", "value")));

    List<ConnectionDto> connectionDtos =
        List.of(
            new ConnectionDto(
                "id",
                Instant.now(),
                ConnectionStatus.CONNECTED,
                Duration.ofSeconds(2),
                "service-name",
                "0.0.1",
                "1.26.8",
                "67887@localhost",
                Instant.now(),
                "22",
                Map.of()));

    when(connectionService.queryConnections(queryRequest)).thenReturn(connectionDtos);

    mockMvc
        .perform(
            post("/api/v1/connections/query")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(queryRequest)))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(content().json(objectMapper.writeValueAsString(connectionDtos)));
  }

  @Test
  void queryConnections_whenUnknownFieldIsProvided_shouldReturnBadRequest() throws Exception {
    String requestBody =
        """
            {
                "id": "123e4567-e89b-12d3-a456-426614174000",
                "unknownField": "value"
            }
            """;

    mockMvc
        .perform(
            post("/api/v1/connections/query")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
        .andExpect(status().isBadRequest());
  }

  @Test
  void queryConnections_whenRegexInParameters_shouldReturnOk() throws Exception {
    QueryConnectionRequest queryRequest =
        new QueryConnectionRequest(
            "^2023-04-[0-9]+T[0-9:]+Z$",
            ConnectionStatus.CONNECTED.name(),
            new QueryConnectionRequest.QueryAgentRequest(
                "regex:^service-.*",
                "12345L",
                "0\\.0\\.1",
                "1\\.26\\.8",
                "67887L",
                "22",
                Map.of("key", "regex:^value.*")));

    List<ConnectionDto> connectionDtos =
        List.of(
            new ConnectionDto(
                "id",
                Instant.parse("2023-04-15T12:34:56Z"),
                ConnectionStatus.CONNECTED,
                Duration.ofSeconds(2),
                "service-name",
                "0.0.1",
                "1.26.8",
                "67887@localhost",
                Instant.now(),
                "22",
                Map.of("key", "value-123")));

    when(connectionService.queryConnections(queryRequest)).thenReturn(connectionDtos);

    mockMvc
        .perform(
            post("/api/v1/connections/query")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(queryRequest)))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(content().json(objectMapper.writeValueAsString(connectionDtos)));
  }

  @Test
  void queryConnections_whenRegexInParametersIsInvalid_shouldReturnBadRequest() throws Exception {
    QueryConnectionRequest queryRequest =
        new QueryConnectionRequest(
            null,
            null,
            new QueryConnectionRequest.QueryAgentRequest(
                "regex:*service-.*", null, null, null, null, null, null));

    mockMvc
        .perform(
            post("/api/v1/connections/query")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(queryRequest)))
        .andExpect(status().isBadRequest());
  }
}
