<script setup lang="ts">
import { useHomeStore } from '@/stores/homeStore.ts';
import { storeToRefs } from 'pinia';
import { useI18n } from 'vue-i18n';
import { useDisplay } from 'vuetify';

const homeStore = useHomeStore();
const { search, isNew, isRoom } = storeToRefs(homeStore);

const { t } = useI18n();
const { mobile } = useDisplay();
</script>

<template>
  <div :class="['d-flex', 'align-center', ' justify-space-between', mobile ? 'mb-2' : 'mb-4']">
    <div>
      <v-btn icon="fas fa-plus" variant="tonal" size="small" @click="isNew = true" />
      <v-btn
        prepend-icon="fas fa-chalkboard-user"
        variant="tonal"
        :text="t('button.rooms')"
        :class="[mobile ? 'ml-2' : 'ml-4', 'custom-height']"
        @click="isRoom = true"
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
