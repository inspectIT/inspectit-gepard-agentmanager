<template>
  <header class="relative isolate pt-16">
    <div class="mx-auto max-w-7xl sm:px-6 lg:px-8">
      <div class="mx-auto px-8 flex max-w-2xl items-center justify-between gap-x-8 lg:mx-0 lg:max-w-none">
        <div class="flex items-center gap-x-6">
          <img src="/gepardLogo.png" alt="" class="h-16 w-16 flex-none rounded-full ring-1 ring-gray-900/10" />
          <h1>
            <div class="text-sm leading-6 text-gray-500">Agent</div>
            <div class="mt-1 text-base font-semibold leading-6 text-gray-900">{{ agent.serviceName }}</div>
          </h1>
        </div>
        <div class="flex items-center gap-x-4 sm:gap-x-6">
          <a href="#" class="rounded-md bg-indigo-600 px-3 py-2 text-sm font-semibold text-white
              shadow-sm hover:bg-indigo-500 focus-visible:outline focus-visible:outline-2
              focus-visible:outline-offset-2 focus-visible:outline-indigo-600">Promote</a>
        </div>
      </div>
    </div>
  </header>
</template>

<script>

export default {
  name: 'AgentDetailHeader',
  computed: {
  },
  components: {
  },
  setup() {
    const route = useRoute();
    const agentId = route.params.id;
    const agentsStore = useAgentsStore();

    console.log(agentId);

    const agent = agentsStore.getAgentById(agentId)

    return {
      agentId,
      agent,
    };
  },
};
</script>

<style scoped>

</style>