<template>
  <div class="px-4 sm:px-6 lg:px-8">
    <div class="sm:flex sm:items-center">
    </div>
    <div class="-mx-4 mt-8 sm:-mx-0">
      <table class="min-w-full divide-y divide-gray-300">
        <thead>
        <tr>
          <th scope="col" class="py-3.5 pl-4 pr-3 text-left text-sm font-semibold text-gray-900 sm:pl-0">Agentname</th>
          <th scope="col" class="hidden px-3 py-3.5 text-left text-sm font-semibold text-gray-900 sm:table-cell">Health</th>
          <th scope="col" class="hidden px-3 py-3.5 text-left text-sm font-semibold text-gray-900 lg:table-cell">Source</th>
          <th scope="col" class="px-3 py-3.5 text-left text-sm font-semibold text-gray-900">Attributes</th>
        </tr>
        </thead>
        <tbody class="divide-y divide-gray-200 bg-white">
        <tr v-for="agent in agents" :key="agent.id">
          <td class="whitespace-nowrap py-4 pl-4 pr-3 text-sm font-medium text-gray-900 sm:pl-0">{{ agent.name }}</td>
          <td class="hidden whitespace-nowrap px-3 py-4 text-sm text-gray-500 sm:table-cell">{{ agent.health }}</td>
          <td class="hidden whitespace-nowrap px-3 py-4 text-sm text-gray-500 lg:table-cell">{{ agent.source }}</td>
          <td class="whitespace-nowrap px-3 py-4 text-sm text-gray-500">{{ agent.attributes }}</td>
          <td class="whitespace-nowrap py-4 pl-3 pr-4 text-right text-sm font-medium sm:pr-0">
            <a href="#" class="text-indigo-600 hover:text-indigo-900"
            >Details<span class="sr-only">, {{ agent.name }}</span></a
            >
          </td>
        </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      agents: [],
    };
  },
  created() {
    this.fetchAgents();
  },
  methods: {
    async fetchAgents() {
      try {
        const response = await fetch('http://localhost:9000/agents');
        const data = await response.json();
        this.agents = data._embedded.agents.map(agent => ({
          id: agent.id,
          name: agent.name,
        }));
      } catch (error) {
        console.error('Fehler beim Abrufen der Agenten:', error);
      }
    },
  },
};

</script>