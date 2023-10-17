<script setup lang="ts">
import { useConfigurationStore } from '@/stores/configurationStore';
import { Tabs } from '@/types/enums/Tabs';
import { format, parseISO } from 'date-fns';
import { storeToRefs } from 'pinia';
import { computed } from 'vue';
import { useI18n } from 'vue-i18n';

const configurationStore = useConfigurationStore();
const { currentFile, isInfo, currentTab } = storeToRefs(configurationStore);

const { t } = useI18n();

const modelValue = computed<boolean>({
  get() {
    return currentFile.value && isInfo.value;
  },
  set() {
    isInfo.value = false;
  },
});

const file = {
  title: 'tldraw test 1',
  description: '',
  creationDate: '2023-09-19 16:45:00.000',
  editionDate: '2023-09-19 16:45:00.000',
  pub: false,
  histories: [
    {
      id: 1,
      creationDate: '2023-09-19 16:00:00.000',
    },
    {
      id: 2,
      creationDate: '2023-09-19 16:30:00.000',
    },
    {
      id: 3,
      creationDate: '2023-09-19 16:45:00.000',
    },
  ],
};
</script>

<template>
  <v-navigation-drawer v-model="modelValue" location="right" :width="460">
    <v-toolbar :title="t('navigation.title.information')" color="rgba(255, 255, 255, 0)">
      <template #append>
        <v-btn
          icon="fas fa-xmark"
          color="default"
          variant="plain"
          :alt="t('button.close')"
          @click="modelValue = false"
        />
      </template>
    </v-toolbar>
    <v-tabs
      v-model="currentTab"
      align-tabs="center"
      :show-arrows="false"
      hide-slider
      selected-class="slide-group-item--activate"
      class="mx-2"
      fixed-tabs
    >
      <v-tab :value="Tabs.Information" :title="t('navigation.title.information')">
        <v-icon icon="fas fa-circle-info" />
      </v-tab>
      <v-tab :value="Tabs.Share" :title="t('navigation.title.share')">
        <v-icon icon="fas fa-share-nodes" />
      </v-tab>
      <v-tab :value="Tabs.Histories" :title="t('navigation.title.histories')">
        <v-icon icon="fas fa-clock-rotate-left" />
      </v-tab>
    </v-tabs>
    <v-window v-model="currentTab" class="pa-2">
      <v-window-item :value="Tabs.Information">
        <v-text-field
          v-model="file.title"
          :label="t('information.title')"
          variant="solo"
          bg-color="grey-lighten-3"
          rounded="xl"
          class="mb-2"
          flat
          hide-details
          disabled
        />
        <v-textarea
          v-model="file.description"
          :label="t('information.description')"
          variant="solo"
          bg-color="grey-lighten-3"
          rounded="xl"
          class="mb-2"
          flat
          hide-details
          disabled
        />
        <div class="ml-2 mb-2">
          {{ t('information.visibility') }} : {{ t(`visibility.${file.pub ? 'public' : 'private'}`) }}
        </div>
        <div class="ml-2 mb-2">{{ t('information.creationDate') }} {{ format(parseISO(file.creationDate), 'Pp') }}</div>
        <div class="ml-2 mb-2">{{ t('information.editionDate') }} {{ format(parseISO(file.editionDate), 'Pp') }}</div>
      </v-window-item>

      <v-window-item :value="Tabs.Share"></v-window-item>

      <v-window-item :value="Tabs.Histories">
        <v-list class="pb-0">
          <v-list-item
            v-for="(history, index) in file.histories"
            :key="index"
            :title="format(parseISO(history.creationDate), 'Pp')"
            rounded="xl"
            :class="[index < file.histories.length - 1 ? 'mb-2' : '', 'pr-1', 'bg-grey-lighten-3']"
          >
            <template #append>
              <v-btn icon="fas fa-eye" color="secondary" variant="text" size="small" :alt="t('button.view')" />
              <v-btn
                icon="fas fa-clock-rotate-left"
                color="secondary"
                variant="text"
                size="small"
                :alt="t('button.revert')"
              />
              <v-btn icon="fas fa-trash" color="secondary" variant="text" size="small" :alt="t('button.delete')" />
            </template>
          </v-list-item>
        </v-list>
      </v-window-item>
    </v-window>
  </v-navigation-drawer>
</template>

<style scoped lang="scss">
.slide-group-item--activate {
  background-color: rgba(var(--v-theme-primary), var(--v-activated-opacity)) !important;
}
</style>
