/* (C) 2024 */
package rocks.inspectit.gepard.agentmanager.agent.model;

import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import java.util.Map;
import lombok.*;

/** Represents an agent which is connected to the config server. */
@AllArgsConstructor
@ToString
@Getter
public class Agent {
  /** The name of the service which is running the agent. */
  @NotNull private String serviceName;

  /** The id of the JVM process carries the agent. */
  @NotNull private String vmId;

  /** The hashed id of the agent. */
  @NotNull private String agentId;

  /** The Gepard-Version. */
  @NotNull private String gepardVersion;

  /** The OpenTelemetry-Java-Instrumentation-Version. */
  @NotNull private String otelVersion;

  /** The start time of the JVM which carries the agent. */
  @NotNull private Instant startTime;

  /** The Java version of the JVM which carries the agent. */
  @NotNull private String javaVersion;

  /** The custom attributes of the agent. */
  @NotNull private Map<String, String> attributes;
}
