<script setup lang="ts">
import { useConfigurationStore } from '@/stores/configurationStore.ts';
import { usePreferredDark } from '@vueuse/core';
import { storeToRefs } from 'pinia';
import { computed, ref, watch } from 'vue';
import { useI18n } from 'vue-i18n';
import { useTheme } from 'vuetify';

const configurationStore = useConfigurationStore();
const { isSettings } = storeToRefs(configurationStore);

const { t } = useI18n();
const isDark = usePreferredDark();
const theme = useTheme();

const modelValue = computed<boolean>({
  get() {
    return isSettings.value;
  },
  set() {},
});

const selected = ref<Array<string>>([]);

const updateSelected = (newValue: Array<any>): void => {
  setDarkTheme(newValue.includes('dark'));
};

const setDarkTheme = (newValue: boolean): void => {
  theme.global.name.value = newValue ? 'dark' : 'light';
};

watch(
  theme.global.name,
  (newValue): void => {
    if (newValue == 'dark') selected.value.push('dark');
    else {
      const index = selected.value.indexOf('dark');
      if (index > -1) selected.value = selected.value.slice(index, 0);
    }
  },
  { immediate: true },
);

watch(isDark, (newValue): void => {
  setDarkTheme(newValue);
});

const onClose = (): void => {
  isSettings.value = false;
};
</script>

<template>
  <v-dialog v-model="modelValue" :max-width="1024">
    <v-card rounded="xl">
      <v-toolbar :title="t('navigation.item.settings')" color="rgba(255, 255, 255, 0)">
        <template #append>
          <v-btn icon="fas fa-xmark" color="default" variant="plain" :alt="t('button.close')" @click="onClose" />
        </template>
      </v-toolbar>
      <v-list v-model:selected="selected" select-strategy="classic" @update:selected="updateSelected">
        <v-list-subheader>{{ t('settings.appearance.subheader') }}</v-list-subheader>
        <v-list-item
          :title="t('settings.appearance.darkTheme.title')"
          :subtitle="t('settings.appearance.darkTheme.subtitle')"
          value="dark"
        >
          <template #prepend="{ isActive }">
            <v-list-item-action start>
              <v-checkbox-btn :model-value="isActive" />
            </v-list-item-action>
          </template>
        </v-list-item>
      </v-list>
    </v-card>
  </v-dialog>
</template>
