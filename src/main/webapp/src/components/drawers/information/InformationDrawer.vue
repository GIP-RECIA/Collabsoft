<script setup lang="ts">
import ShareInRoomDialog from '@/components/dialogs/ShareInRoomDialog.vue';
import HistoriesTab from '@/components/drawers/information/tabs/HistoriesTab.vue';
import InformationTab from '@/components/drawers/information/tabs/InformationTab.vue';
import ShareTab from '@/components/drawers/information/tabs/ShareTab.vue';
import { useFileStore } from '@/stores/fileStore.ts';
import { useHomeStore } from '@/stores/homeStore.ts';
import { Tabs } from '@/types/enums/Tabs.ts';
import { dateToDuration } from '@/utils/dateFnsUtils.ts';
import { storeToRefs } from 'pinia';
import { computed } from 'vue';
import { useI18n } from 'vue-i18n';

const { VITE_API_URI } = import.meta.env;

const isDev = import.meta.env.DEV;

const fileStore = useFileStore();
const { file } = storeToRefs(fileStore);

const homeStore = useHomeStore();
const { isDrawer, drawerTab } = storeToRefs(homeStore);

const { t } = useI18n();

const modelValue = computed<boolean>({
  get() {
    return file.value != undefined && isDrawer.value;
  },
  set() {
    isDrawer.value = false;
  },
});

const onClose = (): void => {
  modelValue.value = false;
};
</script>

<template>
  <v-navigation-drawer v-model="modelValue" location="right" :width="460">
    <v-toolbar :title="t(`navigation.title.${drawerTab}`)" color="rgba(255, 255, 255, 0)">
      <template #title v-if="file">
        <div class="d-flex align-center">
          <v-avatar
            icon="fas fa-file"
            :image="
              file.associatedApp.iconPath != null
                ? `${VITE_API_URI}${VITE_API_URI.endsWith('/') ? '' : '/'}${file.associatedApp.iconPath}`
                : undefined
            "
            class="me-2"
          />
          <div class="overflow-hidden">
            <div class="text-body-1 font-weight-bold text-truncated">
              {{ file.title }}
            </div>
            <div class="text-caption text-medium-emphasis">
              {{ t('information.duration', { duration: dateToDuration(file.editionDate) }) }}
            </div>
          </div>
        </div>
      </template>
      <template #append>
        <v-btn icon="fas fa-xmark" color="default" variant="plain" :alt="t('button.close')" @click="onClose" />
      </template>
    </v-toolbar>

    <v-tabs
      v-model="drawerTab"
      align-tabs="center"
      :show-arrows="false"
      hide-slider
      selected-class="slide-group-item--activate"
      fixed-tabs
      class="mx-2"
    >
      <v-tab :value="Tabs.Information" :title="t('navigation.title.information')">
        <v-icon icon="fas fa-circle-info" />
      </v-tab>
      <v-tab :value="Tabs.Share" :title="t('navigation.title.share')">
        <v-icon icon="fas fa-share-nodes" />
      </v-tab>
      <v-tab v-if="isDev" :value="Tabs.Histories" :title="t('navigation.title.histories')">
        <v-icon icon="fas fa-clock-rotate-left" />
      </v-tab>
    </v-tabs>
    <v-window v-model="drawerTab" class="pa-2">
      <v-window-item :value="Tabs.Information">
        <information-tab />
      </v-window-item>
      <v-window-item :value="Tabs.Share">
        <share-tab />
      </v-window-item>
      <v-window-item v-if="isDev" :value="Tabs.Histories">
        <histories-tab />
      </v-window-item>
    </v-window>
  </v-navigation-drawer>

  <share-in-room-dialog />
</template>

<style scoped lang="scss">
.slide-group-item--activate {
  background-color: rgba(var(--v-theme-primary), var(--v-activated-opacity)) !important;
}

.share-item--name {
  width: 100%;
}

.share-item--role {
  min-width: 150px;
}

.list-item--custom {
  height: 56px;
}
</style>
