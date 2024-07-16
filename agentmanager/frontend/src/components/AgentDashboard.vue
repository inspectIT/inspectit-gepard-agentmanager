<template>
  <div class="min-h-full">
    <Disclosure as="nav" class="border-b border-gray-200 bg-white" v-slot="{ open }">
      <div class="mx-auto max-w-7xl px-4 sm:px-6 lg:px-8">
        <div class="flex h-16 justify-between">
          <div class="flex">

            <!-- Navigation Bar -->
            <div class="hidden sm:-my-px sm:ml-6 sm:flex sm:space-x-8">
              <a v-for="item in navigation" :key="item.name" :href="item.href" :class="[item.current ? 'border-indigo-500 text-gray-900' : 'border-transparent text-gray-500 hover:border-gray-300 hover:text-gray-700', 'inline-flex items-center border-b-2 px-1 pt-1 text-sm font-medium']" :aria-current="item.current ? 'page' : undefined">{{ item.name }}</a>
            </div>
          </div>
        </div>
      </div>
    </Disclosure>

    <div class="py-10 space-y-4 ">
      <header>
        <div class="mx-auto max-w-7xl px-4 sm:px-6 lg:px-8">
          <h1 class="text-3xl font-bold leading-tight tracking-tight text-gray-900">Agents</h1>
        </div>
      </header>

      <main>
        <div class="mx-auto max-w-7xl sm:px-6 lg:px-8">
          <AgentList/>
        </div>
      </main>
    </div>
  </div>
</template>

<script setup>
import { Disclosure } from '@headlessui/vue'
import AgentList from "~/src/pages/AgentList.vue";


const navigation = [
  {name: 'Agent Overview', href: '#', current: true},
  {name: 'Configuration', href: '#', current: false},
  {name: 'Self Monitoring', href: '#', current: false},
  {name: 'Configuration Promotion', href: '#', current: false},
]

</script>