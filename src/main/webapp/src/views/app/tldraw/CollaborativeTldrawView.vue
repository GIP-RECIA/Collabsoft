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
import { computed, onMounted, onUnmounted } from 'vue'
import { useTheme } from 'vuetify'
import { useAppStore, useConfigurationStore } from '@/stores'
import { headObserver, styleObserver } from '@/utils'
import '@gip-recia/tldraw-webcomponent'

const { VITE_API_URI } = import.meta.env

const appStore = useAppStore()
const { isAutoSave, canAutoSave, roomId, room, isRoomOwner, initRoomFileId, destroy, websocketApiUrl }
  = storeToRefs(appStore)

const configurationStore = useConfigurationStore()
const { user } = storeToRefs(configurationStore)

const theme = useTheme()

const apiUrl = computed<string | undefined>(() =>
  room.value?.fileId ? `${VITE_API_URI}/api/file/${room.value.fileId}` : undefined,
)

onMounted(() => {
  styleObserver.observe(document.body, { attributes: true })
  headObserver.observe(document.head, { childList: true })
})

onUnmounted(() => {
  styleObserver.disconnect()
  headObserver.disconnect()
})
</script>

<template>
  <tldraw-editor
    v-if="roomId"
    mode="multi"
    :persistance-api-url="apiUrl"
    :assets-api-url="apiUrl ? `${apiUrl}/resource` : undefined"
    :token="user.token"
    :dark-mode="theme.global.name.value === 'dark'"
    :auto-save="canAutoSave && isAutoSave"
    :open="isRoomOwner"
    :websocket-api-url="websocketApiUrl"
    :room-id="`${roomId}-tldraw`"
    :init-url="initRoomFileId ? `${VITE_API_URI}/api/file/${initRoomFileId}` : undefined"
    :owner="isRoomOwner"
    :clear-on-leave="true"
    :leave="destroy"
  />
</template>
