<script setup lang="ts">
import FileMenu from '@/components/FileMenu.vue';
import InformationDrawer from '@/components/drawers/InformationDrawer.vue';
import { useConfigurationStore } from '@/stores/configurationStore.ts';
import { AppSlug } from '@/types/enums/AppSlug.ts';
import { Navigation } from '@/types/enums/Navigation.ts';
import { Tabs } from '@/types/enums/Tabs.ts';
import { storeToRefs } from 'pinia';
import { onMounted, onUnmounted, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

const { VITE_API_URI, VITE_USER_INFO_API_URI } = import.meta.env;

const configurationStore = useConfigurationStore();
const { loadFile } = configurationStore;
const { currentFile, isInfo, currentTab } = storeToRefs(configurationStore);

const isDev = import.meta.env.DEV;

const route = useRoute();
const router = useRouter();

if (!currentFile.value || currentFile.value.id != (route.params.fileId as unknown as number))
  loadFile(parseInt(route.params.fileId as string));

const isStarred = ref(false);

const star = (): void => {
  isStarred.value = !isStarred.value;
};

const share = (): void => {
  if (currentFile.value) {
    loadFile(currentFile.value.id);
    currentTab.value = Tabs.Share;
    isInfo.value = true;
  }
};

const goBack = () => (window.history.length > 2 ? router.back() : router.push({ name: Navigation.projects }));

const styleObserver = new MutationObserver((mutationList: Array<MutationRecord>) => {
  for (const mutation of mutationList) {
    if (mutation.type === 'attributes' && mutation.attributeName === 'style') {
      document.body.removeAttribute('style');
    }
  }
});

onMounted(() => {
  styleObserver.observe(document.body, { attributes: true });
});

onUnmounted(() => {
  styleObserver.disconnect();
});
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
              v-if="isDev"
              :icon="`${isStarred ? 'fas' : 'far'} fa-star`"
              :color="isStarred ? 'yellow' : 'default'"
              size="small"
              @click="star"
            />
            <v-btn v-if="isDev" icon="fas fa-share-nodes" size="small" @click="share" />
            <file-menu :file-id="currentFile.id" size="small" force-refresh />
          </template>
        </v-toolbar>
        <tldraw-editor
          v-if="currentFile.associatedApp.slug == AppSlug.tldraw"
          :persistance-api-url="`${VITE_API_URI}/api/file/${currentFile.id}`"
          :user-info-api-url="VITE_USER_INFO_API_URI"
        />
      </div>
    </v-main>
    <information-drawer />
  </v-layout>
</template>
