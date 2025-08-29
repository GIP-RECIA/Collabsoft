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
import debounce from 'lodash.debounce'
import { storeToRefs } from 'pinia'
import { computed, onMounted, ref } from 'vue'
import { useI18n } from 'vue-i18n'
import { useConfigurationStore } from '@/stores'
import { initToken } from '@/utils'

const configurationStore = useConfigurationStore()
const { init } = configurationStore
const { configuration, isReady, isSoffitOk } = storeToRefs(configurationStore)

const { t } = useI18n()

const modelValue = computed<boolean>({
  get() {
    return !isReady.value
  },
  set() {},
})

const response = ref<boolean>()
const isLoading = computed<boolean>(() => response.value === undefined)
const errorMessage = computed<string>(() => {
  if (!response.value)
    return 'config'
  if (!isSoffitOk.value)
    return 'soffit'
  return 'unknown'
})
const canRetry = ref<boolean>(true)

onMounted(async (): Promise<void> => {
  response.value = await init()
})

function login(): void {
  window.open(`${window.location.protocol}//${window.location.hostname}`, '_blank')
}

async function retry(): Promise<void> {
  const timeout: number = 5000
  debounce(
    async () => {
      canRetry.value = false
      setTimeout(() => (canRetry.value = true), timeout)
      response.value = undefined
      await initToken(configuration.value!.front.userInfoApiUrl)
      response.value = true
    },
    timeout,
    { leading: true },
  )()
}
</script>

<template>
  <v-dialog v-model="modelValue" :max-width="300">
    <v-card rounded="xl">
      <v-toolbar color="rgba(255, 255, 255, 0)">
        <v-toolbar-title :text="t('dialog.signIn.title')" class="text-h6" />
        <v-spacer />
        <v-progress-circular v-show="isLoading" class="me-4" indeterminate />
      </v-toolbar>
      <v-card-text v-if="!isLoading && !isReady" class="text-preserve-breaks">
        {{ t(`error.signIn.${errorMessage}`) }}
      </v-card-text>
      <v-card-actions v-if="errorMessage === 'soffit'">
        <v-spacer />
        <v-btn color="primary" prepend-icon="fas fa-right-to-bracket" :text="t('button.signIn')" @click="login" />
        <v-btn
          color="primary"
          prepend-icon="fas fa-arrow-rotate-right"
          :text="t('button.retry')"
          :disabled="!canRetry"
          @click="retry"
        />
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>
