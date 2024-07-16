<!-- pages/agents/[name].vue -->
<template>
  <div>
    <h1>Agent Details</h1>
    <p>Name: {{ agentName }}</p>
  </div>
</template>


<script setup lang="ts">
import { useRoute } from 'vue-router';

const route = useRoute();
const agentName = route.params.name;

// When accessing /posts/1, route.params.id will be 1
console.log("name:", route.params.name)
</script>

<!--<script>-->
<!--//     itemName() {-->
<!--//       // `this.$route.params.id` gibt den Parameter aus der URL zurück (hier: die ID)-->
<!--//       // Hier solltest du die Logik implementieren, um den Namen anhand der ID zu finden-->
<!--//       console.log('this.$route.params', this.$route.params)-->
<!--//       const itemId = this.$route.params.name-->
<!--//       const item = this.items.find(item => item.id === Number(itemId))-->
<!--//       return item ? item.name : 'Unbekannt'-->
<!--//     }-->
<!--//   },-->
<!--//   data() {-->
<!--//     return {-->
<!--//       items: [-->
<!--//         { id: 1, name: 'Item 1' },-->
<!--//         { id: 2, name: 'Item 2' },-->
<!--//         { id: 3, name: 'Item 3' },-->
<!--//         // Weitere Elemente hier-->
<!--//       ]-->
<!--//     }-->
<!--//   }-->
<!--// }-->
<!--</script>-->

<!--<script>-->
<!--import { useAgentsStore } from '~/stores/agentstore.js'-->
<!--const store = useAgentsStore();-->



<!--export default {-->
<!--  // async asyncData({ params }) {-->
<!--  //   const agentname = params.name-->
<!--  //   agent = store.getAgentByName(agentname)-->
<!--  // };-->


<!--  // async asyncData({ params }) {-->
<!--  //   console.log('params', params)-->
<!--  //-->
<!--  //   const store = useAgentsStore();-->
<!--  //   // const agentname = params.name-->
<!--  //   const agent = store.getAgentByName(agentname)-->
<!--  //-->
<!--  //   return { agent }-->
<!--  //-->
<!--  //   // Hole die Agentendaten basierend auf der ID aus den params-->
<!--  //   // Hier wird ein Beispiel-Datenarray verwendet. In einer echten Anwendung würdest du die Daten von einer API holen.-->
<!--  //   // const agents = [-->
<!--  //   //   { id: 1, name: 'Egon' },-->
<!--  //   //   { id: 2, name: 'Pupsi' },-->
<!--  //   //   { id: 3, name: 'Erna' }-->
<!--  //   // ];-->
<!--  //   // const agentname = agents.find(agent => agent.name === params.name);-->
<!--  //   // return { agent };-->
<!--  // },-->
<!--  data() {-->
<!--    return {-->
<!--      agent: {}-->
<!--    };-->
<!--  }-->
<!--};-->

<!--</script>-->



<!--<template>-->
<!--  <div>-->
<!--    <h1>Agent Details</h1>-->
<!--    <p>ID: {{ agent.id }}</p>-->
<!--    <p>Name: {{ agent.name }}</p>-->
<!--    &lt;!&ndash; Füge weitere Details hinzu &ndash;&gt;-->
<!--  </div>-->

<!--</template>-->

<!--<script>-->
<!--import { useAgentsStore } from '~/stores/agents';-->

<!--export default {-->
<!--  async asyncData({ params }) {-->
<!--    console.log('params', params)-->

<!--    const store = useAgentsStore();-->
<!--    const agentname = params.name-->
<!--    const agent = store.getAgentByName(agentname)-->

<!--    return { agent }-->

<!--    // Hole die Agentendaten basierend auf der ID aus den params-->
<!--    // Hier wird ein Beispiel-Datenarray verwendet. In einer echten Anwendung würdest du die Daten von einer API holen.-->
<!--    // const agents = [-->
<!--    //   { id: 1, name: 'Egon' },-->
<!--    //   { id: 2, name: 'Pupsi' },-->
<!--    //   { id: 3, name: 'Erna' }-->
<!--    // ];-->
<!--    // const agentname = agents.find(agent => agent.name === params.name);-->
<!--    // return { agent };-->
<!--  },-->
<!--  data() {-->
<!--    return {-->
<!--      agent: {}-->
<!--    };-->
<!--  }-->
<!--};-->

<!--</script>-->

<!--<style scoped>-->

<!--</style>-->