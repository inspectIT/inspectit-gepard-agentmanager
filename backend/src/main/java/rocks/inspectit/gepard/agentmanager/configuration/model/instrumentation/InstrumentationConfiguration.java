/* (C) 2024 */
package rocks.inspectit.gepard.agentmanager.configuration.model.instrumentation;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * The Instrumentation Configuration contains all configuration related to instrumentation. e.g
 * scopes, rules, actions.
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class InstrumentationConfiguration {

  @Valid private List<@NotNull Scope> scopes = List.of();
}
