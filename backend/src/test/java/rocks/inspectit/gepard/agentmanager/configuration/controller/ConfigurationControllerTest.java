/* (C) 2024 */
package rocks.inspectit.gepard.agentmanager.configuration.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import rocks.inspectit.gepard.agentmanager.configuration.model.InspectitConfiguration;
import rocks.inspectit.gepard.agentmanager.configuration.model.instrumentation.InstrumentationConfiguration;
import rocks.inspectit.gepard.agentmanager.configuration.model.instrumentation.Scope;
import rocks.inspectit.gepard.agentmanager.configuration.service.ConfigurationService;

@WebMvcTest(controllers = ConfigurationController.class)
class ConfigurationControllerTest {

  @Autowired private MockMvc mockMvc;

  @Autowired private ObjectMapper objectMapper;

  @MockBean private ConfigurationService configurationService;

  @Test
  void getConfiguration_whenNoConfigAvailable_shouldReturnNoContent() throws Exception {

    when(configurationService.getConfiguration()).thenReturn(null);

    mockMvc.perform(get("/api/v1/agent-configuration")).andExpect(status().isNoContent());
  }

  @Test
  void getConfiguration_whenConfigAvailable_shouldReturnOk() throws Exception {
    when(configurationService.getConfiguration()).thenReturn(new InspectitConfiguration());

    mockMvc
        .perform(get("/api/v1/agent-configuration"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(content().json(objectMapper.writeValueAsString(new InspectitConfiguration())));
  }

  @Test
  void updateConfiguration_shouldReturnOkAndConfiguration() throws Exception {

    InspectitConfiguration configuration = createConfiguration();

    when(configurationService.getConfiguration()).thenReturn(configuration);

    mockMvc
        .perform(
            get("/api/v1/agent-configuration")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(configuration)))
        .andExpect(status().isOk());
  }

  private InspectitConfiguration createConfiguration() {
    Scope scope = new Scope("org.test.package", List.of("testMethod"), true);
    InstrumentationConfiguration instrumentationConfiguration =
        new InstrumentationConfiguration(List.of(scope));
    return new InspectitConfiguration(instrumentationConfiguration);
  }
}
