<script setup lang="ts">
import { useConfigurationStore } from '@/stores/configurationStore.ts';
import { useFileStore } from '@/stores/fileStore.ts';
import { storeToRefs } from 'pinia';
import { useI18n } from 'vue-i18n';

const isDev = import.meta.env.DEV;

const configurationStore = useConfigurationStore();
const { isSettings } = storeToRefs(configurationStore);

const fileStore = useFileStore();
const { refresh } = fileStore;

const { t } = useI18n();

const appName = __APP_NAME__;
</script>

<template>
  <v-navigation-drawer class="pa-2">
    <div class="d-flex flex-column h-100">
      <h1 class="text-center my-3">{{ appName }}</h1>
      <v-list color="primary">
        <v-list-item
          prepend-icon="fas fa-folder"
          :title="t('navigation.item.projects')"
          :to="{ name: 'projects' }"
          rounded="xl"
          class="mb-2"
          @click="refresh()"
        />
        <v-list-item
          v-if="isDev"
          prepend-icon="fas fa-star"
          :title="t('navigation.item.favorites')"
          :to="{ name: 'favorites' }"
          rounded="xl"
          class="mb-2"
          @click="refresh()"
        />
        <v-list-item
          v-if="isDev"
          prepend-icon="fas fa-share-nodes"
          :title="t('navigation.item.shared')"
          :to="{ name: 'shared' }"
          rounded="xl"
          class="mb-2"
          @click="refresh()"
        />
        <v-list-item
          v-if="isDev"
          prepend-icon="fas fa-globe"
          :title="t('navigation.item.public')"
          :to="{ name: 'public' }"
          rounded="xl"
          class="mb-2"
          @click="refresh()"
        />
      </v-list>
      <div class="flex-grow-1"></div>
      <v-list class="py-0">
        <v-list-item
          prepend-icon="fas fa-gear"
          :title="t('navigation.item.settings')"
          rounded="xl"
          @click="isSettings = true"
        />
      </v-list>
    </div>
  </v-navigation-drawer>
</template>
