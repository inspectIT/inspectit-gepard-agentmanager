<template>
  <div class="ring-gray-900/5 sm:mx-0 lg:col-span-2 lg:row-span-2 ">
    <h2 class="text-3xl font-semibold leading-6 text-gray-900">Aktuelle Konfiguration</h2>
    <ExternFileHandler/>
    <pre>
        <code ref="codeElement" class="language-yaml">
          {{ yamlCode }}
        </code>
      </pre>
  </div>
</template>


<script>
import {onMounted} from 'vue';
import {useFileStore} from "~/stores/FileStore.js";
import hljs from 'highlight.js/lib/core';
import 'highlight.js/styles/monokai.css';
import yaml from 'highlight.js/lib/languages/yaml'

const highlightedCode = hljs.registerLanguage('yaml', yaml);
const fileStore = useFileStore();

export default {
  name: 'YamlPreviewer',

  setup() {
    const fileStore = useFileStore();
    const yamlCode = ref(fileStore.getFileContent());
    const codeElement = ref(null);

    const highlightCode = () => {
      if (codeElement.value) {
        // Reset the previously highlighted state
        codeElement.value.removeAttribute('data-highlighted');
        hljs.highlightElement(codeElement.value);
      }
    };

    onMounted(() => {
      yamlCode.value = '\n' + fileStore.fileContent;
      nextTick(() => {
        highlightCode();
      });
    });

    watch(
        () => fileStore.fileContent,
        (newContent) => {
          yamlCode.value = '\n' + newContent;
          nextTick(() => {
            highlightCode();
          });
        }
    );
    return {yamlCode, codeElement};
  }
};
</script>


<style scoped>
/* Optional: Weitere Styles, um das Aussehen der Komponente anzupassen */
</style>
