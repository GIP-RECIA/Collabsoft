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

import { Tabs } from '@/types/enums'
import { useSessionStorage } from '@vueuse/core'
import { defineStore, storeToRefs } from 'pinia'
import { computed, ref } from 'vue'
import { useFileStore } from './fileStore.ts'

export const useHomeStore = defineStore('home', () => {
  const fileStore = useFileStore()

  /* -- Drawer -- */

  /**
   * Information drawer state
   */
  const isDrawer = ref<boolean>(false)

  /**
   * Drawer tab state
   */
  const drawerTab = ref<Tabs>(Tabs.Information)

  /* -- Dialogs -- */

  /**
   * Dialog new file state
   */
  const isNew = ref<boolean>(false)

  /**
   * Dialog create/join room state
   */
  const isRoom = ref<boolean>(false)

  /**
   * Dialog share room state
   */
  const isShareInRoom = ref<boolean>(false)

  /**
   * Dialog confirmation delete state
   */
  const isDelete = ref<boolean>(false)
  const deleteTitle = computed<string | undefined>(() => {
    const { file } = storeToRefs(fileStore)

    return file.value ? `"${file.value.title}"` : undefined
  })

  /* -- Display -- */

  /**
   * Search input value
   */
  const search = ref<string | undefined>()

  /**
   * Files diplay state
   */
  const isGrid = useSessionStorage<boolean>(`${__APP_SLUG__}.is-grid`, false)

  /* -- Store actions -- */

  /**
   * Reset home
   */
  const reset = (): void => {
    const { file } = storeToRefs(fileStore)

    // Clear current file
    file.value = undefined

    // Close and reset drawer tab
    isDrawer.value = false
    drawerTab.value = Tabs.Information

    // Close dialogs
    isNew.value = false
    isRoom.value = false
    isShareInRoom.value = false
    isDelete.value = false

    // Clear search
    search.value = undefined
  }

  return {
    isDrawer,
    drawerTab,
    isNew,
    isRoom,
    isShareInRoom,
    isDelete,
    deleteTitle,
    search,
    isGrid,
    reset,
  }
})
