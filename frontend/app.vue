<template>
<!--<AgentDashboard/>-->
  <SidebarHeader/>
</template>

<script>

// app Vue instance
import AgentDashboard from "~/src/components/AgentDashboard.vue";
import AgentList from "~/src/components/AgentList.vue";
import api from "~/Api.js";
import SidebarHeader from "~/src/components/SidebarHeader.vue";

const Agents = {
  name: 'Agents',
  components: {SidebarHeader, AgentDashboard, AgentList},
  props: {
    activeUser: Object
  },

  // app initial state
  data: function () {
    return {
      agents: [],
      error: null,
    }
  },

  mounted() {
    api.getAll()
        .then(response => {
          this.agents = response.data
        })
        .catch(error => {
          this.$log.debug(error)
          this.error = 'Failed to load agents'
        })
        .finally(() => this.loading = false)
  },


  // computed properties
  // http://vuejs.org/guide/computed.html
  computed: {

  },


  // methods that implement data logic.
  // note there's no DOM manipulation here at all.
  methods: {
  },
}

export default Agents
</script>

<style>
[v-cloak] {
  display: none;
}
</style>
