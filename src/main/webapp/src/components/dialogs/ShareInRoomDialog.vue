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
import { useAppStore, useFileStore, useHomeStore } from '@/stores'
import { charOTP } from '@/utils'
import { storeToRefs } from 'pinia'
import { ref } from 'vue'
import { useI18n } from 'vue-i18n'

const appStore = useAppStore()
const { initRoom } = appStore

const fileStore = useFileStore()
const { file } = storeToRefs(fileStore)

const homeStore = useHomeStore()
const { isShareInRoom } = storeToRefs(homeStore)

const { t } = useI18n()

const joinCode = ref<string>(charOTP())
const autoSave = ref<boolean>(false)

function onCreateAndJoin(): void {
  if (file.value === undefined)
    return
  onClose()
  initRoom(joinCode.value, file.value.associatedApp.slug, file.value.id, autoSave.value)
}

function onAutoSave(): void {
  autoSave.value = !autoSave.value
}

function onClose(): void {
  isShareInRoom.value = false
}

function reset(): void {
  joinCode.value = charOTP()
  autoSave.value = false
}
</script>

<template>
  <v-dialog v-model="isShareInRoom" :max-width="1024 / 2" persistent @after-leave="reset">
    <v-card rounded="xl">
      <v-toolbar :title="t('dialog.shareInRoom.title')" color="rgba(255, 255, 255, 0)">
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
      <v-card-text class="pt-0">
        <v-alert type="info" variant="tonal" class="text-preserve-breaks">
          <i18n-t keypath="dialog.shareInRoom.info">
            <template #icon>
              <v-icon icon="fas fa-arrows-rotate" size="small" />
            </template>
          </i18n-t>
        </v-alert>
        <div class="d-flex flex-column align-center mt-4">
          {{ t('dialog.shareInRoom.description') }}
          <v-otp-input :model-value="joinCode" type="text" disabled />
        </div>
      </v-card-text>
      <v-card-actions>
        <v-tooltip :text="t(`button.autoSave.${autoSave ? 'enabled' : 'disabled'}`)" location="bottom center">
          <template #activator="{ props }">
            <v-btn v-bind="props" color="secondary" @click="onAutoSave">
              <v-icon icon="fas fa-arrows-rotate" :class="[autoSave ? 'text-info' : 'text-disabled']" />
            </v-btn>
          </template>
        </v-tooltip>
        <v-spacer />
        <v-btn
          prepend-icon="fas fa-arrow-right-to-bracket"
          :text="t('button.createAndJoin')"
          color="success"
          @click="onCreateAndJoin()"
        />
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>
