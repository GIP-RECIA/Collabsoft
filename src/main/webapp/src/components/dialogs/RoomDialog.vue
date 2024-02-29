<script setup lang="ts">
import { useAppStore } from '@/stores/appStore.ts';
import { useHomeStore } from '@/stores/homeStore.ts';
import { AppSlug } from '@/types/enums/AppSlug.ts';
import type { RoomAction } from '@/types/roomActionType.ts';
import { charOTP } from '@/utils/stringUtils.ts';
import debounce from 'lodash.debounce';
import { storeToRefs } from 'pinia';
import { computed, ref } from 'vue';
import { useI18n } from 'vue-i18n';

const isDev = import.meta.env.DEV;

const appStore = useAppStore();
const { initRoom } = appStore;

const homeStore = useHomeStore();
const { isRoom } = storeToRefs(homeStore);

const { t } = useI18n();

const modelValue = computed<boolean>({
  get() {
    return isRoom.value;
  },
  set() {},
});

const appType = ref<AppSlug | undefined>(isDev ? undefined : AppSlug.tldraw);
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
  const roomId = action == 'create' ? charOTP() : joinCode.value.toUpperCase();
  initRoom(roomId, appType.value as AppSlug, undefined);
  onClose();
};

const onClose = (): void => {
  isRoom.value = false;
  reset();
};

const reset = debounce((): void => {
  appType.value = isDev ? undefined : AppSlug.tldraw;
  joinCode.value = '';
}, 200);
</script>

<template>
  <v-dialog v-model="modelValue" :max-width="1024 / 2">
    <v-card rounded="xl">
      <v-toolbar :title="t('dialog.room.title')" color="rgba(255, 255, 255, 0)">
        <template #append>
          <v-btn icon="fas fa-xmark" color="default" variant="plain" :alt="t('button.close')" @click="onClose" />
        </template>
      </v-toolbar>
      <v-card-text class="d-flex flex-column align-center">
        <v-btn-toggle v-if="isDev" v-model="appType" mandatory class="mb-3">
          <v-btn text="tldraw" :value="AppSlug.tldraw" rounded="xl" />
          <v-btn text="WiseMapping" :value="AppSlug.wisemapping" rounded="xl" />
        </v-btn-toggle>
        <v-otp-input v-model="joinCode" type="text" />
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
