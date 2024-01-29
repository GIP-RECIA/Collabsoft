<script setup lang="ts">
import { useConfigurationStore } from '@/stores/configurationStore.ts';
import { watchOnce } from '@vueuse/core';
import { storeToRefs } from 'pinia';
import { onMounted, onUnmounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { useTheme } from 'vuetify';

const configurationStore = useConfigurationStore();
const { isWsOk } = storeToRefs(configurationStore);

const { VITE_WEBSOCKET_API_URL, VITE_USER_INFO_API_URI } = import.meta.env;

const route = useRoute();
const router = useRouter();
const theme = useTheme();

const joinCode: string = route.params.joinCode != undefined ? (route.params.joinCode as string) : '';

watchOnce(
  isWsOk,
  () => {
    router.go(0);
  },
  { immediate: true },
);

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
  isWsOk.value = false;
});
</script>

<template>
  <tldraw-multiplayer
    v-if="isWsOk"
    :websocket-api-url="VITE_WEBSOCKET_API_URL"
    :room-id="joinCode"
    :user-info-api-url="VITE_USER_INFO_API_URI"
    :dark-mode="theme.global.name.value == 'dark'"
  />
</template>
