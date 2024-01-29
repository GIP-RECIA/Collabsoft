<script setup lang="ts">
import { useFileStore } from '@/stores/fileStore.ts';
import { storeToRefs } from 'pinia';
import { onMounted, onUnmounted } from 'vue';
import { useRoute } from 'vue-router';
import { useTheme } from 'vuetify';

const fileStore = useFileStore();
const { loadFile } = fileStore;
const { currentFile } = storeToRefs(fileStore);

const { VITE_API_URI, VITE_USER_INFO_API_URI } = import.meta.env;

const route = useRoute();
const theme = useTheme();

if (!currentFile.value && route.params.fileId != undefined) loadFile(parseInt(route.params.fileId as string));

const styleObserver = new MutationObserver((mutationList: Array<MutationRecord>) => {
  for (const mutation of mutationList) {
    if (mutation.type === 'attributes' && mutation.attributeName === 'style') {
      document.body.removeAttribute('style');
    }
  }
});

onMounted(() => {
  styleObserver.observe(document.body, { attributes: true });
});

onUnmounted(() => {
  styleObserver.disconnect();
});
</script>

<template>
  <tldraw-singleplayer
    v-if="currentFile"
    :persistance-api-url="`${VITE_API_URI}/api/file/${currentFile.id}`"
    :assets-api-url="`${VITE_API_URI}/api/file/${currentFile.id}/resource`"
    :user-info-api-url="VITE_USER_INFO_API_URI"
    :dark-mode="theme.global.name.value == 'dark'"
  />
</template>
