/* (C) 2024 */
package rocks.inspectit.gepard.agentmanager.configuration.events;

import java.util.Map;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class ConfigurationRequestEvent extends ApplicationEvent {

  private final String agentId;
  private final Map<String, String> headers;

  public ConfigurationRequestEvent(Object source, String agentId, Map<String, String> headers) {
    super(source);
    this.agentId = agentId;
    this.headers = headers;
  }
}
