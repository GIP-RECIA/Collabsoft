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
import { useHomeStore } from '@/stores/homeStore.ts';
import { storeToRefs } from 'pinia';
import { useI18n } from 'vue-i18n';
import { useDisplay } from 'vuetify';

const homeStore = useHomeStore();
const { isNew, isRoom, search } = storeToRefs(homeStore);

const { t } = useI18n();
const { mobile } = useDisplay();
</script>

<template>
  <div class="d-flex align-center justify-space-between" :class="[mobile ? 'mb-2' : 'mb-4']">
    <div>
      <v-btn icon="fas fa-plus" variant="tonal" size="small" @click="isNew = true" />
      <v-btn
        prepend-icon="fas fa-chalkboard-user"
        variant="tonal"
        :text="t('button.rooms')"
        class="custom-height"
        :class="[mobile ? 'ms-2' : 'ms-4']"
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
      class="max-width"
      :class="[mobile ? 'ms-2' : 'ms-4']"
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
