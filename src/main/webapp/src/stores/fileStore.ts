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
import type { File, FileBody } from '@/types'
import { getFile, getFiles, getPublic, getShared, getStarred } from '@/services/api'
import { Navigation } from '@/types/enums'
import { errorHandler } from '@/utils'
import { differenceInMilliseconds } from 'date-fns'
import debounce from 'lodash.debounce'
import { defineStore, storeToRefs } from 'pinia'
import { ref } from 'vue'
import { useConfigurationStore } from './configurationStore.ts'

export const useFileStore = defineStore('file', () => {
  const configurationStore = useConfigurationStore()

  /* -- Files -- */

  let lastUpdated = new Date()

  /**
   * List of files
   */
  const files = ref<Array<File> | undefined>()

  /**
   * Load files for a Navigation
   */
  const loadFiles = debounce(async (requestedFiles: Navigation): Promise<void> => {
    try {
      let response
      switch (requestedFiles) {
        case Navigation.projects:
          response = await getFiles()
          break
        case Navigation.favorites:
          response = await getStarred()
          break
        case Navigation.shared:
          response = await getShared()
          break
        case Navigation.public:
          response = await getPublic()
          break
      }
      if (response)
        files.value = response.data
    }
    catch (e) {
      errorHandler(e, 'loadFiles')
    }
    lastUpdated = new Date()
  }, 200)

  /**
   * Refresh files
   */
  const refreshFiles = (instant: boolean = false, loading: boolean = false): void => {
    const { lastNavigation } = storeToRefs(configurationStore)

    if (instant || differenceInMilliseconds(new Date(), lastUpdated) > 5000) {
      if (loading)
        files.value = undefined
      if (lastNavigation.value !== undefined)
        loadFiles(lastNavigation.value as Navigation)
    }
  }

  /**
   * Add a file
   */
  const addFile = (file: File): void => {
    files.value?.push(file)
  }

  /**
   * Delete a file
   */
  const deleteFile = (fileId: number): void => {
    if (!files.value)
      return
    const index = files.value.findIndex(file => file.id === fileId)
    if (index > -1)
      files.value.splice(index, 1)
  }

  /**
   * Set a file title, description and pub form store ref files, file or both
   */
  const setFile = (from: 'files' | 'file' | 'both', fileId: number | undefined, value: FileBody): void => {
    const _setFile = (file: File | undefined, value: FileBody): void => {
      if (!file)
        return
      if (value.title)
        file.title = value.title
      if (value.description)
        file.description = value.description
      if (value.pub)
        file.pub = value.pub
      file.editionDate = new Date() as unknown as string
    }

    switch (from) {
      case 'files':
        _setFile(
          files.value?.find(file => file.id === fileId),
          value,
        )
        break
      case 'file':
        _setFile(file.value, value)
        break
      case 'both':
        _setFile(
          files.value?.find(file => file.id === fileId),
          value,
        )
        _setFile(file.value, value)
        break
    }
  }

  /* -- File -- */

  /**
   * Current selected file
   */
  const file = ref<File>()

  /**
   * Load current file
   */
  const loadFile = async (fileId: number, force: boolean = false): Promise<void> => {
    if (!file.value || file.value.id !== fileId || force) {
      try {
        const response = await getFile(fileId)
        file.value = response.data
      }
      catch (e) {
        errorHandler(e, 'loadFile')
      }
    }
  }

  /**
   * Refresh current file
   */
  const refreshFile = (): void => {
    if (file.value)
      loadFile(file.value.id, true)
  }

  return {
    files,
    refreshFiles,
    addFile,
    deleteFile,
    setFile,
    file,
    loadFile,
    refreshFile,
  }
})
