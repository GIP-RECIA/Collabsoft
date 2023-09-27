<script setup lang="ts">
import { getFile } from '@/services/fileService';
import { ref } from 'vue';
import { useRoute } from 'vue-router';

const route = useRoute();

const file = ref(undefined);

const getData = async () => {
  const { fileId } = route.params;
  const result = await getFile(parseInt(fileId as string));
  file.value = result.data;
};

getData();
</script>

<template>
  <div class="d-flex flex-column h-100">
    <v-toolbar :title="file?.title" density="compact">
      <template #prepend>
        <v-btn density="compact" icon :to="{ name: 'projects' }">
          <v-icon icon="fas fa-arrow-left" size="xs" />
        </v-btn>
      </template>
    </v-toolbar>
    {{ file }}
  </div>
</template>

<style scoped lang="scss"></style>
