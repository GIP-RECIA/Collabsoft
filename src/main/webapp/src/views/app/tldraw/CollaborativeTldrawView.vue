<script setup lang="ts">
import { useAppStore } from '@/stores/appStore.ts';
import { useConfigurationStore } from '@/stores/configurationStore.ts';
import { AppSlug } from '@/types/enums/AppSlug.ts';
import { headObserver, styleObserver } from '@/utils/tldrawUtils.ts';
import { storeToRefs } from 'pinia';
import { onMounted, onUnmounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { useTheme } from 'vuetify';

const { VITE_API_URI } = import.meta.env;

const configurationStore = useConfigurationStore();
const { configuration } = configurationStore;

const appStore = useAppStore();
const { reset: resetApp } = appStore;
const { initFileId } = storeToRefs(appStore);

const route = useRoute();
const router = useRouter();
const theme = useTheme();

const roomId: string = route.params.roomId != undefined ? (route.params.roomId as string) : '';

onMounted(() => {
  styleObserver.observe(document.body, { attributes: true });
  headObserver.observe(document.head, { childList: true });
});

onUnmounted(() => {
  resetApp();
  styleObserver.disconnect();
  headObserver.disconnect();
  router.go(0); // force page reload to disconnect websocket
});
</script>

<template>
  <tldraw-multiplayer
    :websocket-api-url="configuration?.front.collaboration.websocketApiUrl"
    :room-id="`${roomId}-${AppSlug.tldraw}`"
    :init-url="initFileId ? `${VITE_API_URI}/api/file/${initFileId}` : ''"
    :user-info-api-url="configuration?.front.collaboration.userInfoApiUrl"
    :dark-mode="theme.global.name.value == 'dark'"
  />
</template>
