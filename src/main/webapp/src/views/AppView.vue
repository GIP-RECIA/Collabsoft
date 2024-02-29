<script setup lang="ts">
import FileMenu from '@/components/FileMenu.vue';
import InformationDrawer from '@/components/drawers/information/InformationDrawer.vue';
import { useAppStore } from '@/stores/appStore.ts';
import { useFileStore } from '@/stores/fileStore.ts';
import { useHomeStore } from '@/stores/homeStore.ts';
import { Navigation } from '@/types/enums/Navigation.ts';
import { Tabs } from '@/types/enums/Tabs.ts';
import { storeToRefs } from 'pinia';
import { onUnmounted, ref, watch } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';

const isDev = import.meta.env.DEV;

const appStore = useAppStore();
const { initAppContext, exitAppContext } = appStore;
const { isRoom, title, isAutoSave, canAutoSave, isRoomOwner } = storeToRefs(appStore);

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

const onAutoSave = (): void => {
  isAutoSave.value = !isAutoSave.value;
};

const goBack = (): void => {
  window.history.length > 2 ? router.back() : router.push({ name: Navigation.projects });
};

watch(
  () => route,
  () => {
    const { fileId, roomId } = route.params;
    initAppContext(roomId as string, fileId ? parseInt(fileId as string) : undefined, route.name as string);
  },
  { immediate: true, deep: true },
);

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
          <template #append>
            <div v-if="!isRoom">
              <v-tooltip :text="t(`menu.item.${isStarred ? 'unstar' : 'star'}`)" location="bottom center">
                <template v-slot:activator="{ props }">
                  <v-btn
                    v-bind="props"
                    v-if="isDev"
                    :icon="`${isStarred ? 'fas' : 'far'} fa-star`"
                    size="small"
                    @click="onStar"
                  />
                </template>
              </v-tooltip>
              <v-tooltip :text="t('menu.item.information')" location="bottom center">
                <template v-slot:activator="{ props }">
                  <v-btn v-bind="props" icon="fas fa-circle-info" size="small" @click="onInformation" />
                </template>
              </v-tooltip>
              <v-tooltip :text="t('menu.item.share')" location="bottom center">
                <template v-slot:activator="{ props }">
                  <v-btn v-bind="props" icon="fas fa-share-nodes" size="small" @click="onShare" />
                </template>
              </v-tooltip>
            </div>
            <div v-if="isRoomOwner && isDev">
              <v-tooltip
                v-if="canAutoSave"
                :text="t(`button.autoSave.${isAutoSave ? 'enabled' : 'disabled'}`)"
                location="bottom center"
              >
                <template v-slot:activator="{ props }">
                  <v-btn
                    v-bind="props"
                    icon="fas fa-arrows-rotate"
                    size="small"
                    :class="[isAutoSave ? '' : 'text-disabled']"
                    @click="onAutoSave"
                  />
                </template>
              </v-tooltip>
            </div>
            <file-menu :file-id="file?.id ?? -1" size="small" force-refresh />
          </template>
        </v-toolbar>
        <router-view />
      </div>
    </v-main>
    <information-drawer v-if="!isRoom" />
  </v-layout>
</template>
