<script setup lang="ts">
import { useConfigurationStore } from '@/stores/configurationStore.ts';
import { storeToRefs } from 'pinia';
import { computed, onMounted, ref } from 'vue';
import { useI18n } from 'vue-i18n';

const configurationStore = useConfigurationStore();
const { init } = configurationStore;
const { isReady, isSoffitOk } = storeToRefs(configurationStore);

const { t } = useI18n();

const modelValue = computed<boolean>({
  get() {
    return !isReady.value;
  },
  set() {},
});

const response = ref<boolean>();
const isLoading = computed<boolean>(() => response.value == undefined);
const errorMessage = computed<string>(() => {
  if (!response.value) return 'config';
  if (!isSoffitOk.value) return 'soffit';
  return 'unknown';
});

onMounted(async () => {
  response.value = await init();
});
</script>

<template>
  <v-dialog v-model="modelValue" :max-width="300">
    <v-card>
      <v-toolbar color="rgba(255, 255, 255, 0)">
        <v-toolbar-title :text="t('dialog.signIn.title')" class="text-h6" />
        <v-spacer />
        <v-progress-circular v-show="isLoading" class="me-4" indeterminate />
      </v-toolbar>
      <v-card-text v-if="!isLoading && !isReady">{{ t(`error.signIn.${errorMessage}`) }}</v-card-text>
    </v-card>
  </v-dialog>
</template>
