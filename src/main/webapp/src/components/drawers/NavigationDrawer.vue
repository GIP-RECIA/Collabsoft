<script setup lang="ts">
import { useConfigurationStore } from '@/stores/configurationStore';
import { storeToRefs } from 'pinia';
import { useI18n } from 'vue-i18n';

const configurationStore = useConfigurationStore();
const { refresh } = configurationStore;
const { isSettings } = storeToRefs(configurationStore);

const { t } = useI18n();
</script>

<template>
  <v-navigation-drawer class="pa-2">
    <div class="d-flex flex-column h-100">
      <h1 class="text-center my-3">Collabsoft</h1>
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
          prepend-icon="fas fa-star"
          :title="t('navigation.item.favorites')"
          :to="{ name: 'favorites' }"
          rounded="xl"
          class="mb-2"
          @click="refresh()"
        />
        <v-list-item
          prepend-icon="fas fa-share-nodes"
          :title="t('navigation.item.shared')"
          :to="{ name: 'shared' }"
          rounded="xl"
          class="mb-2"
          @click="refresh()"
        />
        <v-list-item
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
