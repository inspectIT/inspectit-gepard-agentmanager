/* (C) 2024 */
package rocks.inspectit.gepard.agentmanager.configuration.model.instrumentation;

import jakarta.validation.Valid;
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

  @Valid private List<Scope> scopes = List.of();
}