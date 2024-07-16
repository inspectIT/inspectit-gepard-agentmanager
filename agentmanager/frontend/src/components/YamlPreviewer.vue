<template>
  <div class="ring-gray-900/5 sm:mx-0 lg:col-span-2 lg:row-span-2 ">
<!--    <h2 class="text-base font-semibold leading-6 text-gray-900">Konfiguration</h2>-->
    <div class="bg-gray-800 text-white p-4 rounded-lg shadow-md">
      <pre v-highlightjs="code" class="yaml">
<!--        <code ref="code" class="yaml">-->
          {{ code }}
<!--        </code>-->
      </pre>
    </div>
  </div>
</template>

<script>
import {onMounted, ref} from 'vue';
import hljs from 'highlight.js/lib/core';
import yaml from 'highlight.js/lib/languages/yaml';
import 'highlight.js/styles/monokai.css'; // Wähle einen anderen Stil nach Bedarf aus

hljs.registerLanguage('yaml', yaml);

export default {
  name: 'YamlPreviewer',
  props: {
    code: {
      type: String,
      required: true
    }
  },
  setup(props) {
    onMounted(() => {
      hljs.highlightAll();
    });

    return {};
  }
  // props: {
  //   code: {
  //     type: String,
  //     required: true
  //   }
  // },
  // setup(props, {emit}) {
  //   const codeElement = ref(null);
  //
  //   onMounted(() => {
  //     if (codeElement.value) {
  //       codeElement.value.textContent = props.code;
  //       hljs.highlightBlock(codeElement.value);
  //     }
  //   });
  //
  //   return {
  //     codeElement
  //   };
  // }
};
</script>

<style scoped>
/* Optional: Weitere Styles, um das Aussehen der Komponente anzupassen */
</style>
