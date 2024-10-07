<!--
 Copyright (C) 2023 GIP-RECIA, Inc.

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
-->

<script setup lang="ts">
import { dateToDuration, formatedDuration } from '@/utils'
import { onUnmounted, ref, watch } from 'vue'
import { useI18n } from 'vue-i18n'

const props = defineProps<{
  date: string
}>()

const { t } = useI18n()

const formated = ref<string>('')

const timeout = ref<number | undefined>()

function getFormatedDuration(): void {
  const duration = dateToDuration(props.date)
  const { years, months, weeks, days, hours } = duration

  if (!years && !months && !weeks && !days)
    timeout.value = setTimeout(getFormatedDuration, hours && hours > 0 ? 600000 : 20000)

  formated.value = formatedDuration(duration)
}

watch(
  () => props.date,
  () => {
    clearTimeout(timeout.value)
    getFormatedDuration()
  },
  { immediate: true },
)

onUnmounted(() => clearTimeout(timeout.value))
</script>

<template>
  <span>{{ t('information.duration', { duration: formated }) }}</span>
</template>
