<script setup lang="ts">
import FileMenu from '@/components/FileMenu.vue';
import InformationDrawer from '@/components/drawers/InformationDrawer.vue';
import { useConfigurationStore } from '@/stores/configurationStore.ts';
import { Navigation } from '@/types/enums/Navigation.ts';
import { Tabs } from '@/types/enums/Tabs.ts';
import { storeToRefs } from 'pinia';
import { computed, onMounted, onUnmounted, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';

const configurationStore = useConfigurationStore();
const { loadFile } = configurationStore;
const { isApp, currentFile, isInfo, currentTab } = storeToRefs(configurationStore);

const isDev = import.meta.env.DEV;

const route = useRoute();
const router = useRouter();
const { t } = useI18n();

const isStarred = ref(false);

const getFile = async (): Promise<void> => {
  if (currentFile.value) await loadFile(currentFile.value.id);
};

const star = (): void => {
  isStarred.value = !isStarred.value;
};

const information = async (): Promise<void> => {
  await getFile();
  currentTab.value = Tabs.Information;
  isInfo.value = true;
};

const share = async (): Promise<void> => {
  await getFile();
  currentTab.value = Tabs.Share;
  isInfo.value = true;
};

const goBack = () => (window.history.length > 2 ? router.back() : router.push({ name: Navigation.projects }));

const title = computed<string>(() => {
  const joinCode: string = route.params.joinCode != undefined ? (route.params.joinCode as string) : '';

  return currentFile.value ? currentFile.value.title : joinCode;
});

onMounted(() => {
  isApp.value = true;
});

onUnmounted(() => {
  isApp.value = false;
});
</script>

<template>
  <v-layout full-height>
    <v-main>
      <div class="d-flex flex-column h-100">
        <v-toolbar :title="title" density="compact">
          <template #prepend>
            <v-btn icon="fas fa-arrow-left" size="small" @click="goBack" />
          </template>
          <template v-if="currentFile" #append>
            <v-btn
              v-if="isDev"
              :icon="`${isStarred ? 'fas' : 'far'} fa-star`"
              size="small"
              :alt="t(`menu.item.${isStarred ? 'unstar' : 'star'}`)"
              @click="star"
            />
            <v-btn icon="fas fa-circle-info" size="small" :alt="t('menu.item.information')" @click="information" />
            <v-btn v-if="isDev" icon="fas fa-share-nodes" :alt="t('menu.item.share')" size="small" @click="share" />
            <file-menu :file-id="currentFile.id" size="small" force-refresh />
          </template>
        </v-toolbar>
        <router-view />
      </div>
    </v-main>
    <information-drawer v-if="currentFile" />
  </v-layout>
</template>
