<script setup lang="ts">
import { useFileStore } from '@/stores/fileStore.ts';
import { headObserver, styleObserver } from '@/utils/tldrawUtils.ts';
import { storeToRefs } from 'pinia';
import { onMounted, onUnmounted } from 'vue';
import { useTheme } from 'vuetify';

const { VITE_API_URI, VITE_USER_INFO_API_URI } = import.meta.env;

const fileStore = useFileStore();
const { currentFile } = storeToRefs(fileStore);

const theme = useTheme();

onMounted(() => {
  styleObserver.observe(document.body, { attributes: true });
  headObserver.observe(document.head, { childList: true });
});

onUnmounted(() => {
  styleObserver.disconnect();
  headObserver.disconnect();
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
