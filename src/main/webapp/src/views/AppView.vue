<script setup lang="ts">
import FileMenu from '@/components/FileMenu.vue';
import InformationDrawer from '@/components/drawers/InformationDrawer.vue';
import { useConfigurationStore } from '@/stores/configurationStore.ts';
import { Navigation } from '@/types/enums/Navigation.ts';
import { Tabs } from '@/types/enums/Tabs.ts';
import { storeToRefs } from 'pinia';
import { ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

const configurationStore = useConfigurationStore();
const { loadFile } = configurationStore;
const { currentFile, isInfo, currentTab } = storeToRefs(configurationStore);

const route = useRoute();
const router = useRouter();

if (!currentFile.value || currentFile.value.id != (route.params.fileId as unknown as number))
  loadFile(parseInt(route.params.fileId as string));

const isStarred = ref(false);

const star = () => {
  isStarred.value = !isStarred.value;
};

const share = () => {
  if (currentFile.value) {
    loadFile(currentFile.value.id);
    currentTab.value = Tabs.Share;
    isInfo.value = true;
  }
};

const goBack = () => (window.history.length > 2 ? router.back() : router.push({ name: Navigation.projects }));
</script>

<template>
  <v-layout full-height>
    <v-main>
      <div v-if="currentFile" class="d-flex flex-column h-100">
        <v-toolbar :title="currentFile.title" density="compact">
          <template #prepend>
            <v-btn icon="fas fa-arrow-left" size="small" @click="goBack" />
          </template>
          <template #append>
            <v-btn
              :icon="`${isStarred ? 'fas' : 'far'} fa-star`"
              :color="isStarred ? 'yellow' : 'default'"
              size="small"
              @click="star"
            />
            <v-btn icon="fas fa-share-nodes" size="small" @click="share" />
            <file-menu size="small" @click="loadFile(currentFile.id)" />
          </template>
        </v-toolbar>
      </div>
    </v-main>
    <information-drawer />
  </v-layout>
</template>
