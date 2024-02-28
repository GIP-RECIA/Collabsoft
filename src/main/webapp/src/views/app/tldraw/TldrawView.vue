<script setup lang="ts">
import { useConfigurationStore } from '@/stores/configurationStore.ts';
import { useFileStore } from '@/stores/fileStore.ts';
import { headObserver, styleObserver } from '@/utils/tldrawUtils.ts';
import { storeToRefs } from 'pinia';
import { onMounted, onUnmounted } from 'vue';
import { useTheme } from 'vuetify';

const { VITE_API_URI } = import.meta.env;

const configurationStore = useConfigurationStore();
const { configuration } = configurationStore;

const fileStore = useFileStore();
const { file } = storeToRefs(fileStore);

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
    v-if="file"
    :persistance-api-url="`${VITE_API_URI}/api/file/${file.id}`"
    :assets-api-url="`${VITE_API_URI}/api/file/${file.id}/resource`"
    :user-info-api-url="configuration?.front.collaboration.userInfoApiUrl"
    :dark-mode="theme.global.name.value == 'dark'"
  />
</template>
