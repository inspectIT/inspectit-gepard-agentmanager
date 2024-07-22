<template>
  <div class="pt-10 flex items-center justify-end">

    <!-- Edit -->
    <div class="p-4">
      <button @click="openModal"
              class="rounded-full bg-white px-3 py-1.5 text-sm font-semibold text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 hover:bg-gray-50">
        <PencilSquareIcon class="h-5 w-5 text-gray-400" aria-hidden="true"/>
      </button>
      <EditModal
          :showModal="showModal"
          :text="text"
          @update:showModal="showModal = $event"
          @update:text="text = $event"
      />
    </div>

    <!-- Container für die rechten Buttons -->
    <div class="flex items-center space-x-2">
      <!-- Upload -->
      <div
          class="rounded-full bg-white px-3 py-1.5 text-sm font-semibold text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 hover:bg-gray-50">
        <label class="">
          <ArrowUpTrayIcon class="h-5 w-5 text-gray-400" aria-hidden="true"/>
          <input type='file' class="hidden" @change="handleFileUpload"/>
        </label>
      </div>

      <!-- Download -->
      <button type="button"
              class="rounded-full bg-white px-3 py-1.5 text-sm font-semibold text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 hover:bg-gray-50"
              @click="handleDownload">
        <ArrowDownTrayIcon class="h-5 w-5 text-gray-400" aria-hidden="true"/>
      </button>
    </div>
  </div>
</template>

<script setup>
import {ref} from 'vue'
import {ArrowDownTrayIcon, ArrowUpTrayIcon, PencilSquareIcon} from '@heroicons/vue/24/outline'
import {useFileStore} from '~/stores/fileStore';
import EditModal from "~/modals/EditModal.vue";

const file = ref(null)
const fileStore = useFileStore();
const showModal = ref(false);
const text = ref(fileStore.getFileContent())

const handleFileUpload = (event) => {
  file.value = event.target.files[0]
  if (file.value) {
    const reader = new FileReader()
    reader.onload = (e) => {
      fileStore.setFileContent(e.target.result);
    }
    reader.readAsText(file.value)
  }
}

const handleDownload = () => {
  console.log('Download wurde ausgeführt')
}

const openModal = () => {
  showModal.value = true;
};

// Watch for changes in fileStore.fileContent and update yamlContent
watch(
    () => fileStore.fileContent,
    (newContent) => {
      text.value = newContent;
    }
);
</script>
