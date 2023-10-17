<script setup lang="ts">
import FileMenu from '@/components/FileMenu.vue';
import InformationDrawer from '@/components/drawers/InformationDrawer.vue';
import { useConfigurationStore } from '@/stores/configurationStore';
import { Tabs } from '@/types/enums/Tabs';
import { storeToRefs } from 'pinia';
import { ref } from 'vue';
import { useRoute } from 'vue-router';

const configurationStore = useConfigurationStore();
const { loadFile } = configurationStore;
const { currentFile, selectedFile, isInfo, currentTab } = storeToRefs(configurationStore);

const route = useRoute();
if (!currentFile.value || currentFile.value.id != route.params.fileId)
  loadFile(parseInt(route.params.fileId as string));

const isStarred = ref(false);

const star = () => {
  selectedFile.value = currentFile.value.id;
  isStarred.value = !isStarred.value;
};

const share = () => {
  selectedFile.value = currentFile.value.id;
  currentTab.value = Tabs.Share;
  isInfo.value = true;
};
</script>

<template>
  <v-layout full-height>
    <v-main>
      <div class="d-flex flex-column h-100">
        <v-toolbar :title="currentFile.title" density="compact">
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
            <file-menu size="small" @click="selectedFile = currentFile.id" />
          </template>
        </v-toolbar>
        {{ currentFile }}
      </div>
    </v-main>
    <information-drawer />
  </v-layout>
</template>
