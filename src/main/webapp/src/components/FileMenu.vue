<script setup lang="ts">
import { useConfigurationStore } from '@/stores/configurationStore';
import { storeToRefs } from 'pinia';
import { useI18n } from 'vue-i18n';

const configurationStore = useConfigurationStore();
const { isInfo, isConfirmation } = storeToRefs(configurationStore);

const { t } = useI18n();

const information = () => {
  isInfo.value = true;
};

const createHistory = () => {};

const exportOnNextloud = () => {};

const download = () => {};

const confirmDelete = () => {
  isConfirmation.value = true;
};
</script>

<template>
  <v-menu>
    <template #activator="{ props }">
      <v-btn v-bind="props" icon="fas fa-ellipsis-vertical" />
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
      <v-list-item prepend-icon="fas fa-trash" :title="t('menu.item.delete')" rounded="xl" @click="confirmDelete" />
    </v-list>
  </v-menu>
</template>
