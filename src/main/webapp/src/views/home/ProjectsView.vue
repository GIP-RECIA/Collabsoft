<script setup lang="ts">
import { useConfigurationStore } from '@/stores/configurationStore.ts';
import { storeToRefs } from 'pinia';
import { useDisplay } from 'vuetify';

const configurationStore = useConfigurationStore();
const { search, isNew, isJoin } = storeToRefs(configurationStore);

const isDev = import.meta.env.DEV;

const { mobile } = useDisplay();
</script>

<template>
  <div :class="['d-flex', 'align-center', ' justify-space-between', mobile ? 'mb-2' : 'mb-4']">
    <div>
      <v-btn icon="fas fa-plus" variant="tonal" @click="isNew = true" />
      <v-btn v-if="isDev" icon="fas fa-arrow-right-to-bracket" variant="tonal" class="ml-4" @click="isJoin = true" />
    </div>
    <v-text-field
      v-model="search"
      variant="solo"
      density="compact"
      rounded="xl"
      prepend-inner-icon="fas fa-magnifying-glass"
      flat
      hide-details
      single-line
      clearable
      :class="[mobile ? 'ml-2' : 'ml-4', 'max-width']"
    />
  </div>
</template>

<style scoped lang="scss">
.max-width {
  max-width: 300px;
}
</style>
