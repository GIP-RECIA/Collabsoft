<script setup lang="ts">
import { useConfigurationStore } from '@/stores/configurationStore.ts';
import { Tabs } from '@/types/enums/Tabs.ts';
import { downloadFileOrBlob } from '@/utils/fileUtils.ts';
import { storeToRefs } from 'pinia';
import { useI18n } from 'vue-i18n';

const configurationStore = useConfigurationStore();
const { loadFile } = configurationStore;
const { currentFile, isInfo, currentTab, isConfirmation } = storeToRefs(configurationStore);

const isDev = import.meta.env.DEV;

const { t } = useI18n();

const props = defineProps<{
  fileId?: number;
  size?: string | number;
}>();

const getFile = async (): Promise<void> => {
  if (!props.fileId) return;
  await loadFile(props.fileId);
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

const histories = async (): Promise<void> => {
  await getFile();
  currentTab.value = Tabs.Histories;
  isInfo.value = true;
};

const exportOnNextloud = async (): Promise<void> => {
  await getFile();
};

const download = async (): Promise<void> => {
  await getFile();
  if (!currentFile.value) return;
  downloadFileOrBlob(
    new File([currentFile.value.blob], currentFile.value.title, {
      type: `application/${currentFile.value.associatedApp.type};charset=utf-8`,
    }),
    `${currentFile.value.title}.${currentFile.value.associatedApp.extension}`,
  );
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
      />
    </template>

    <v-list rounded="xl" class="pa-2">
      <v-list-item
        prepend-icon="fas fa-circle-info"
        :title="t('menu.item.information')"
        rounded="xl"
        @click="information"
      />
      <v-list-item
        v-if="isDev"
        prepend-icon="fas fa-share-nodes"
        :title="t('menu.item.share')"
        rounded="xl"
        @click="share"
      />
      <v-list-item
        v-if="isDev"
        prepend-icon="fas fa-clock-rotate-left"
        :title="t('menu.item.histories')"
        rounded="xl"
        @click="histories"
      />
      <v-list-item
        v-if="isDev"
        prepend-icon="fas fa-cloud"
        :title="t('menu.item.exportOnNextcloud')"
        rounded="xl"
        @click="exportOnNextloud"
      />
      <v-list-item prepend-icon="fas fa-download" :title="t('menu.item.download')" rounded="xl" @click="download" />
      <v-divider class="my-2" />
      <v-list-item
        prepend-icon="fas fa-trash"
        :title="t('menu.item.delete')"
        rounded="xl"
        base-color="error"
        @click="isConfirmation = true"
      />
    </v-list>
  </v-menu>
</template>
