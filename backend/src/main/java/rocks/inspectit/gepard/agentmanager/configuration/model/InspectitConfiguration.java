/* (C) 2024 */
package rocks.inspectit.gepard.agentmanager.configuration.model;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import rocks.inspectit.gepard.agentmanager.configuration.model.instrumentation.InstrumentationConfiguration;

/** Model of an inspectit gepard configuration. */
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class InspectitConfiguration {

  @Valid private InstrumentationConfiguration instrumentation = new InstrumentationConfiguration();
}
