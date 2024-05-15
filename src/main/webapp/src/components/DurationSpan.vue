<script setup lang="ts">
import { dateToDuration, formatedDuration } from '@/utils/dateFnsUtils.ts';
import { onUnmounted, ref, watch } from 'vue';
import { useI18n } from 'vue-i18n';

const { t } = useI18n();

const props = defineProps<{
  date: string;
}>();

const formated = ref<string>('');

const timeout = ref<NodeJS.Timeout | undefined>();

const getFormatedDuration = (): void => {
  const duration = dateToDuration(props.date);
  const { years, months, weeks, days, hours } = duration;

  if (!years && !months && !weeks && !days)
    timeout.value = setTimeout(getFormatedDuration, hours && hours > 0 ? 600000 : 20000);

  formated.value = formatedDuration(duration);
};

watch(
  () => props.date,
  () => {
    clearTimeout(timeout.value);
    getFormatedDuration();
  },
  { immediate: true },
);

onUnmounted(() => clearTimeout(timeout.value));
</script>

<template>
  <span>{{ t('information.duration', { duration: formated }) }}</span>
</template>
