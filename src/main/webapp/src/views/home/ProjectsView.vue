<script setup lang="ts">
import { useConfigurationStore } from '@/stores/configurationStore.ts';
import { storeToRefs } from 'pinia';
import { useI18n } from 'vue-i18n';
import { useDisplay } from 'vuetify';

const { mobile } = useDisplay();

const configurationStore = useConfigurationStore();
const { search, isNew } = storeToRefs(configurationStore);

const { t } = useI18n();
</script>

<template>
  <div :class="['d-flex', 'align-center', ' justify-space-between', mobile ? 'mb-2' : 'mb-4']">
    <v-btn icon="fas fa-plus" variant="tonal" @click="isNew = true" />
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
