<script setup lang="ts">
import { useConfigurationStore } from '@/stores/configurationStore.ts';
import { storeToRefs } from 'pinia';
import { useDisplay } from 'vuetify';

const { mobile } = useDisplay();

const configurationStore = useConfigurationStore();
const { isNew } = storeToRefs(configurationStore);
</script>

<template>
  <div :class="[mobile ? 'ma-4' : 'mb-4']">
    <v-btn icon="fas fa-plus" variant="tonal" @click="isNew = true" />
  </div>
</template>
