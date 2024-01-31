<script setup lang="ts">
import { useConfigurationStore } from '@/stores/configurationStore.ts';
import { storeToRefs } from 'pinia';
import { useI18n } from 'vue-i18n';
import { useDisplay } from 'vuetify';

const configurationStore = useConfigurationStore();
const { search, isNew, isJoin } = storeToRefs(configurationStore);

const isDev = import.meta.env.DEV;

const { t } = useI18n();
const { mobile } = useDisplay();
</script>

<template>
  <div :class="['d-flex', 'align-center', ' justify-space-between', mobile ? 'mb-2' : 'mb-4']">
    <div>
      <v-btn icon="fas fa-plus" variant="tonal" size="small" @click="isNew = true" />
      <v-btn
        v-if="isDev"
        prepend-icon="fas fa-arrow-right-to-bracket"
        variant="tonal"
        :text="t('button.join')"
        :class="[mobile ? 'ml-2' : 'ml-4', 'custom-height']"
        @click="isJoin = true"
      />
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

.v-btn.custom-height {
  --v-btn-height: 40px;
}
</style>
