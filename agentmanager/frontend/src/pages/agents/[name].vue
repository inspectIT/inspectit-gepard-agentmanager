<template>
  <div>
    <SidebarHeader/>
    <main>
      <AgentDetailHeader/>
      <div class="mx-auto max-w-7xl sm:px-6 lg:px-8 py-16 ">
        <div class="mx-auto max-w-7xl px-4 py-16 sm:px-6 lg:px-8">
          <div class="mx-auto grid max-w-2xl grid-cols-1 grid-rows-1 items-start gap-x-8 gap-y-8 lg:mx-0 lg:max-w-none lg:grid-cols-3">
            <AgentSummary/>
            <YamlPreviewer :code="yamlCode"/>
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

export default {
  name: 'AgentDetails',
  computed: {},
  components: {
    Configuration:
    AgentList,
    SidebarHeader,
    YamlPreviewer,
  },
  setup() {
    const route = useRoute();
    const agentName = route.params.name;
    const agentsStore = useAgentsStore();

    const agent = agentsStore.getAgentByName(agentName);


    // Beispielhafter yamlCode
    const yamlCode = `
  apiVersion: apps/v1
  kind: Deployment
  metadata:
    name: nginx-deployment
    labels:
      app: nginx
  spec:
    replicas: 3
    selector:
      matchLabels:
        app: nginx
    template:
      metadata:
        labels:
          app: nginx
      spec:
        containers:
          - name: nginx
            image: nginx:1.14.2
            ports:
              - containerPort: 80
            volumeMounts:
              - name: nginx-persistent-storage
                mountPath: /var/www/html
        volumes:
          - name: nginx-persistent-storage
            emptyDir: {}
  ---
  apiVersion: v1
  kind: Service
  metadata:
    name: nginx-service
  spec:
    selector:
      app: nginx
    ports:
      - protocol: TCP
        port: 80
        targetPort: 80
    type: LoadBalancer
    `;


    return {
      agentName,
      agent,
      yamlCode,
    };
  },
};
</script>