<script setup lang="ts">
import FileCard from '@/components/FileCard.vue';

defineProps<{
  files: Array<any> | undefined;
  list?: boolean;
}>();
</script>

<template>
  <v-row class="mb-1">
    <v-col
      v-for="(file, index) in files"
      :key="index"
      :cols="12"
      :sm="6"
      :md="4"
      :lg="3"
      :xxl="2"
      class="d-flex align-center pa-2"
    >
      <file-card :file="file" />
    </v-col>
  </v-row>
</template>
