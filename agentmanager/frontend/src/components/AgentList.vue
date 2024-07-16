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
          <th scope="col" class="hidden px-3 py-3.5 text-left text-sm font-semibold text-gray-900 lg:table-cell">Javaversion</th>
          <th scope="col" class="px-3 py-3.5 text-left text-sm font-semibold text-gray-900">Otelversion</th>
        </tr>
        </thead>
        <tbody class="divide-y divide-gray-200 bg-white">
        <tr v-for="agent in agents" :key="agent.name">
          <td class="whitespace-nowrap py-4 pl-4 pr-3 text-sm font-medium text-gray-900 sm:pl-0">{{ agent.name }}</td>
          <td class="hidden whitespace-nowrap px-3 py-4 text-sm text-gray-500 sm:table-cell">
            <span :class="[statuses[agent.health], 'rounded-md px-2 py-1 text-xs font-medium ring-1 ring-inset']" >{{ agent.health }}</span>
          </td>
          <td class="hidden whitespace-nowrap px-3 py-4 text-sm text-gray-500 lg:table-cell">{{ agent.javaversion }}</td>
          <td class="whitespace-nowrap px-3 py-4 text-sm text-gray-500">{{ agent.otelversion }}</td>
          <td class="whitespace-nowrap py-4 pl-3 pr-4 text-right text-sm font-medium sm:pr-0">
<!--            <button @click="goToDetail(agent.name)">Details-->
<!--            </button>-->
            <span>
              <NuxtLink to='/agents/'>Details</NuxtLink>
             <p>{{ $route.params.name }}</p>
</span>
<!--            <a href="" class="text-indigo-600 hover:text-indigo-900"-->
<!--            >Details<span class="sr-only">, {{ agent.name }}</span></a-->
            >
          </td>
        </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script>
import {useAgentsStore} from "~/stores/agentstore.js";

const statuses = {
  alive: 'text-green-700 bg-green-50 ring-green-600/20',
  missed : 'text-red-700 bg-red-50 ring-red-600/10' }

export default {
  data() {
    return {
      agents: [],
      statuses,
    };
  },
  created() {
    this.fetchAgents();
  },

  methods: {

    async fetchAgents() {
      const store = useAgentsStore();
      store.clear()

      try {
        const response = await fetch('http://localhost:8080/api/v1/agents');
        const data = await response.json();
        data.forEach(agent => {
          store.addAgent(agent);
        });
      } catch (error) {
        console.error('Fehler beim Abrufen der Agenten:', error);
      }

      this.agents = store.agents.map(agent => ({
        // id: agent.id,
        name: agent.name,
        health: agent.healthState !== null ? agent.healthState : 'missed',
        javaversion: agent.javaversion,
        otelversion: agent.otelversion
      }));
    },


    // goToDetail(name) {
    //   this.$router.push(`/agents/${name}`);
    // },

  },
};

</script>