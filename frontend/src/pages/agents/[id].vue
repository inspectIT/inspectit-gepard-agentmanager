<template>
  <div>
    <SidebarHeader/>
    <main>
      <AgentDetailHeader/>
      <div class="mx-auto max-w-7xl sm:px-8 lg:px-8 py-16">
        <div class="mx-auto max-w-7xl px-4 sm:px-6 lg:px-8">
          <div
              class="px-8 mx-auto grid max-w-2xl grid-cols-1 grid-rows-1 items-start gap-x-8 gap-y-8 lg:mx-0 lg:max-w-none lg:grid-cols-3">
            <AgentSummary/>
            <YamlPreviewer/>
            <UpdateFeed/>
          </div>
          </div>
        </div>
    </main>
  </div>
</template>

<script>
import AgentList from "~/components/AgentList.vue";
import SidebarHeader from "~/components/SidebarHeader.vue";
import YamlPreviewer from "~/components/YamlPreviewer.vue";
import EditorLanguage from "~/components/ExternFileHandler.vue";
import {useRoute} from 'vue-router';
import {useAgentsStore} from '~/stores/Agentstore.js';

export default {
  name: 'AgentDetails',
  components: {
    EditorLanguage,
    Configuration: AgentList,
    SidebarHeader,
    YamlPreviewer,
  },
  setup() {
    const route = useRoute();
    const agentId = route.params.id;
    const agentsStore = useAgentsStore();

    const agent = agentsStore.getAgentById(agentId);

    return {
      agentId,
      agent,
    };
  },
};
</script>

