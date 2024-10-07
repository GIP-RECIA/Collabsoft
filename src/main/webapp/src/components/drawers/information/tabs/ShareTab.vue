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
import type { Collaboration } from '@/types'
import { useFileStore, useHomeStore } from '@/stores'
import { getRole, Role } from '@/types/enums'
import { storeToRefs } from 'pinia'
import { ref, watch } from 'vue'
import { useI18n } from 'vue-i18n'

const isDev = import.meta.env.DEV

const fileStore = useFileStore()
const { refreshFile } = fileStore
const { file } = storeToRefs(fileStore)

const homeStore = useHomeStore()
const { isShareInRoom } = storeToRefs(homeStore)

const { t } = useI18n()

const roles: Array<Role> = [Role.editor, Role.readonly]

const newUser = ref<any>()
const newRole = ref<Role>()

function updateRole(collaboration: Collaboration, newValue: Role): void {
  if (newValue !== getRole(collaboration.role))
    refreshFile()
}

function addUser(): void {
  newUser.value = undefined
  newRole.value = undefined
}

const visibility = ref<boolean>(false)

watch(
  () => file.value?.pub,
  (newValue): void => {
    if (newValue === undefined)
      return
    visibility.value = newValue
  },
  { immediate: true },
)

watch(visibility, (): void => refreshFile())
</script>

<template>
  <div v-if="isDev" class="d-flex align-center mb-2 bg-grey-lighten-3 rounded-xl">
    <v-text-field
      v-model="newUser"
      variant="solo"
      bg-color="rgba(255, 255, 255, 0)"
      rounded="xl"
      class="share-item--name"
      flat
      hide-details
    />
    <v-select
      v-model="newRole"
      :items="roles"
      variant="solo"
      rounded="xl"
      bg-color="rgba(255, 255, 255, 0)"
      class="share-item--role"
      flat
      hide-details
      hide-no-data
    >
      <v-list rounded="xl" class="pa-2" />
      <template #item="{ props }">
        <v-list-item v-bind="props" rounded="xl" />
      </template>
    </v-select>
    <v-btn
      icon="fas fa-plus"
      color="success"
      variant="text"
      size="small"
      :alt="t('button.add')"
      class="me-2"
      @click="addUser"
    />
  </div>

  <v-list v-if="file?.collaborations" class="pt-0">
    <v-list-item
      v-for="(collaboration, index) in file.collaborations"
      :key="index"
      rounded="xl"
      class="pe-2 bg-grey-lighten-3 list-item--custom"
      :class="[index < file.collaborations.length - 1 ? 'mb-2' : '']"
    >
      <template #default>
        <div class="d-flex">
          <div class="d-flex align-center share-item--name">
            {{ collaboration.user.displayName }}
          </div>
          <v-select
            :model-value="getRole(collaboration.role)"
            :items="roles"
            variant="solo"
            density="compact"
            rounded="xl"
            bg-color="rgba(255,255,255, 0)"
            flat
            hide-details
            hide-no-data
            class="share-item--role"
            @update:model-value="(newValue) => updateRole(collaboration, newValue)"
          >
            <v-list rounded="xl" class="pa-2" />
            <template #item="{ props }">
              <v-list-item v-bind="props" rounded="xl" />
            </template>
          </v-select>
        </div>
      </template>
      <template #append>
        <v-btn icon="fas fa-trash" color="error" variant="text" size="small" :alt="t('button.delete')" />
      </template>
    </v-list-item>
  </v-list>

  <div :class="[{ 'my-2': isDev }]">
    <div class="ms-2 mb-1">
      {{ t('shareOptions') }}
    </div>
    <v-btn
      prepend-icon="fas fa-chalkboard-user"
      :text="t('button.shareInRoom')"
      variant="tonal"
      :disabled="!file?.associatedApp.enabled"
      block
      @click="isShareInRoom = true"
    />
  </div>

  <div v-if="isDev">
    <div class="ms-2 my-1">
      {{ t('information.visibility') }}
    </div>
    <v-switch
      v-model="visibility"
      :label="t(`visibility.${visibility ? 'public' : 'private'}`)"
      density="compact"
      inset
      hide-details
    />
  </div>
</template>

<style scoped lang="scss">
.share-item--name {
  width: 100%;
}

.share-item--role {
  min-width: 150px;
}

.list-item--custom {
  height: 56px;
}
</style>
