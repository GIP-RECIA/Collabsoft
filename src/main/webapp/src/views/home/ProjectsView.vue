<script setup lang="ts">
import FilesLayout from '@/components/layouts/FilesLayout.vue';
import { useConfigurationStore } from '@/stores/configurationStore.ts';
import { storeToRefs } from 'pinia';

const configurationStore = useConfigurationStore();
const { files, isNew } = storeToRefs(configurationStore);
</script>

<template>
  <div class="pb-4">
    <v-btn icon="fas fa-plus" variant="tonal" @click="isNew = true" />
  </div>
  <files-layout :files="files" />
</template>
