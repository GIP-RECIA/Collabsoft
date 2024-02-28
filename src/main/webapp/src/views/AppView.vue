<script setup lang="ts">
import FileMenu from '@/components/FileMenu.vue';
import InformationDrawer from '@/components/drawers/information/InformationDrawer.vue';
import { useAppStore } from '@/stores/appStore.ts';
import { useFileStore } from '@/stores/fileStore.ts';
import { useHomeStore } from '@/stores/homeStore.ts';
import { Navigation } from '@/types/enums/Navigation.ts';
import { Tabs } from '@/types/enums/Tabs.ts';
import { storeToRefs } from 'pinia';
import { onUnmounted, ref, watchEffect } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';

const isDev = import.meta.env.DEV;

const appStore = useAppStore();
const { initAppContext, exitAppContext } = appStore;
const { isRoom, title } = storeToRefs(appStore);

const fileStore = useFileStore();
const { loadFile } = fileStore;
const { file } = storeToRefs(fileStore);

const homeStore = useHomeStore();
const { isDrawer, drawerTab } = storeToRefs(homeStore);

const route = useRoute();
const router = useRouter();
const { t } = useI18n();

const isStarred = ref(false);

const getFile = async (): Promise<void> => {
  if (file.value) await loadFile(file.value.id);
};

const onStar = (): void => {
  isStarred.value = !isStarred.value;
};

const onInformation = async (): Promise<void> => {
  await getFile();
  drawerTab.value = Tabs.Information;
  isDrawer.value = true;
};

const onShare = async (): Promise<void> => {
  await getFile();
  drawerTab.value = Tabs.Share;
  isDrawer.value = true;
};

const goBack = (): void => {
  window.history.length > 2 ? router.back() : router.push({ name: Navigation.projects });
};

watchEffect(() => {
  initAppContext(route.params.roomId as string, parseInt(route.params.fileId as string), route.name as string);
});

onUnmounted(() => {
  exitAppContext();
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
          <template v-if="file" #append>
            <v-btn
              v-if="isDev"
              :icon="`${isStarred ? 'fas' : 'far'} fa-star`"
              size="small"
              :alt="t(`menu.item.${isStarred ? 'unstar' : 'star'}`)"
              @click="onStar"
            />
            <v-btn icon="fas fa-circle-info" size="small" :alt="t('menu.item.information')" @click="onInformation" />
            <v-btn icon="fas fa-share-nodes" :alt="t('menu.item.share')" size="small" @click="onShare" />
            <file-menu :file-id="file.id" size="small" force-refresh />
          </template>
        </v-toolbar>
        <router-view />
      </div>
    </v-main>
    <information-drawer v-if="file" />
  </v-layout>
</template>
