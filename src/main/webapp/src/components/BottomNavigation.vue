<script setup lang="ts">
import { useConfigurationStore } from '@/stores/configurationStore';
import { useI18n } from 'vue-i18n';
import { useDisplay } from 'vuetify';

const configurationStore = useConfigurationStore();
const { refresh } = configurationStore;

const { mobile } = useDisplay();
const { t } = useI18n();
</script>

<template>
  <v-bottom-navigation v-if="mobile" grow color="primary" class="pa-2">
    <v-btn :to="{ name: 'projects' }" rounded="xl" @click="refresh">
      <v-icon icon="fas fa-folder" />
      {{ t('navigation.item.projects') }}
    </v-btn>
    <v-btn :to="{ name: 'favorites' }" rounded="xl" @click="refresh">
      <v-icon icon="fas fa-star" />
      {{ t('navigation.item.favorites') }}
    </v-btn>
    <v-btn :to="{ name: 'shared' }" rounded="xl" @click="refresh">
      <v-icon icon="fas fa-share-nodes" />
      {{ t('navigation.item.shared') }}
    </v-btn>
    <v-btn :to="{ name: 'public' }" rounded="xl" @click="refresh">
      <v-icon icon="fas fa-globe" />
      {{ t('navigation.item.public') }}
    </v-btn>
  </v-bottom-navigation>
</template>
