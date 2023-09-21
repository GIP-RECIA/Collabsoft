<script setup lang="ts">
import FileCard from '@/components/FileCard.vue';

defineProps<{
  files: Array<any> | undefined;
  list?: boolean;
}>();
</script>

<template>
  <v-row>
    <v-col
      v-for="(file, index) in files"
      :key="index"
      :cols="12"
      :md="6"
      :lg="4"
      :xxl="3"
      class="d-flex align-center pa-2"
    >
      <file-card :file="file" />
    </v-col>
  </v-row>
</template>

<style scoped lang="scss"></style>
