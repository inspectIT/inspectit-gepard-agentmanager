/* (C) 2024 */
package rocks.inspectit.gepard.agentmanager.agent.model;

import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import lombok.*;

/** Represents an agent which is connected to the config server. */
@AllArgsConstructor
@ToString
@Getter
public class Agent {
  /** The name of the service which is running the agent. */
  @NotNull private String serviceName;

  /** The process id of the JVM which carries the agent. */
  @NotNull private Long pid;

  /** The Gepard-Version. */
  @NotNull private String gepardVersion;

  /** The OpenTelemetry-Java-Instrumentation-Version. */
  @NotNull private String otelVersion;

  /** The start time of the JVM which carries the agent. */
  @NotNull private Instant startTime;

  /** The Java version of the JVM which carries the agent. */
  @NotNull private String javaVersion;
}
