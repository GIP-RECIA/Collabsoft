<script setup lang="ts">
import { useAppStore } from '@/stores/appStore.ts';
import { useConfigurationStore } from '@/stores/configurationStore.ts';
import { useFileStore } from '@/stores/fileStore.ts';
import { useHomeStore } from '@/stores/homeStore.ts';
import { Tabs } from '@/types/enums/Tabs.ts';
import { downloadFileOrBlob, toFile } from '@/utils/fileUtils.ts';
import { saveOnNextcloud } from '@/utils/nextcloudUtils.ts';
import { storeToRefs } from 'pinia';
import { ref } from 'vue';
import { useI18n } from 'vue-i18n';

const isDev = import.meta.env.DEV;

const appStore = useAppStore();
const { isApp } = storeToRefs(appStore);

const configurationStore = useConfigurationStore();
const { isSettings } = storeToRefs(configurationStore);

const fileStore = useFileStore();
const { loadFile } = fileStore;
const { file } = storeToRefs(fileStore);

const homeStore = useHomeStore();
const { isDrawer, drawerTab, isDelete } = storeToRefs(homeStore);

const { t } = useI18n();

const props = defineProps<{
  fileId: number;
  size?: string | number;
  forceRefresh?: boolean;
}>();

const isStarred = ref(false);

const getFile = async (): Promise<void> => {
  await loadFile(props.fileId, props.forceRefresh);
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

const onHistories = async (): Promise<void> => {
  await getFile();
  drawerTab.value = Tabs.Histories;
  isDrawer.value = true;
};

const onExport = async (): Promise<void> => {
  await getFile();
  if (!file.value) return;
  await saveOnNextcloud(toFile(file.value), file.value.associatedApp.extension);
};

const onDownload = async (): Promise<void> => {
  await getFile();
  if (!file.value) return;
  downloadFileOrBlob(toFile(file.value), `${file.value.title}.${file.value.associatedApp.extension}`);
};

const onDelete = async (): Promise<void> => {
  await getFile();
  isDelete.value = true;
};
</script>

<template>
  <v-menu>
    <template #activator="{ props }">
      <v-btn
        v-bind="props"
        variant="text"
        color="default"
        icon="fas fa-ellipsis-vertical"
        :alt="t('button.options')"
        :size="size"
        class="text-medium-emphasis"
      />
    </template>

    <v-list rounded="xl" class="pa-2">
      <div v-if="!isApp">
        <v-list-item
          v-if="isDev"
          :prepend-icon="`${isStarred ? 'fas' : 'far'} fa-star`"
          :title="t(`menu.item.${isStarred ? 'unstar' : 'star'}`)"
          rounded="xl"
          @click="onStar"
        />
        <v-list-item
          prepend-icon="fas fa-circle-info"
          :title="t('menu.item.information')"
          rounded="xl"
          @click="onInformation"
        />
        <v-list-item prepend-icon="fas fa-share-nodes" :title="t('menu.item.share')" rounded="xl" @click="onShare" />
      </div>
      <div v-if="fileId > -1">
        <v-list-item
          v-if="isDev"
          prepend-icon="fas fa-clock-rotate-left"
          :title="t('menu.item.histories')"
          rounded="xl"
          @click="onHistories"
        />
        <v-list-item
          prepend-icon="fas fa-cloud"
          :title="t('menu.item.exportOnNextcloud')"
          rounded="xl"
          @click="onExport"
        />
        <v-list-item prepend-icon="fas fa-download" :title="t('menu.item.download')" rounded="xl" @click="onDownload" />
      </div>
      <v-divider v-if="fileId > -1" class="my-2" />
      <v-list-item
        v-if="!isApp"
        prepend-icon="fas fa-trash"
        :title="t('menu.item.delete')"
        rounded="xl"
        base-color="error"
        @click="onDelete"
      />
      <v-list-item
        v-if="isApp"
        prepend-icon="fas fa-gear"
        :title="t('navigation.item.settings')"
        rounded="xl"
        @click="isSettings = true"
      />
    </v-list>
  </v-menu>
</template>
