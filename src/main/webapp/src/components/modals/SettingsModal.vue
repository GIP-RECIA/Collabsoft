<script setup lang="ts">
import BaseModal from '@/components/modals/BaseModal.vue';
import { ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useTheme } from 'vuetify';

const { t } = useI18n();
const theme = useTheme();

const vModel = ref<boolean>(true);

const selected = ref<Array<string>>(['']);
const updateSelected = (newValue: Array<string>) => {
  setDarkTheme(newValue.includes('dark'));
  selected.value = newValue;
};

const setDarkTheme = (newValue: boolean) => {
  theme.global.name.value = !newValue ? 'light' : 'dark';
};
</script>

<template>
  <base-modal v-model="vModel" :title="t('navigation.item.settings')" body-class="px-0">
    <v-list :selected="selected" select-strategy="classic" class="pt-0" @update:selected="updateSelected">
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
  </base-modal>
</template>
