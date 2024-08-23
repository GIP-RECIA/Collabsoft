<!--
 Copyright (C) 2023 GIP-RECIA, Inc.

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
-->

<script setup lang="ts">
import { useFileStore } from '@/stores/index.ts';
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
