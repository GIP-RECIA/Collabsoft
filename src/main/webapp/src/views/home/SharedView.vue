<script setup lang="ts">
import FilesLayout from '@/components/layouts/FilesLayout.vue';
import { useConfigurationStore } from '@/stores/configurationStore.ts';
import { storeToRefs } from 'pinia';

const configurationStore = useConfigurationStore();
const { files } = storeToRefs(configurationStore);
</script>

<template>
  <files-layout :files="files" />
</template>
