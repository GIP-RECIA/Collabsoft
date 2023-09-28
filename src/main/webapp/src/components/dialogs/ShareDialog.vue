<script setup lang="ts">
import { useConfigurationStore } from '@/stores/configurationStore';
import { storeToRefs } from 'pinia';
import { computed } from 'vue';
import { useI18n } from 'vue-i18n';

const configurationStore = useConfigurationStore();
const { isShare } = storeToRefs(configurationStore);

const { t } = useI18n();

const modelValue = computed<boolean>({
  get() {
    return isShare.value;
  },
  set() {},
});

const save = () => {
  isShare.value = false;
};
</script>

<template>
  <v-dialog v-model="modelValue" :max-width="1024">
    <v-card>
      <v-toolbar :title="t('dialog.share.title')" color="rgba(255, 255, 255, 0)">
        <template #append>
          <v-btn icon="fas fa-xmark" color="default" variant="plain" @click="isShare = false" />
        </template>
      </v-toolbar>
      <v-card-text></v-card-text>

      <v-card-actions>
        <v-spacer />
        <v-btn :text="t('button.save')" @click="save" />
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>
