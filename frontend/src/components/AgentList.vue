<template>
  <header>
    <div class="mx-auto px-12 sm:px-6 lg:px-8">
      <h1 class="text-3xl font-bold leading-tight tracking-tight text-gray-900">Übersicht Agenten</h1>
    </div>
  </header>
  <main class="py-6">
    <div class="px-16 sm:px-6 lg:px-8">
      <div class="px-4 sm:px-6 lg:px-8">
        <div class="sm:flex sm:items-center">
        </div>
        <div class="-mx-4 mt-8 sm:-mx-0">
          <table class="min-w-full divide-y divide-gray-300">
            <thead>
            <tr>
              <th scope="col" class="py-3.5 pl-4 pr-3 text-left text-sm font-semibold text-gray-900 sm:pl-0">Agentname
              </th>
              <th scope="col" class="hidden px-3 py-3.5 text-left text-sm font-semibold text-gray-900 sm:table-cell">
                Health
              </th>
              <th scope="col" class="hidden px-3 py-3.5 text-left text-sm font-semibold text-gray-900 lg:table-cell">
                Javaversion
              </th>
              <th scope="col" class="px-3 py-3.5 text-left text-sm font-semibold text-gray-900">Otelversion</th>
            </tr>
            </thead>
            <tbody class="divide-y divide-gray-200 bg-white">
            <tr v-for="agent in agents" :key="agent.serviceName">
              <td class="whitespace-nowrap py-4 pl-4 pr-3 text-sm font-medium text-gray-900 sm:pl-0">{{
                  agent.serviceName
                }}
              </td>
              <td class="hidden whitespace-nowrap px-3 py-4 text-sm text-gray-500 sm:table-cell">
            <span :class="[statuses[agent.health], 'rounded-md px-2 py-1 text-xs font-medium ring-1 ring-inset']">{{
                agent.health
              }}</span>
              </td>
              <td class="hidden whitespace-nowrap px-3 py-4 text-sm text-gray-500 lg:table-cell">{{
                  agent.javaVersion
                }}
              </td>
              <td class="whitespace-nowrap px-3 py-4 text-sm text-gray-500">{{ agent.otelVersion }}</td>
              <td class="whitespace-nowrap py-4 pl-3 pr-4 text-right text-sm font-medium sm:pr-0">
            <span>
               <NuxtLink :to="`/agents/${encodeURIComponent(agent.id)}`">Details > </NuxtLink>
            </span>
              </td>
            </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>
  </main>
</template>

<script>
import {useAgentsStore} from "~/stores/Agentstore.js";

const statuses = {
  alive: 'text-green-700 bg-green-50 ring-green-600/20',
  missed: 'text-red-700 bg-red-50 ring-red-600/10'
}

export default {
  props: {
    agentName: String,
  },
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
        const response = await fetch('https://localhost:8080/api/v1/connections');
        const data = await response.json();
        data.forEach(agent => {
          store.addAgent(agent);
        });
      } catch (error) {
        console.error('Fehler beim Abrufen der Agenten:', error);
      }

      this.agents = store.agents.map(agent => ({
        id: agent.id,
        serviceName: agent.serviceName,
        health: agent.healthState !== null ? agent.healthState : 'missed',
        javaVersion: agent.javaVersion,
        otelVersion: agent.otelVersion
      }));
    },
  },
};

</script>