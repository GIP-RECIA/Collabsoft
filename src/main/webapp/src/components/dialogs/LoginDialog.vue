<script setup lang="ts">
import { useConfigurationStore } from '@/stores/configurationStore.ts';
import { storeToRefs } from 'pinia';
import { computed, onMounted } from 'vue';
import { useI18n } from 'vue-i18n';

const configurationStore = useConfigurationStore();
const { init } = configurationStore;
const { isInit, isSoffitOk } = storeToRefs(configurationStore);

const { t } = useI18n();

const modelValue = computed<boolean>({
  get() {
    return !isSoffitOk.value;
  },
  set() {},
});

onMounted(() => {
  init();
});
</script>

<template>
  <v-dialog v-model="modelValue" :max-width="300">
    <v-card>
      <v-toolbar color="rgba(255, 255, 255, 0)">
        <v-toolbar-title :text="t('dialog.signIn.title')" class="text-h6" />
        <v-spacer />
        <v-progress-circular v-show="!isInit" class="mr-4" indeterminate />
      </v-toolbar>
      <v-card-text v-if="isInit && !isSoffitOk">{{ t('dialog.signIn.fail') }}</v-card-text>
    </v-card>
  </v-dialog>
</template>
