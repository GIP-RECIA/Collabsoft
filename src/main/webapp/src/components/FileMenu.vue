<script setup lang="ts">
import { useConfigurationStore } from '@/stores/configurationStore.ts';
import { useFileStore } from '@/stores/fileStore.ts';
import { Tabs } from '@/types/enums/Tabs.ts';
import { downloadFileOrBlob } from '@/utils/fileUtils.ts';
import { saveOnNextcloud } from '@/utils/nextcloudUtils.ts';
import { storeToRefs } from 'pinia';
import { ref } from 'vue';
import { useI18n } from 'vue-i18n';

const isDev = import.meta.env.DEV;

const configurationStore = useConfigurationStore();
const { isApp, isInfo, currentTab, isConfirmation } = storeToRefs(configurationStore);

const fileStore = useFileStore();
const { loadFile } = fileStore;
const { currentFile } = storeToRefs(fileStore);

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
  currentTab.value = Tabs.Information;
  isInfo.value = true;
};

const onShare = async (): Promise<void> => {
  await getFile();
  currentTab.value = Tabs.Share;
  isInfo.value = true;
};

const onHistories = async (): Promise<void> => {
  await getFile();
  currentTab.value = Tabs.Histories;
  isInfo.value = true;
};

const onExport = async (): Promise<void> => {
  await getFile();
  if (!currentFile.value) return;
  await saveOnNextcloud(
    new File([currentFile.value.blob], currentFile.value.title, {
      type: `application/${currentFile.value.associatedApp.type};charset=utf-8`,
    }),
    currentFile.value.associatedApp.extension,
  );
};

const onDownload = async (): Promise<void> => {
  await getFile();
  if (!currentFile.value) return;
  downloadFileOrBlob(
    new File([currentFile.value.blob], currentFile.value.title, {
      type: `application/${currentFile.value.associatedApp.type};charset=utf-8`,
    }),
    `${currentFile.value.title}.${currentFile.value.associatedApp.extension}`,
  );
};

const onDelete = async (): Promise<void> => {
  await getFile();
  isConfirmation.value = true;
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
        v-if="isDev && !isApp"
        :prepend-icon="`${isStarred ? 'fas' : 'far'} fa-star`"
        :title="t(`menu.item.${isStarred ? 'unstar' : 'star'}`)"
        rounded="xl"
        @click="onStar"
      />
      <v-list-item
        v-if="!isApp"
        prepend-icon="fas fa-circle-info"
        :title="t('menu.item.information')"
        rounded="xl"
        @click="onInformation"
      />
      <v-list-item
        v-if="!isApp"
        prepend-icon="fas fa-share-nodes"
        :title="t('menu.item.share')"
        rounded="xl"
        @click="onShare"
      />
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
      <v-divider v-if="!isApp" class="my-2" />
      <v-list-item
        v-if="!isApp"
        prepend-icon="fas fa-trash"
        :title="t('menu.item.delete')"
        rounded="xl"
        base-color="error"
        @click="onDelete"
      />
    </v-list>
  </v-menu>
</template>
