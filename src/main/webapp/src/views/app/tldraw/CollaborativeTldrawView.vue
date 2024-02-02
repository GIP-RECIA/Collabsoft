<script setup lang="ts">
import { useCollaborativeStore } from '@/stores/collaborativeStore';
import { AppSlug } from '@/types/enums/AppSlug.ts';
import { storeToRefs } from 'pinia';
import { onMounted, onUnmounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { useTheme } from 'vuetify';

const { VITE_API_URI, VITE_WEBSOCKET_API_URL, VITE_USER_INFO_API_URI } = import.meta.env;

const collaborativeStore = useCollaborativeStore();
const { initFileId } = storeToRefs(collaborativeStore);

const route = useRoute();
const router = useRouter();
const theme = useTheme();

const roomId: string = route.params.roomId != undefined ? (route.params.roomId as string) : '';

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
  initFileId.value = undefined;
  styleObserver.disconnect();
  router.go(0); // force page reload to disconnect websocket
});
</script>

<template>
  <tldraw-multiplayer
    :websocket-api-url="VITE_WEBSOCKET_API_URL"
    :room-id="`${roomId}-${AppSlug.tldraw}`"
    :init-url="initFileId ? `${VITE_API_URI}/api/file/${initFileId}` : ''"
    :user-info-api-url="VITE_USER_INFO_API_URI"
    :dark-mode="theme.global.name.value == 'dark'"
  />
</template>
