<script setup lang="ts">
import { useConfigurationStore } from '@/stores/configurationStore.ts';
import { Tabs } from '@/types/enums/Tabs.ts';
import { storeToRefs } from 'pinia';
import { useI18n } from 'vue-i18n';

const configurationStore = useConfigurationStore();
const { isInfo, currentTab, isConfirmation } = storeToRefs(configurationStore);

const { t } = useI18n();

defineProps<{
  size?: string | number;
}>();

const information = (): void => {
  currentTab.value = Tabs.Information;
  isInfo.value = true;
};

const share = (): void => {
  currentTab.value = Tabs.Share;
  isInfo.value = true;
};

const histories = (): void => {
  currentTab.value = Tabs.Histories;
  isInfo.value = true;
};

const exportOnNextloud = (): void => {};

const download = (): void => {};
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
      <v-list-item prepend-icon="fas fa-share-nodes" :title="t('menu.item.share')" rounded="xl" @click="share" />
      <v-list-item
        prepend-icon="fas fa-clock-rotate-left"
        :title="t('menu.item.histories')"
        rounded="xl"
        @click="histories"
      />
      <v-list-item
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
