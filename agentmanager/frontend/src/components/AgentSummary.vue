<template>
  <div class="lg:col-start-3 lg:row-end-1 mt-16">
    <div class="rounded-lg bg-gray-50 shadow-sm  ring-gray-900/5">
      <dl class="flex flex-wrap">
        <div class="flex-auto pl-6 pt-6">
          <dt class="text-sm font-semibold leading-6 text-gray-900"> Eigenschaften</dt>
        </div>
        <div class="flex-none self-end px-8 pt-4 flex items-center justify-end space-x-2">
          <div :class="[environments[deployment.environment], 'rounded-md px-2 py-1 text-xs font-medium ring-1 ring-inset']">
            {{ deployment.environment }}
          </div>
          <dd class="rounded-md bg-green-50 px-2 py-1 text-xs font-medium text-green-600 ring-1 ring-inset ring-green-600/20">
            {{ agent.healthState }}
          </dd>
        </div>
        <div class="mt-6 flex w-full flex-none gap-x-4 border-t border-gray-900/5 px-6 pt-6">
          <dt class="flex-none">
                <span>
                  Otelversion:
                </span>
          </dt>
          <dd class="text-sm font-medium leading-6 text-gray-900">{{ agent.otelversion }}</dd>
        </div>
        <div class="mt-4 flex w-full flex-none gap-x-4 px-6">
          <dt class="flex-none">
                <span>
                  Javaversion:
                </span>
          </dt>
          <dd class="text-sm font-medium leading-6 text-gray-900">
            <time datetime="2023-01-31">{{ agent.javaversion }}</time>
          </dd>
        </div>
      </dl>
      <div class="mt-6 border-t border-gray-900/5 px-6 py-6">
        <!--            <a href="#" class="text-sm font-semibold leading-6 text-gray-900">See Details <span-->
        <!--                aria-hidden="true">&rarr;</span></a>-->
      </div>
    </div>
  </div>
</template>

<script>
import { Switch, SwitchGroup, SwitchLabel } from '@headlessui/vue'

export default {
  name: 'AgentSummary',
  components: {
    Switch,
    SwitchGroup,
    SwitchLabel
  },

  setup() {

    const route = useRoute();
    const agentName = route.params.name;
    const agentsStore = useAgentsStore();
    const agent = agentsStore.getAgentByName(agentName);

    const environments = {
      Dev: 'text-gray-400 bg-gray-400/10 ring-gray-400/20',
      Prod: 'text-red-400 bg-red-400/10 ring-red-400/30',
    }

    const defaultDeployment = {
      environment: 'Prod', // hard coded example
    };

    const deployment = agent ? (agent.deployment || defaultDeployment) : defaultDeployment;

    return {
      agentName,
      agent,
      environments,
      deployment,
    };
  },
};
</script>

<style scoped>

</style>