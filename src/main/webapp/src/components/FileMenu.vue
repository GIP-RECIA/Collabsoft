<script setup lang="ts">
import { useConfigurationStore } from '@/stores/configurationStore';
import { Tabs } from '@/types/enums/Tabs';
import { storeToRefs } from 'pinia';
import { useI18n } from 'vue-i18n';

const configurationStore = useConfigurationStore();
const { isInfo, currentTab, isConfirmation } = storeToRefs(configurationStore);

const { t } = useI18n();

defineProps<{
  size?: string | number;
}>();

const information = () => {
  currentTab.value = Tabs.Information;
  isInfo.value = true;
};

const createHistory = () => {
  currentTab.value = Tabs.Histories;
  isInfo.value = true;
};

const exportOnNextloud = () => {};

const download = () => {};
</script>

<template>
  <v-menu>
    <template #activator="{ props }">
      <v-btn v-bind="props" icon="fas fa-ellipsis-vertical" :alt="t('button.options')" :size="size" />
    </template>

    <v-list rounded="xl" class="pa-2">
      <v-list-item
        prepend-icon="fas fa-circle-info"
        :title="t('menu.item.information')"
        rounded="xl"
        @click="information"
      />
      <v-list-item
        prepend-icon="fas fa-clock-rotate-left"
        :title="t('menu.item.createHistory')"
        rounded="xl"
        @click="createHistory"
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
        @click="isConfirmation = true"
      />
    </v-list>
  </v-menu>
</template>
