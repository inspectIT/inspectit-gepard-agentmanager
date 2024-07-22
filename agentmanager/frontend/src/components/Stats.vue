<template>
  <div class="bg-white">
    <div class="mx-auto max-w-7xl px-6 lg:px-8 mt-6">
      <div class="mx-auto max-w-2xl lg:max-w-none">
        <div class="relative text-center bg-logo-container">
          <h2 class="relative z-10 text-3xl font-bold tracking-tight text-gray-900 sm:text-4xl p-10">
            Admin Dashboard Inpectit Gepard
          </h2>
          <div class="absolute inset-0 bg-logo"></div>
        </div>
        <dl class="mt-6 mb-12 grid grid-cols-1 gap-0.5 overflow-hidden rounded-2xl text-center sm:grid-cols-2 lg:grid-cols-4">
          <div v-for="stat in stats" :key="stat.id" class="flex flex-col p-8">
            <dt class="text-sm font-semibold leading-6 text-gray-600">{{ stat.name }}</dt>
            <dd class="order-first text-3xl font-semibold tracking-tight text-gray-900">{{ stat.value }}</dd>
          </div>
        </dl>
      </div>
    </div>
  </div>
</template>

<script setup>
const stats = [
  { id: 1, name: 'Angemeldete Agenten', value: '842' },
  { id: 2, name: 'Redirection Rate ', value: '3%' },
  { id: 3, name: 'Uptime guarantee', value: '99.9%' },
  { id: 4, name: 'other', value: '42' },
]
</script>

<style scoped>
.text-center {
  position: relative;
}

.bg-logo-container {
  position: relative;
  display: inline-block;
  width: 100%;
  padding: 20px 0; /* Adjust padding as needed */
}

.bg-logo {
  background-image: url('/gepardLogo.png');
  background-repeat: no-repeat;
  background-position: center;
  background-size: cover; /* Ensures the image covers the entire area */
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  z-index: 0;
  opacity: 0.1; /* Adjust the opacity to make the text readable */
}

h2 {
  position: relative;
  z-index: 1;
  padding: 50px 20px; /* Adjust padding to make the h2 larger */
  font-size: 4rem; /* Increase the font size for the h2 text */
}
</style>
