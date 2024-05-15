<script setup lang="ts">
import { useConfigurationStore } from '@/stores/configurationStore.ts';
import { useFileStore } from '@/stores/fileStore.ts';
import '@gip-recia/wisemapping-webcomponent';
import { storeToRefs } from 'pinia';
import { computed } from 'vue';

const isDev = import.meta.env.DEV;

const { VITE_API_URI } = import.meta.env;

const configurationStore = useConfigurationStore();
const { configuration } = configurationStore;

const fileStore = useFileStore();
const { file } = storeToRefs(fileStore);

const apiUrl = computed<string | undefined>(() => (file.value ? `${VITE_API_URI}/api/file` : undefined));
</script>

<template>
  <wisemapping-editor
    v-if="isDev && file"
    :persistance-api-url="apiUrl"
    :file-id="file.id"
    :user-info-api-url="configuration?.front.userInfoApiUrl"
    mode="edition-owner"
  />
</template>

<style lang="scss">
wisemapping-editor {
  display: block;
  position: relative;
  width: 100%;
  height: 100%;
  z-index: 0;
}
</style>
