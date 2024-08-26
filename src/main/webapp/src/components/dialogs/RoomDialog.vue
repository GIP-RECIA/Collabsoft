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
import { useAppStore, useConfigurationStore, useHomeStore } from '@/stores/index.ts';
import type { RoomAction } from '@/types/roomActionType.ts';
import { charOTP } from '@/utils/stringUtils.ts';
import debounce from 'lodash.debounce';
import { storeToRefs } from 'pinia';
import { computed, ref } from 'vue';
import { useI18n } from 'vue-i18n';

const appStore = useAppStore();
const { initRoom, joinRoom } = appStore;

const configurationStore = useConfigurationStore();
const { availableApps } = storeToRefs(configurationStore);

const homeStore = useHomeStore();
const { isRoom } = storeToRefs(homeStore);

const { t } = useI18n();

const modelValue = computed<boolean>({
  get() {
    return isRoom.value;
  },
  set() {},
});

const appType = ref<string | undefined>(availableApps.value.length == 1 ? availableApps.value[0].slug : undefined);
const joinCode = ref<string>('');

const button = computed<{ i18n: string; icon: string; disabled: boolean; action: RoomAction }>(() => {
  if (joinCode.value.length > 0) {
    return {
      i18n: 'button.join',
      icon: 'fas fa-arrow-right-to-bracket',
      disabled: appType.value == undefined || !/^[a-zA-Z]{6}$/.test(joinCode.value),
      action: 'join',
    };
  }

  return {
    i18n: 'button.create',
    icon: 'fas fa-plus',
    disabled: appType.value == undefined,
    action: 'create',
  };
});

const onAction = (action: RoomAction): void => {
  if (!appType.value) return;
  if (action == 'create') initRoom(charOTP(), appType.value, undefined);
  else joinRoom(joinCode.value.toUpperCase(), appType.value);
  onClose();
};

const onClose = (): void => {
  isRoom.value = false;
  reset();
};

const reset = debounce((): void => {
  appType.value = availableApps.value.length == 1 ? availableApps.value[0].slug : undefined;
  joinCode.value = '';
}, 200);
</script>

<template>
  <v-dialog v-model="modelValue" :max-width="1024 / 2">
    <v-card rounded="xl">
      <v-toolbar :title="t('dialog.room.title')" color="rgba(255, 255, 255, 0)">
        <template #append>
          <v-btn
            icon="fas fa-xmark"
            color="default"
            variant="plain"
            :alt="t('button.close')"
            class="me-1"
            @click="onClose"
          />
        </template>
      </v-toolbar>
      <v-card-text class="pt-0">
        <v-alert type="info" variant="tonal" class="text-preserve-breaks">
          {{ t('dialog.room.info') }}
        </v-alert>
        <div class="d-flex flex-column align-center mt-4">
          <v-btn-toggle v-model="appType" mandatory class="mb-3">
            <v-btn
              v-for="app in availableApps"
              :key="app.id"
              :text="t(`application.${app.slug}`)"
              :value="app.slug"
              rounded="xl"
            />
          </v-btn-toggle>
          <v-otp-input v-model="joinCode" type="text" />
        </div>
      </v-card-text>
      <v-card-actions>
        <v-spacer />
        <v-btn
          :prepend-icon="button.icon"
          :text="t(button.i18n)"
          color="success"
          :disabled="button.disabled"
          @click="onAction(button.action)"
        />
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<style lang="scss">
.v-otp-input__field {
  text-transform: uppercase;
}
</style>
