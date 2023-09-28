<script setup lang="ts">
import FileMenu from '@/components/FileMenu.vue';
import InformationDrawer from '@/components/drawers/InformationDrawer.vue';
import { getFile } from '@/services/fileService';
import { useConfigurationStore } from '@/stores/configurationStore';
import { storeToRefs } from 'pinia';
import { ref } from 'vue';
import { useRoute } from 'vue-router';

const configurationStore = useConfigurationStore();
const { selectedFile, isShare } = storeToRefs(configurationStore);

const route = useRoute();

const file = ref(undefined);

const getData = async () => {
  const { fileId } = route.params;
  const result = await getFile(parseInt(fileId as string));
  file.value = result.data;
};

getData();

const isStarred = ref(false);

const star = () => {
  selectedFile.value = file.value.id;
  isStarred.value = !isStarred.value;
};

const share = () => {
  selectedFile.value = file.value.id;
  isShare.value = true;
};
</script>

<template>
  <v-layout full-height>
    <v-main>
      <div class="d-flex flex-column h-100">
        <v-toolbar :title="file?.title" density="compact">
          <template #prepend>
            <v-btn icon="fas fa-arrow-left" size="small" :to="{ name: 'projects' }" />
          </template>
          <template #append>
            <v-btn
              :icon="`${isStarred ? 'fas' : 'far'} fa-star`"
              :color="isStarred ? 'yellow' : 'default'"
              size="small"
              @click="star"
            />
            <v-btn icon="fas fa-share-nodes" size="small" @click="share" />
            <file-menu size="small" @click="selectedFile = file.id" />
          </template>
        </v-toolbar>
        {{ file }}
      </div>
    </v-main>
    <information-drawer />
  </v-layout>
</template>

<style scoped lang="scss"></style>
