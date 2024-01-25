<script setup lang="ts">
import { useConfigurationStore } from '@/stores/configurationStore.ts';
import { AppSlug } from '@/types/enums/AppSlug.ts';
import { storeToRefs } from 'pinia';
import { computed, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRouter } from 'vue-router';

const configurationStore = useConfigurationStore();
const { isJoin } = storeToRefs(configurationStore);

const router = useRouter();
const { t } = useI18n();

const modelValue = computed<boolean>({
  get() {
    return isJoin.value;
  },
  set() {},
});

const joinCode = ref<string>('');

const canJoin = computed<boolean>(() => joinCode.value.trim().length == 6);

const join = (): void => {
  router.push({ name: `join-${AppSlug.tldraw}`, params: { joinCode: joinCode.value } });
  joinCode.value = '';
  isJoin.value = false;
};
</script>

<template>
  <v-dialog v-model="modelValue" :max-width="1024 / 2">
    <v-card rounded="xl">
      <v-toolbar :title="t('dialog.join.title')" color="rgba(255, 255, 255, 0)">
        <template #append>
          <v-btn icon="fas fa-xmark" color="default" variant="plain" :alt="t('button.close')" @click="isJoin = false" />
        </template>
      </v-toolbar>
      <v-card-text>
        <v-otp-input id="join-code" v-model="joinCode" type="text" rounded="xl" />
      </v-card-text>
      <v-card-actions>
        <v-spacer />
        <v-btn
          prepend-icon="fas fa-arrow-right-to-bracket"
          :text="t('button.join')"
          color="success"
          :disabled="!canJoin"
          @click="join"
        />
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>
