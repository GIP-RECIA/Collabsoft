<script setup lang="ts">
import { Response } from '@/types/enums/Response.ts';
import { useI18n } from 'vue-i18n';

const { t } = useI18n();

defineProps<{
  title: string;
  description?: string;
  yesValue: string;
  noValue: string;
  cancelable?: boolean;
  noColor?: string;
  yesColor?: string;
}>();

const modelValue = defineModel<boolean>();

const emit = defineEmits<(event: 'close', payload: Response) => void>();

const onCancel = (): void => {
  modelValue.value = false;
  emit('close', Response.cancel);
};

const onNo = (): void => {
  modelValue.value = false;
  emit('close', Response.no);
};

const onYes = (): void => {
  modelValue.value = false;
  emit('close', Response.yes);
};
</script>

<template>
  <v-dialog v-model="modelValue" persistent :max-width="500">
    <v-card :title="title" rounded="xl">
      <v-card-text v-if="description">{{ description }}</v-card-text>

      <v-card-actions>
        <v-btn v-if="cancelable" :text="t('button.cancel')" @click="onCancel" />
        <v-spacer />
        <v-btn :text="t(noValue)" :color="noColor" @click="onNo" />
        <v-btn :text="t(yesValue)" :color="yesColor" @click="onYes" />
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>
