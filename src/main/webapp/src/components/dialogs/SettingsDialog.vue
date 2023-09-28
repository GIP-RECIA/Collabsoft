<script setup lang="ts">
import { useConfigurationStore } from '@/stores/configurationStore';
import { storeToRefs } from 'pinia';
import { computed, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useTheme } from 'vuetify';

const configurationStore = useConfigurationStore();
const { isSettings } = storeToRefs(configurationStore);

const { t } = useI18n();
const theme = useTheme();

const modelValue = computed<boolean>({
  get() {
    return isSettings.value;
  },
  set() {},
});

const selected = ref<Array<string>>(['']);

const updateSelected = (newValue: Array<any>) => {
  setDarkTheme(newValue.includes('dark'));
};

const setDarkTheme = (newValue: boolean) => {
  theme.global.name.value = !newValue ? 'light' : 'dark';
};
</script>

<template>
  <v-dialog v-model="modelValue" :max-width="1024">
    <v-card>
      <v-toolbar :title="t('navigation.item.settings')" color="rgba(255, 255, 255, 0)">
        <template #append>
          <v-btn icon="fas fa-xmark" color="default" variant="plain" @click="isSettings = false" />
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
