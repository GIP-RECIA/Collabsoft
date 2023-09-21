<script setup lang="ts">
import FilesLayout from '@/components/layouts/FilesLayout.vue';
import { getShared } from '@/services/fileService';
import { ref } from 'vue';

const files = ref(undefined);

const getData = async () => {
  const result = await getShared();
  files.value = result.data;
};

getData();
</script>

<template>
  <files-layout :files="files" />
</template>
