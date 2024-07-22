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
        getAgentByName(name) {
            return this.agents.find(agent => agent.name === name);
        },
    }
});
