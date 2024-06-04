<script setup lang="ts">
import { useFileStore } from '@/stores/fileStore.ts';
import { format } from 'date-fns';
import { storeToRefs } from 'pinia';
import { useI18n } from 'vue-i18n';

const fileStore = useFileStore();
const { file } = storeToRefs(fileStore);

const { t } = useI18n();
</script>

<template>
  <v-list v-if="file?.histories" class="py-0">
    <v-list-item
      v-for="(history, index) in file.histories"
      :key="index"
      :title="format(history.creationDate, 'Pp')"
      rounded="xl"
      class="pe-2 bg-grey-lighten-3 list-item--custom"
      :class="[index < file.histories.length - 1 ? 'mb-2' : '']"
    >
      <template #append>
        <v-btn icon="fas fa-eye" color="info" variant="text" size="small" :alt="t('button.view')" />
        <v-btn
          icon="fas fa-clock-rotate-left"
          color="secondary"
          variant="text"
          size="small"
          :alt="t('button.revert')"
        />
        <v-btn icon="fas fa-trash" color="error" variant="text" size="small" :alt="t('button.delete')" />
      </template>
    </v-list-item>
  </v-list>
</template>
