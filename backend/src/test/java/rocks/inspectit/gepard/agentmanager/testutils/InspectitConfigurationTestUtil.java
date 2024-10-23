/* (C) 2024 */
package rocks.inspectit.gepard.agentmanager.testutils;

import java.util.Collections;
import java.util.Map;
import rocks.inspectit.gepard.config.model.InspectitConfiguration;
import rocks.inspectit.gepard.config.model.instrumentation.InstrumentationConfiguration;
import rocks.inspectit.gepard.config.model.instrumentation.rules.RuleConfiguration;
import rocks.inspectit.gepard.config.model.instrumentation.rules.RuleTracingConfiguration;
import rocks.inspectit.gepard.config.model.instrumentation.scopes.ScopeConfiguration;

public class InspectitConfigurationTestUtil {

  /**
   * Create a configuration object, with one rule, including one existing scope and enabled tracing.
   *
   * @return the inspectIT configuration as object
   */
  public static InspectitConfiguration createConfiguration() {
    ScopeConfiguration scope =
        new ScopeConfiguration(true, "com.example.Application", Collections.emptyList());
    RuleTracingConfiguration tracing = new RuleTracingConfiguration(true);
    RuleConfiguration rule = new RuleConfiguration(true, Map.of("s_scope", true), tracing);
    InstrumentationConfiguration instrumentationConfiguration =
        new InstrumentationConfiguration(Map.of("s_scope", scope), Map.of("r_rule", rule));

    return new InspectitConfiguration(instrumentationConfiguration);
  }
}
