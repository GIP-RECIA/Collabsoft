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
import { useFileStore } from '@/stores/fileStore.ts';
import { useI18n } from 'vue-i18n';
import { useDisplay } from 'vuetify';

const isDev = import.meta.env.DEV;

const fileStore = useFileStore();
const { refreshFiles } = fileStore;

const { t } = useI18n();
const { mobile } = useDisplay();
</script>

<template>
  <v-bottom-navigation v-if="mobile" :elevation="0" grow color="primary" bg-color="transparent" class="px-2 pb-2">
    <v-btn :to="{ name: 'projects' }" rounded="xl" class="text-medium-emphasis" @click="refreshFiles">
      <v-icon icon="fas fa-folder" />
      {{ t('navigation.item.projects') }}
    </v-btn>
    <v-btn v-if="isDev" :to="{ name: 'favorites' }" rounded="xl" class="text-medium-emphasis" @click="refreshFiles">
      <v-icon icon="fas fa-star" />
      {{ t('navigation.item.favorites') }}
    </v-btn>
    <v-btn v-if="isDev" :to="{ name: 'shared' }" rounded="xl" class="text-medium-emphasis" @click="refreshFiles">
      <v-icon icon="fas fa-share-nodes" />
      {{ t('navigation.item.shared') }}
    </v-btn>
    <v-btn v-if="isDev" :to="{ name: 'public' }" rounded="xl" class="text-medium-emphasis" @click="refreshFiles">
      <v-icon icon="fas fa-globe" />
      {{ t('navigation.item.public') }}
    </v-btn>
  </v-bottom-navigation>
</template>
