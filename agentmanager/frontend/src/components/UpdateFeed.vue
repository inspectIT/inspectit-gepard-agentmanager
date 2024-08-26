<template>
  <div class="lg:col-start-3">
    <h2 class="text-sm font-semibold leading-6 text-gray-900">Activity</h2>
    <ul role="list" class="mt-6 space-y-6">
      <li v-for="(activityItem, activityItemIdx) in activity" :key="activityItem.id" class="relative flex gap-x-4">
        <div
            :class="[activityItemIdx === activity.length - 1 ? 'h-6' : '-bottom-6', 'absolute left-0 top-0 flex w-6 justify-center']">
          <div class="w-px bg-gray-200"/>
        </div>
        <div class="relative flex h-6 w-6 flex-none items-center justify-center bg-white">
          <div class="h-1.5 w-1.5 rounded-full bg-gray-100 ring-1 ring-gray-300"/>
        </div>
        <p class="flex-auto py-0.5 text-xs leading-5 text-gray-500">
          <span class="font-medium text-gray-900">{{ activityItem.person.name }}</span> {{ activityItem.type }} the
          invoice.
        </p>
        <time :datetime="activityItem.dateTime" class="flex-none py-0.5 text-xs leading-5 text-gray-500">
          {{ activityItem.date }}
        </time>
      </li>
    </ul>
  </div>
</template>

<script setup>

const activity = [
  {
    id: 1,
    person: {name: 'User 1', href: '#'},
    date: '6d ago',
  },
  {
    id: 2,
    person: {name: 'User 2', href: '#'},
    date: '2d ago',
  },
  {
    id: 3,
    person: {name: 'User 1', href: '#'},
    date: '6h ago',
  },
  {
    id: 4,
    person: {name: 'User 3', href: '#'},
    date: '2h ago',
  },
]
</script>