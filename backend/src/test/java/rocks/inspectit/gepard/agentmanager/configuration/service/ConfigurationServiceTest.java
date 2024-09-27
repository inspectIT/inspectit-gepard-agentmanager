/* (C) 2024 */
package rocks.inspectit.gepard.agentmanager.configuration.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import rocks.inspectit.gepard.agentmanager.configuration.model.InspectitConfiguration;
import rocks.inspectit.gepard.agentmanager.configuration.model.instrumentation.InstrumentationConfiguration;
import rocks.inspectit.gepard.agentmanager.configuration.model.instrumentation.Scope;
import rocks.inspectit.gepard.agentmanager.exception.JsonParseException;

@ExtendWith(MockitoExtension.class)
class ConfigurationServiceTest {

  @InjectMocks private ConfigurationService configurationService;

  @Mock private GitService gitService;

  @Spy private ObjectMapper objectMapper;

  private InspectitConfiguration configuration;

  @BeforeEach
  public void setUp() {
    Scope scope = new Scope("org.domain.test", List.of("method1", "method2"), true);
    InstrumentationConfiguration instrumentationConfiguration =
        new InstrumentationConfiguration(List.of(scope));
    configuration = new InspectitConfiguration(instrumentationConfiguration);
  }

  @Test
  void testGetConfiguration_SuccessfulDeserialization() throws JsonProcessingException {
    String fileContent =
        """
                {
                    "instrumentation": {
                        "scopes": [
                            {
                                "fqn": "org.domain.test1",
                                "methods": [
                                    "method1",
                                    "method2"
                                ],
                                "enabled": true
                            }
                        ]
                    }
                }
                """;
    when(gitService.getFileContent()).thenReturn(fileContent);
    when(objectMapper.readValue(fileContent, InspectitConfiguration.class))
        .thenReturn(configuration);

    configurationService.getConfiguration();

    verify(gitService, times(1)).getFileContent();
  }

  @Test
  void testGetConfiguration_NotSuccessfulDeserialization() {
    String fileContent =
        """
                    {
                        "wrongAttributeName": {
                        }
                    }
                    """;
    when(gitService.getFileContent()).thenReturn(fileContent);

    Exception exception =
        assertThrows(JsonParseException.class, () -> configurationService.getConfiguration());
    assertEquals(
        "Failed to deserialize JSON content to InspectitConfiguration", exception.getMessage());
  }

  @Test
  void testUpdateConfiguration_SuccessfulSerialization() {
    configurationService.updateConfiguration(configuration);

    verify(gitService, times(1)).updateFileContent(anyString());
    verify(gitService, times(1)).commit();
  }

  @Test
  void testUpdateConfiguration_NotSuccessfulSerialization() throws JsonProcessingException {
    when(objectMapper.writeValueAsString(any())).thenThrow(JsonProcessingException.class);

    Exception exception =
        assertThrows(
            JsonParseException.class,
            () -> configurationService.updateConfiguration(configuration));

    verify(gitService, times(0)).updateFileContent(anyString());
    verify(gitService, times(0)).commit();
    assertEquals("Failed to serialize InspectitConfiguration to JSON", exception.getMessage());
  }
}
