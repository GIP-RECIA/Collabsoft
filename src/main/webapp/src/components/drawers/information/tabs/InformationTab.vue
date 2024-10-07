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
import type { FileBody } from '@/types'
import { setFile } from '@/services/api'
import { useFileStore, useHomeStore } from '@/stores'
import { errorHandler } from '@/utils'
import { format } from 'date-fns'
import { storeToRefs } from 'pinia'
import { computed, onMounted, ref, watch } from 'vue'
import { useI18n } from 'vue-i18n'

const fileStore = useFileStore()
const { setFile: setFileFromStore } = fileStore
const { file } = storeToRefs(fileStore)

const homeStore = useHomeStore()
const { isDrawer, drawerTab } = storeToRefs(homeStore)

const { t } = useI18n()

const isEdit = ref<boolean>(false)
const tmp = ref<{ title: string, description: string | null }>({ title: '', description: '' })

function initForm(): void {
  isEdit.value = false
  if (file.value === undefined)
    return
  tmp.value = {
    title: file.value.title,
    description: file.value.description,
  }
}

function edit(): void {
  isEdit.value = true
  document.getElementById('tmp-title')?.focus()
}

const canSave = computed<boolean>(() => {
  if (!file.value)
    return false

  const hasTitle = tmp.value.title !== undefined && tmp.value.title.trim().length > 0
  const titleHasChanged = tmp.value.title !== file.value.title

  const tmpDesctiption
    = tmp.value.description && tmp.value.description.trim().length > 0 ? tmp.value.description : null
  const descriptionHasChanged = tmpDesctiption !== file.value?.description

  return hasTitle && (titleHasChanged || descriptionHasChanged)
})

async function save(): Promise<void> {
  if (!canSave.value || file.value === undefined)
    return
  try {
    await setFile(file.value.id, tmp.value as FileBody)
    setFileFromStore('both', file.value.id, tmp.value as FileBody)
    isEdit.value = false
  }
  catch (e) {
    errorHandler(e, true)
  }
}

watch(file, (): void => initForm())

watch(drawerTab, (): void => initForm())

watch(isDrawer, (): void => initForm())

onMounted((): void => initForm())
</script>

<template>
  <v-text-field
    id="tmp-title"
    v-model="tmp.title"
    :label="t('information.title')"
    :maxlength="45"
    variant="solo-filled"
    rounded="xl"
    flat
    hide-details
    :readonly="!isEdit"
    class="mb-2"
  />
  <v-textarea
    v-model="tmp.description"
    :label="t('information.description')"
    :maxlength="255"
    variant="solo-filled"
    rounded="xl"
    flat
    hide-details
    :readonly="!isEdit"
    class="mb-2"
  />
  <div class="d-flex mb-2">
    <v-spacer />
    <v-btn v-if="!isEdit" prepend-icon="fas fa-pen" :text="t('button.edit')" variant="tonal" @click="edit" />
    <div v-else>
      <v-btn
        prepend-icon="fas fa-xmark"
        :text="t('button.cancel')"
        variant="tonal"
        color="secondary"
        class="me-2"
        @click="initForm"
      />
      <v-btn
        prepend-icon="fas fa-save"
        :text="t('button.save')"
        variant="tonal"
        color="success"
        :disabled="!canSave"
        @click="save"
      />
    </div>
  </div>
  <template v-if="file">
    <div class="text-body-2 ms-2 mb-1">
      {{ t(`application.${file.associatedApp.slug}`) }}
    </div>
    <div class="text-body-2 ms-2 mb-1">
      {{ t('information.creationDate', { date: format(file.creationDate, 'Pp') }) }}
    </div>
    <div class="text-body-2 ms-2 mb-1">
      {{ t('information.editionDate', { date: format(file.editionDate, 'Pp') }) }}
    </div>
  </template>
</template>
