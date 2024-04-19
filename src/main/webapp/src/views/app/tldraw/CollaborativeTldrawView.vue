<script setup lang="ts">
import { useAppStore } from '@/stores/appStore.ts';
import { useConfigurationStore } from '@/stores/configurationStore.ts';
import { headObserver, styleObserver } from '@/utils/tldrawUtils.ts';
import { storeToRefs } from 'pinia';
import { computed, onMounted, onUnmounted } from 'vue';
import { useTheme } from 'vuetify';

const { VITE_API_URI } = import.meta.env;

const appStore = useAppStore();
const { isAutoSave, canAutoSave, roomId, room, isRoomOwner, initRoomFileId, destroy } = storeToRefs(appStore);

const configurationStore = useConfigurationStore();
const { configuration } = configurationStore;

const theme = useTheme();

const apiUrl = computed<string | undefined>(() =>
  room.value?.fileId ? `${VITE_API_URI}/api/file/${room.value.fileId}` : undefined,
);

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
  <tldraw-editor
    v-if="roomId"
    mode="multi"
    :persistance-api-url="apiUrl"
    :assets-api-url="apiUrl ? `${apiUrl}/resource` : undefined"
    :user-info-api-url="configuration?.front.collaboration.userInfoApiUrl"
    :dark-mode="theme.global.name.value == 'dark'"
    :auto-save="canAutoSave && isAutoSave"
    :open="isRoomOwner"
    :websocket-api-url="configuration?.front.collaboration.websocketApiUrl"
    :room-id="`${roomId}-tldraw`"
    :init-url="initRoomFileId ? `${VITE_API_URI}/api/file/${initRoomFileId}` : undefined"
    :owner="isRoomOwner"
    :clear-on-leave="true"
    :leave="destroy"
  />
</template>
