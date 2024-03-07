<script setup lang="ts">
import FileMenu from '@/components/FileMenu.vue';
import ConfirmationDialog from '@/components/dialogs/ConfirmationDialog.vue';
import InformationDrawer from '@/components/drawers/information/InformationDrawer.vue';
import { useAppStore } from '@/stores/appStore.ts';
import { useFileStore } from '@/stores/fileStore.ts';
import { useHomeStore } from '@/stores/homeStore.ts';
import type { Confirmation } from '@/types/confirmationType.ts';
import { Navigation } from '@/types/enums/Navigation.ts';
import { Tabs } from '@/types/enums/Tabs.ts';
import { storeToRefs } from 'pinia';
import { computed, onMounted, onUnmounted, ref, watch } from 'vue';
import { useI18n } from 'vue-i18n';
import { onBeforeRouteLeave, useRoute, useRouter } from 'vue-router';

const isDev = import.meta.env.DEV;

const appStore = useAppStore();
const { destroyRoom, initAppContext, exitAppContext } = appStore;
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
  if (canAutoSave.value) isAutoSave.value = !isAutoSave.value;
};

const goBack = (): void => {
  window.history.length > 2 ? router.back() : router.push({ name: Navigation.projects });
};

const canLeave = ref<boolean>(false);

const exit = (): void => {
  canLeave.value = true;
  goBack();
  setTimeout(() => (canLeave.value = false), 200);
};

const confirmationLeave = ref<boolean>(false);

const confirmationLeaveDescription = computed<string>(() => {
  let info = 'app';
  if (isRoom.value) {
    info = 'room';
    if (isRoomOwner.value && canAutoSave.value) info += '.owner';
  }

  return `dialog.leave.${info}.description`;
});

const leave = (result: Confirmation): void => {
  confirmationLeave.value = false;

  const leaveApp = (): void => {
    exit();
  };

  const leaveRoom = (): void => {
    destroyRoom();
    setTimeout(() => exit(), 200);
  };

  if (result === 'yes') isRoom.value ? leaveRoom() : leaveApp();
};

watch(
  () => route,
  () => {
    const { fileId, roomId } = route.params;
    initAppContext(roomId as string, fileId ? parseInt(fileId as string) : undefined, route.name as string);
  },
  { immediate: true, deep: true },
);

onBeforeRouteLeave(() => {
  if (!canLeave.value) {
    if (!confirmationLeave.value) confirmationLeave.value = true;
    return false;
  } else return true;
});

const preventExit = (e: Event): void => {
  e.preventDefault();
};

watch(
  title,
  (newValue) => {
    if (newValue.trim() != '') document.title = `${newValue} - ${__APP_NAME__}`;
    else if (document.title != __APP_NAME__) document.title = __APP_NAME__;
  },
  { immediate: true },
);

onMounted(() => {
  window.addEventListener('beforeunload', preventExit);
});

onUnmounted(() => {
  if (document.title != __APP_NAME__) document.title = __APP_NAME__;
  window.removeEventListener('beforeunload', preventExit);
  exitAppContext();
});
</script>

<template>
  <v-layout full-height>
    <v-main>
      <div class="d-flex flex-column h-100">
        <v-toolbar :title="title" density="compact">
          <template #prepend>
            <v-btn icon="fas fa-arrow-left" size="small" @click="isRoom ? goBack() : exit()" />
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
                    class="text-medium-emphasis"
                    @click="onStar"
                  />
                </template>
              </v-tooltip>
              <v-tooltip :text="t('menu.item.information')" location="bottom center">
                <template v-slot:activator="{ props }">
                  <v-btn
                    v-bind="props"
                    icon="fas fa-circle-info"
                    size="small"
                    class="text-medium-emphasis"
                    @click="onInformation"
                  />
                </template>
              </v-tooltip>
              <v-tooltip :text="t('menu.item.share')" location="bottom center">
                <template v-slot:activator="{ props }">
                  <v-btn
                    v-bind="props"
                    icon="fas fa-share-nodes"
                    size="small"
                    class="text-medium-emphasis"
                    @click="onShare"
                  />
                </template>
              </v-tooltip>
            </div>
            <v-tooltip
              v-if="isRoomOwner && canAutoSave"
              :text="t(`button.autoSave.${isAutoSave ? 'enabled' : 'disabled'}`)"
              location="bottom center"
            >
              <template v-slot:activator="{ props }">
                <v-btn
                  v-bind="props"
                  icon="fas fa-arrows-rotate"
                  size="small"
                  :class="[isAutoSave ? 'text-medium-emphasis text-info' : 'text-disabled']"
                  @click="onAutoSave"
                />
              </template>
            </v-tooltip>
            <file-menu :file-id="file?.id ?? -1" size="small" force-refresh />
          </template>
        </v-toolbar>
        <router-view />
      </div>
    </v-main>
    <information-drawer v-if="!isRoom" />
    <confirmation-dialog
      v-model="confirmationLeave"
      :title="t(`dialog.leave.${isRoom ? 'room' : 'app'}.title`)"
      :description="t(confirmationLeaveDescription)"
      yes-value="button.leave"
      no-value="button.cancel"
      yes-color="error"
      no-color="secondary"
      @close="leave"
    />
  </v-layout>
</template>
