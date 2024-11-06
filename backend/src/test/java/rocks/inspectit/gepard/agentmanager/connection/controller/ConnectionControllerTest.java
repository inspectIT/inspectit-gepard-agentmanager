/* (C) 2024 */
package rocks.inspectit.gepard.agentmanager.connection.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import rocks.inspectit.gepard.agentmanager.connection.model.Connection;
import rocks.inspectit.gepard.agentmanager.connection.model.ConnectionStatus;
import rocks.inspectit.gepard.agentmanager.connection.model.dto.ConnectionDto;
import rocks.inspectit.gepard.agentmanager.connection.model.dto.CreateConnectionRequest;
import rocks.inspectit.gepard.agentmanager.connection.model.dto.QueryConnectionRequest;
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
        new CreateConnectionRequest(
            "customer-service-e",
            "0.0.1",
            "1.26.8",
            "67887@localhost",
            "7e4686b7998c88427b14700f1c2aa69304a1c2fdb899067efe8ba9542fc02029",
            Instant.now().toEpochMilli(),
            "22",
            Map.of());

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
                .string(
                    "Location",
                    "http://localhost/api/v1/connections/" + connection.getAgent().getAgentId()));
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
    String agentId = "12345";
    ConnectionDto connectionDto =
        new ConnectionDto(
            Instant.now(),
            ConnectionStatus.CONNECTED,
            "service name",
            "5",
            "7",
            "42@localhost",
            agentId,
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

  // TODO updateConnection tests

  @Test
  void queryConnections_whenMultipleParametersAreDefined_shouldReturnOk() throws Exception {
    QueryConnectionRequest queryRequest =
        new QueryConnectionRequest(
            LocalDateTime.now().toString(),
            ConnectionStatus.CONNECTED.toString(),
            new QueryConnectionRequest.QueryAgentRequest(
                "service-name",
                "12345",
                "123456",
                "0.0.1",
                "1.26.8",
                "67887",
                "22",
                Map.of("key", "value")));

    List<ConnectionDto> connectionDtos =
        List.of(
            new ConnectionDto(
                Instant.now(),
                ConnectionStatus.CONNECTED,
                "service-name",
                "0.0.1",
                "1.26.8",
                "67887@localhost",
                "7e4686b7998c88427b14700f1c2aa69304a1c2fdb899067efe8ba9542fc02029",
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
                "7e4686b7998c88427b14700f1c2aa69304a1c2fdb899067efe8ba9542fc02029",
                "0\\.0\\.1",
                "1\\.26\\.8",
                "67887L",
                "22",
                Map.of("key", "regex:^value.*")));

    List<ConnectionDto> connectionDtos =
        List.of(
            new ConnectionDto(
                Instant.parse("2023-04-15T12:34:56Z"),
                ConnectionStatus.CONNECTED,
                "service-name",
                "0.0.1",
                "1.26.8",
                "67887@localhost",
                "7e4686b7998c88427b14700f1c2aa69304a1c2fdb899067efe8ba9542fc02029",
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
                "regex:*service-.*", null, null, null, null, null, null, null));

    mockMvc
        .perform(
            post("/api/v1/connections/query")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(queryRequest)))
        .andExpect(status().isBadRequest());
  }
}
