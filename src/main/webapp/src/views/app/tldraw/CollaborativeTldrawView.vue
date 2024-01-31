<script setup lang="ts">
import { onMounted, onUnmounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { useTheme } from 'vuetify';

const { VITE_WEBSOCKET_API_URL, VITE_USER_INFO_API_URI } = import.meta.env;

const route = useRoute();
const router = useRouter();
const theme = useTheme();

const joinCode: string = route.params.joinCode != undefined ? (route.params.joinCode as string) : '';

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
  router.go(0); // force page reload to disconnect websocket
});
</script>

<template>
  <tldraw-multiplayer
    :websocket-api-url="VITE_WEBSOCKET_API_URL"
    :room-id="joinCode"
    :user-info-api-url="VITE_USER_INFO_API_URI"
    :dark-mode="theme.global.name.value == 'dark'"
  />
</template>
