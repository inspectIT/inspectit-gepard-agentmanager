<template>
  <div v-if="showModal" class="fixed inset-0 flex items-center justify-center z-50">
    <div class="absolute inset-0 bg-gray-900 opacity-50" @click="closeModal"></div>
    <div class="bg-white p-6 rounded-lg shadow-lg z-10">
      <textarea v-model="localText" class="w-full p-2 border rounded" rows="5"></textarea>
      <button @click="closeModal" class="mt-4 bg-blue-500 text-white px-4 py-2 rounded">
        Schließen
      </button>
    </div>
  </div>
</template>

<script setup>
import { ref, watch, defineEmits, defineProps } from 'vue';
import hljs from "highlight.js/lib/core";
import {useFileStore} from "~/stores/FileStore.js";

// Define props and emits
const props = defineProps({
  showModal: Boolean,
  text: String
});
const emit = defineEmits(['update:showModal', 'update:text']);

// Local reactivity
const localText = ref(useFileStore().getFileContent());
const showModal = ref(props.showModal);

const highlightCode = () => {
  if (codeElement.value) {
    // Reset the previously highlighted state
    codeElement.value.removeAttribute('data-highlighted');
    hljs.highlightElement(codeElement.value);
  }
};

// Watch for changes in props
watch(() => props.text, (newText) => {
  localText.value = newText;
});
watch(() => props.showModal, (newValue) => {
  showModal.value = newValue;
});

// Watch localText and emit updates
watch(localText, (newText) => {
  emit('update:text', newText);
});

// Define methods
const closeModal = () => {
  emit('update:showModal', false);
};

</script>

<style scoped>
/* Hier können zusätzliche Styles hinzugefügt werden */
</style>
