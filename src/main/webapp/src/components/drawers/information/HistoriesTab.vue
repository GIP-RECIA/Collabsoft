<script setup lang="ts">
import { useFileStore } from '@/stores/fileStore.ts';
import { format, parseISO } from 'date-fns';
import { storeToRefs } from 'pinia';
import { useI18n } from 'vue-i18n';

const fileStore = useFileStore();
const { currentFile } = storeToRefs(fileStore);

const { t } = useI18n();
</script>

<template>
  <v-list v-if="currentFile?.histories" class="py-0">
    <v-list-item
      v-for="(history, index) in currentFile.histories"
      :key="index"
      :title="format(parseISO(history.creationDate), 'Pp')"
      rounded="xl"
      :class="[
        index < currentFile.histories.length - 1 ? 'mb-2' : '',
        'pr-2',
        'bg-grey-lighten-3',
        'list-item--custom',
      ]"
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
