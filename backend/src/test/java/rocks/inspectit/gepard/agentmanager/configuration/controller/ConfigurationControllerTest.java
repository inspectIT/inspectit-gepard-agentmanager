/* (C) 2024 */
package rocks.inspectit.gepard.agentmanager.configuration.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static rocks.inspectit.gepard.agentmanager.testutils.ConfigurationRequestTestUtils.getGepardHeaders;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import rocks.inspectit.gepard.agentmanager.configuration.service.ConfigurationService;
import rocks.inspectit.gepard.agentmanager.connection.service.ConnectionService;
import rocks.inspectit.gepard.agentmanager.testutils.InspectitConfigurationTestUtil;
import rocks.inspectit.gepard.config.model.InspectitConfiguration;

@WebMvcTest(controllers = ConfigurationController.class)
class ConfigurationControllerTest {

  @Autowired private MockMvc mockMvc;

  @Autowired private ObjectMapper objectMapper;

  @MockBean private ConfigurationService configurationService;
  @MockBean private ConnectionService connectionService;

  private static String agentId = "12345";

  @Test
  void getConfiguration_whenNoConfigAvailable_shouldReturnNoContent() throws Exception {

    when(configurationService.getConfiguration()).thenReturn(null);

    mockMvc
        .perform(get("/api/v1/agent-configuration/" + agentId))
        .andExpect(status().isNoContent());
  }

  @Test
  void getConfiguration_whenConfigAvailable_shouldReturnOk() throws Exception {

    HttpHeaders headers = getGepardHeaders();

    when(configurationService.getConfiguration()).thenReturn(new InspectitConfiguration());

    mockMvc
        .perform(get("/api/v1/agent-configuration/" + agentId).headers(headers))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(content().json(objectMapper.writeValueAsString(new InspectitConfiguration())));
  }

  @Test
  void getConfiguration_whenAgentIsNew_shouldReturnRegistrationHeaderTrue() throws Exception {
    HttpHeaders headers = getGepardHeaders();
    when(configurationService.getConfiguration()).thenReturn(new InspectitConfiguration());
    when(connectionService.handleConfigurationRequest(agentId, headers.toSingleValueMap()))
        .thenReturn(true);

    mockMvc
        .perform(get("/api/v1/agent-configuration/" + agentId).headers(headers))
        .andExpect(header().string("x-gepard-service-registered", "true"));
  }

  @Test
  void getConfiguration_whenAgentIsNew_shouldReturnRegistrationHeaderFalse() throws Exception {
    HttpHeaders headers = getGepardHeaders();
    when(configurationService.getConfiguration()).thenReturn(new InspectitConfiguration());
    when(connectionService.handleConfigurationRequest(agentId, headers.toSingleValueMap()))
        .thenReturn(false);

    mockMvc
        .perform(get("/api/v1/agent-configuration/" + agentId).headers(headers))
        .andExpect(header().string("x-gepard-service-registered", "false"));
  }

  @Test
  void updateConfiguration_shouldReturnOkAndConfiguration() throws Exception {
    InspectitConfiguration configuration = InspectitConfigurationTestUtil.createConfiguration();

    mockMvc
        .perform(
            put("/api/v1/agent-configuration")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(configuration)))
        .andExpect(status().isOk());
  }
}
