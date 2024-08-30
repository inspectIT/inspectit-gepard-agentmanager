import { defineStore } from 'pinia';

export const useAgentsStore = defineStore({
    id: 'agents',
    state: () => ({
        agents: []
    }),
    getters: {},
    actions: {
        addAgent(agent) {
            this.agents.push(agent);
        },
        clear() {
            this.agents = []
        },
        getAgentById(id) {
            return this.agents.find(agent => agent.id === id);
        },
    }
});
