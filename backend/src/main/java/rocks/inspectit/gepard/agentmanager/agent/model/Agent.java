/* (C) 2024 */
package rocks.inspectit.gepard.agentmanager.agent.model;

import jakarta.annotation.Nonnull;
import java.time.Instant;
import lombok.*;

/** Represents an agent which is connected to the config server. */
@RequiredArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Getter
public class Agent {
  /** The name of the service which is running the agent. */
  @Nonnull private String serviceName;

  /** The process id of the JVM which carries the agent. */
  @Nonnull private Long pid;

  /** The Gepard-Version. */
  @Nonnull private String gepardVersion;

  /** The OpenTelemetry-Java-Instrumentation-Version. */
  @Nonnull private String otelVersion;

  /** The start time of the JVM which carries the agent. */
  @Nonnull private Instant startTime;

  /** The Java version of the JVM which carries the agent. */
  @Nonnull private String javaVersion;
}
