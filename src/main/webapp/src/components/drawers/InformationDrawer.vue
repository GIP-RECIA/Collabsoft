<script setup lang="ts">
import { useConfigurationStore } from '@/stores/configurationStore';
import { format, parseISO } from 'date-fns';
import { storeToRefs } from 'pinia';
import { computed } from 'vue';
import { useI18n } from 'vue-i18n';

const configurationStore = useConfigurationStore();
const { isSelectedFile, isInfo } = storeToRefs(configurationStore);

const { t } = useI18n();

const modelValue = computed<boolean>({
  get() {
    return isSelectedFile.value && isInfo.value;
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
  <v-navigation-drawer v-model="modelValue" location="right" :width="350">
    <v-toolbar :title="t('navigation.title.information')" color="rgba(255, 255, 255, 0)">
      <template #append>
        <v-btn icon="fas fa-xmark" color="default" variant="plain" @click="modelValue = false" />
      </template>
    </v-toolbar>
    <div class="pa-2">
      <div class="ml-2 mb-2">{{ t('information.title') }}</div>
      <v-card variant="tonal" rounded="xl" class="mb-2">
        <v-card-text>
          {{ file.title }}
        </v-card-text>
      </v-card>
      <div class="ml-2 mb-2">{{ t('information.description') }}</div>
      <v-card variant="tonal" rounded="xl" class="mb-2">
        <v-card-text>
          {{ file.description ? file.description : '-' }}
        </v-card-text>
      </v-card>
      <div class="ml-2 mb-2">{{ t('information.creationDate') }} {{ format(parseISO(file.creationDate), 'Pp') }}</div>
      <div class="ml-2 mb-2">{{ t('information.editionDate') }} {{ format(parseISO(file.editionDate), 'Pp') }}</div>
      <v-divider class="my-2" />
      <div class="d-flex align-center">
        <div class="text-h6 mx-3">{{ t('navigation.title.histories') }}</div>
        <v-btn icon="fas fa-plus" color="default" variant="plain" size="small" />
      </div>
      <v-list class="pb-0">
        <v-list-item
          v-for="(history, index) in file.histories"
          :key="index"
          :title="format(parseISO(history.creationDate), 'Pp')"
          variant="tonal"
          rounded="xl"
          :class="[index < file.histories.length - 1 ? 'mb-2' : '', 'pr-1']"
        >
          <template #append>
            <v-btn icon="fas fa-eye" color="secondary" variant="text" size="small" />
            <v-btn icon="fas fa-clock-rotate-left" color="secondary" variant="text" size="small" />
            <v-btn icon="fas fa-trash" color="secondary" variant="text" size="small" />
          </template>
        </v-list-item>
      </v-list>
    </div>
  </v-navigation-drawer>
</template>
