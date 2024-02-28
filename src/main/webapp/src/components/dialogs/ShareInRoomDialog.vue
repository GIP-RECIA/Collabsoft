<script setup lang="ts">
import { useAppStore } from '@/stores/appStore.ts';
import { useFileStore } from '@/stores/fileStore.ts';
import { useHomeStore } from '@/stores/homeStore.ts';
import type { AppSlug } from '@/types/enums/AppSlug.ts';
import { charOTP } from '@/utils/stringUtils.ts';
import debounce from 'lodash.debounce';
import { storeToRefs } from 'pinia';
import { computed, ref } from 'vue';
import { useI18n } from 'vue-i18n';

const appStore = useAppStore();
const { initRoom } = appStore;

const fileStore = useFileStore();
const { file } = storeToRefs(fileStore);

const homeStore = useHomeStore();
const { isShareInRoom } = storeToRefs(homeStore);

const { t } = useI18n();

const modelValue = computed<boolean>({
  get() {
    return isShareInRoom.value;
  },
  set() {},
});

const joinCode = ref<string>(charOTP());

const onCreateAndJoin = (): void => {
  if (file.value == undefined) return;
  onClose();
  initRoom(joinCode.value, file.value.associatedApp.slug as AppSlug, file.value.id, false);
};

const onClose = (): void => {
  isShareInRoom.value = false;
  reset();
};

const reset = debounce((): void => {
  joinCode.value = charOTP();
}, 200);
</script>

<template>
  <v-dialog v-model="modelValue" :max-width="1024 / 2">
    <v-card rounded="xl">
      <v-toolbar :title="t('dialog.shareInRoom.title')" color="rgba(255, 255, 255, 0)">
        <template #append>
          <v-btn icon="fas fa-xmark" color="default" variant="plain" :alt="t('button.close')" @click="onClose" />
        </template>
      </v-toolbar>
      <v-card-text class="d-flex flex-column align-center">
        {{ t('dialog.shareInRoom.description') }}
        <v-otp-input :model-value="joinCode" type="text" disabled />
      </v-card-text>
      <v-card-actions>
        <v-spacer />
        <v-btn
          prepend-icon="fas fa-arrow-right-to-bracket"
          :text="t('button.createAndJoin')"
          color="success"
          @click="onCreateAndJoin()"
        />
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>
