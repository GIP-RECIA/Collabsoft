/**
 * Copyright (C) 2023 GIP-RECIA, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/* eslint-disable ts/no-use-before-define */
import type { RouteLocationRaw } from 'vue-router'
import { useSessionStorage } from '@vueuse/core'
import { defineStore, storeToRefs } from 'pinia'
import { computed, readonly, ref } from 'vue'
import { useRouter } from 'vue-router'
import { interpolate } from '@/utils'
import { useConfigurationStore } from './configurationStore.ts'
import { useFileStore } from './fileStore.ts'

export const useAppStore = defineStore('app', () => {
  const { VITE_BASE_URI } = import.meta.env

  const router = useRouter()
  const configurationStore = useConfigurationStore()
  const fileStore = useFileStore()

  /* -- App -- */

  /**
   * App context state
   */
  const isApp = ref<boolean>(false)

  /**
   * Room context state
   */
  const isRoom = computed<boolean>(() => _roomId.value !== undefined)

  /**
   * Type of app state
   */
  const _appType = ref<string | undefined>()

  /**
   * File / Room title
   */
  const title = computed<string>(() => {
    const { file } = storeToRefs(fileStore)

    const fileName = file.value ? file.value.title : ''

    return isRoom.value ? _roomId.value! : fileName
  })

  /**
   * Auto save state
   */
  const _autoSave = ref<boolean>(true)
  const isAutoSave = computed<boolean>({
    get() {
      return room.value ? room.value.saveOnFile : _autoSave.value
    },
    set(value) {
      if (canAutoSave.value)
        _autoSave.value = value
      if (room.value?.fileId)
        room.value.saveOnFile = value
    },
  })

  /**
   * Auto save is available state
   */
  const canAutoSave = computed<boolean>(() => {
    return !isRoom.value ? true : room.value?.fileId !== undefined
  })

  /* -- File -- */

  /**
   * Current file id
   */
  const _fileId = ref<number | undefined>()

  /* -- Room -- */

  /**
   * Current room id
   */
  const _roomId = ref<string | undefined>()

  /**
   * List of owned rooms
   */
  const _ownedRooms = useSessionStorage<
    Array<{ roomId: string, appType: string, fileId: number | undefined, saveOnFile: boolean }>
  >(`${__APP_SLUG__}.owned-rooms`, [])

  /**
   * Room information if owner
   */
  const room = computed(() =>
    _ownedRooms.value.find(room => room.roomId === _roomId.value && room.appType === _appType.value),
  )

  /**
   * Chech if user is the owner of the room
   */
  const isRoomOwner = computed<boolean>(() => room.value !== undefined)

  /**
   * Id of file to load in the room
   */
  const _initRoomFileId = ref<number | undefined>()

  /**
   * Initialize room and navigate to it
   */
  const initRoom = (roomId: string, appType: string, fileId: number | undefined, saveOnFile: boolean = false): void => {
    _initRoomFileId.value = fileId
    _ownedRooms.value.push({ roomId, appType, fileId, saveOnFile })
    joinRoom(roomId, appType)
  }

  const joinRoom = (roomId: string, appType: string): void => {
    const route: RouteLocationRaw = { name: `collaborative-${appType}`, params: { roomId } }
    isApp.value ? router.replace(route) : router.push(route)
  }

  /**
   * Destroy yjs session
   */
  const _destroy = ref<boolean>(false)
  const destroyRoom = (): void => {
    if (!isApp.value || !isRoom.value)
      return
    _destroy.value = true
    setTimeout(() => {
      _destroy.value = false
    }, 200)
  }

  /* -- Store actions -- */

  /**
   * Initialize app context
   */
  const initAppContext = (roomId: string | undefined, fileId: number | undefined, routeName: string): void => {
    const { loadFile } = fileStore
    const { file } = storeToRefs(fileStore)

    isApp.value = true
    _fileId.value = fileId
    _roomId.value = roomId
    _appType.value = routeName.startsWith('collaborative-') ? routeName.substring('collaborative-'.length) : routeName

    if (fileId !== undefined) {
      if (!file.value)
        loadFile(fileId)
    }
    else {
      file.value = undefined
    }
  }

  /**
   * Exit app context
   */
  const exitAppContext = (): void => {
    isApp.value = false
    _autoSave.value = true
    _fileId.value = undefined
    _roomId.value = undefined
    _appType.value = undefined
    _initRoomFileId.value = undefined
  }

  const websocketApiUrl = computed<string>(() => {
    const { configuration } = storeToRefs(configurationStore)

    return interpolate(
      // eslint-disable-next-line no-template-curly-in-string
      configuration.value?.front.collaboration.websocketApiUrl ?? '${protocol}://${domain}${baseUri}/ws',
      {
        protocol: `ws${location.protocol === 'http' ? '' : 's'}`,
        domain: window.location.hostname,
        baseUri: VITE_BASE_URI,
      },
    )
  })

  return {
    isApp,
    isRoom,
    title,
    isAutoSave,
    canAutoSave,
    roomId: readonly(_roomId),
    room,
    isRoomOwner,
    initRoomFileId: readonly(_initRoomFileId),
    initRoom,
    joinRoom,
    destroy: readonly(_destroy),
    destroyRoom,
    initAppContext,
    exitAppContext,
    websocketApiUrl,
  }
})
