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
import { storeToRefs } from 'pinia'
import { computed, ref } from 'vue'
import { useI18n } from 'vue-i18n'
import { saveFile } from '@/services/api'
import { useConfigurationStore, useFileStore, useHomeStore } from '@/stores'
import { errorHandler } from '@/utils'

const isDev = import.meta.env.DEV

const configurationStore = useConfigurationStore()
const { availableApps } = storeToRefs(configurationStore)

const fileStore = useFileStore()
const { addFile } = fileStore

const homeStore = useHomeStore()
const { isNew } = storeToRefs(homeStore)

const { t } = useI18n()

const fileType = ref<number | undefined>(availableApps.value.length === 1 ? availableApps.value[0].id : undefined)
const title = ref<string | undefined>()
const description = ref<string | undefined>()
const pub = ref<boolean>(false)

const canSave = computed<boolean>(
  () => fileType.value !== undefined && title.value !== undefined && title.value.trim().length > 0,
)

async function onSave(): Promise<void> {
  if (!canSave.value)
    return
  try {
    const response = await saveFile({
      title: title.value!,
      description: description.value && description.value.trim().length > 0 ? description.value : null,
      data: '',
      associatedAppId: fileType.value!,
      pub: pub.value,
    })
    addFile(response.data)
    onClose()
  }
  catch (e) {
    errorHandler(e, true)
  }
}

function onClose(): void {
  isNew.value = false
}

function reset(): void {
  fileType.value = availableApps.value.length === 1 ? availableApps.value[0].id : undefined
  title.value = undefined
  description.value = undefined
  pub.value = false
}
</script>

<template>
  <v-dialog v-model="isNew" :max-width="1024" persistent @after-leave="reset">
    <v-card rounded="xl">
      <v-toolbar :title="t('dialog.file.title')" color="rgba(255, 255, 255, 0)">
        <template #append>
          <v-btn
            icon="fas fa-xmark"
            color="default"
            variant="plain"
            :alt="t('button.close')"
            class="me-1"
            @click="onClose"
          />
        </template>
      </v-toolbar>
      <v-card-text>
        <div class="ms-2 mb-2">
          {{ t('dialog.file.description') }}
        </div>
        <v-btn-toggle v-model="fileType" mandatory class="mb-3">
          <v-btn
            v-for="app in availableApps"
            :key="app.id"
            :text="t(`application.${app.slug}`)"
            :value="app.id"
            rounded="xl"
          />
        </v-btn-toggle>
        <v-text-field
          v-model="title"
          :label="t('information.title')"
          :maxlength="45"
          variant="solo-filled"
          rounded="xl"
          flat
          hide-details
          class="mb-3"
        />
        <v-textarea
          v-model="description"
          :label="t('information.description')"
          :maxlength="255"
          variant="solo-filled"
          rounded="xl"
          flat
          hide-details
          class="mb-3"
        />
        <div v-if="isDev">
          <div class="ms-2 mb-1">
            {{ t('information.visibility') }}
          </div>
          <v-switch
            v-model="pub"
            :label="t(`visibility.${pub ? 'public' : 'private'}`)"
            density="compact"
            inset
            hide-details
          />
        </div>
      </v-card-text>
      <v-card-actions>
        <v-spacer />
        <v-btn
          prepend-icon="fas fa-save"
          :text="t('button.save')"
          color="success"
          :disabled="!canSave"
          @click="onSave"
        />
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>
