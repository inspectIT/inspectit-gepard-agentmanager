/* (C) 2024 */
package rocks.inspectit.gepard.agentmanager.connection.model;

import java.time.LocalDateTime;
import lombok.*;
import rocks.inspectit.gepard.agentmanager.agent.model.Agent;

/**
 * Represents a connected agent. It is an internal data structure and not exposed to the API. Acts
 * as Aggregate Root.
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Connection {

  /** The registration time. * */
  private LocalDateTime registrationTime;

  /** The status of the connection. */
  private ConnectionStatus connectionStatus;

  /** The agent which is connected. */
  private Agent agent;
}
