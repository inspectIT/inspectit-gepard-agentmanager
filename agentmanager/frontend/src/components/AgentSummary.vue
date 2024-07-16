<template>
      <div class="lg:col-start-3 lg:row-end-1">
        <div class="rounded-lg bg-gray-50 shadow-sm  ring-gray-900/5">
          <dl class="flex flex-wrap">
            <div class="flex-auto pl-6 pt-6">
              <dt class="text-sm font-semibold leading-6 text-gray-900"> Eigenschaften </dt>
            </div>
            <div class="flex-none self-end px-6 pt-4">
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
                <time datetime="2023-01-31">{{ agent.javaversion}}</time>
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
export default {
  name: 'AgentSummary',
  components: {
  },
  setup() {
    const route = useRoute();
    const agentName = route.params.name;
    const agentsStore = useAgentsStore();
    const agent = agentsStore.getAgentByName(agentName);

    return {
      agentName,
      agent,
    };
  },
};
</script>

<style scoped>

</style>