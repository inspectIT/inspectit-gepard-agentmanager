<template>
  <SidebarHeader/>
  <Stats/>
  <div class="min-h-full">
    <div class="py-10 space-y-4 ">
      <main>
        <div class="mx-auto max-w-7xl sm:px-6 lg:px-8">
          <AgentList/>
        </div>
      </main>
    </div>
  </div>
</template>

<script setup>
import AgentList from "~/components/AgentList.vue";
</script>