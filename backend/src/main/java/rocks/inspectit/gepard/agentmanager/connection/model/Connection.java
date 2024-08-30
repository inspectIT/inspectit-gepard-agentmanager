/* (C) 2024 */
package rocks.inspectit.gepard.agentmanager.connection.model;

import java.time.LocalDateTime;
import java.util.UUID;
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

  /** The id of the connection. */
  private UUID id;

  /** The registration time * */
  private LocalDateTime registrationTime;

  /** The agent which is connected. */
  private Agent agent;
}
