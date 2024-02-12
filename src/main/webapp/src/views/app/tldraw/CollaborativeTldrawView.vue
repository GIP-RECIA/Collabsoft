<script setup lang="ts">
import { useCollaborativeStore } from '@/stores/collaborativeStore.ts';
import { useConfigurationStore } from '@/stores/configurationStore.ts';
import { AppSlug } from '@/types/enums/AppSlug.ts';
import { headObserver, styleObserver } from '@/utils/tldrawUtils.ts';
import { storeToRefs } from 'pinia';
import { onMounted, onUnmounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { useTheme } from 'vuetify';

const { VITE_API_URI, VITE_USER_INFO_API_URI } = import.meta.env;

const configurationStore = useConfigurationStore();
const { configuration } = configurationStore;

const collaborativeStore = useCollaborativeStore();
const { initFileId } = storeToRefs(collaborativeStore);

const route = useRoute();
const router = useRouter();
const theme = useTheme();

const roomId: string = route.params.roomId != undefined ? (route.params.roomId as string) : '';

onMounted(() => {
  styleObserver.observe(document.body, { attributes: true });
  headObserver.observe(document.head, { childList: true });
});

onUnmounted(() => {
  initFileId.value = undefined;
  styleObserver.disconnect();
  headObserver.disconnect();
  router.go(0); // force page reload to disconnect websocket
});
</script>

<template>
  <tldraw-multiplayer
    :websocket-api-url="configuration?.front.websocket.url"
    :room-id="`${roomId}-${AppSlug.tldraw}`"
    :init-url="initFileId ? `${VITE_API_URI}/api/file/${initFileId}` : ''"
    :user-info-api-url="VITE_USER_INFO_API_URI"
    :dark-mode="theme.global.name.value == 'dark'"
  />
</template>
