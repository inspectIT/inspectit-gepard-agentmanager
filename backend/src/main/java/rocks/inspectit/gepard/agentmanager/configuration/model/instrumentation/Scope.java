/* (C) 2024 */
package rocks.inspectit.gepard.agentmanager.configuration.model.instrumentation;

import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Represents a scope in the instrumentation configuration. A scope defines a set of methods which
 * should be instrumented.
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Scope {

  @NotNull(message = "Fqn is missing.") private String fqn;

  private List<String> methods = List.of();

  private boolean enabled = false;
}
