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
import type { File } from '@/types'
import DurationSpan from '@/components/DurationSpan.vue'
import FileMenu from '@/components/FileMenu.vue'
import { useHomeStore } from '@/stores'
import { useSessionStorage } from '@vueuse/core'
import { storeToRefs } from 'pinia'
import { ref, watch } from 'vue'
import { useI18n } from 'vue-i18n'
import { useDisplay } from 'vuetify'

defineProps<{
  files: Array<File> | undefined
}>()

const isDev = import.meta.env.DEV

const { VITE_API_URI } = import.meta.env

const homeStore = useHomeStore()
const { search } = storeToRefs(homeStore)

const { t } = useI18n()
const { xs } = useDisplay()

const headers = ref<Array<any>>([
  { title: t('information.title'), key: 'title', sortable: true, width: '100%' },
  { title: '', key: 'actions', sortable: false, align: 'end' },
])

function addColumnEditionDate(): void {
  const index = headers.value.findIndex(header => header.key === 'editionDate')
  if (index === -1) {
    headers.value.splice(1, 0, {
      title: t('information.edited'),
      key: 'editionDate',
      sortable: true,
      headerProps: {
        style: 'white-space: nowrap;',
      },
      cellProps: {
        style: 'white-space: nowrap;',
      },
    })
  }
}

function removeColumnEditionDate(): void {
  const index = headers.value.findIndex(header => header.key === 'editionDate')
  if (index >= 0)
    headers.value.splice(index, 1)
}

watch(xs, (newValue): void => (newValue ? removeColumnEditionDate() : addColumnEditionDate()), { immediate: true })

const sortBy = useSessionStorage<Array<any>>(`${__APP_SLUG__}.sort-by`, [{ key: 'title', order: 'asc' }])
</script>

<!-- eslint-disable vue/valid-v-slot -->
<template>
  <v-data-table
    v-model:sort-by="sortBy"
    :headers="headers"
    :items="files"
    :search="search"
    filter-keys="title"
    :loading="files === undefined"
    :items-per-page="-1"
    sort-asc-icon="fas fa-sort-up"
    sort-desc-icon="fas fa-sort-down"
    height="0px"
    fixed-header
    hover
    class="h-100 rounded-xl"
  >
    <template #item.title="{ item }">
      <router-link
        :to="item.associatedApp.enabled || isDev ? { name: item.associatedApp.slug, params: { fileId: item.id } } : ''"
        class="v-data-table-column-title"
      >
        <v-icon
          icon="fas fa-file"
          :image="
            item.associatedApp.iconPath != null
              ? `${VITE_API_URI}${VITE_API_URI.endsWith('/') ? '' : '/'}${item.associatedApp.iconPath}`
              : undefined
          "
          class="text-medium-emphasis ms-2"
        />
        <span class="text-truncated ms-4">{{ item.title }}</span>
      </router-link>
    </template>
    <template #item.editionDate="{ item }">
      <DurationSpan :date="item.editionDate" />
    </template>
    <template #item.actions="{ item }">
      <FileMenu :file-id="item.id" />
    </template>
    <template #loading>
      <v-skeleton-loader type="table-row@6" />
    </template>
    <template #bottom />
  </v-data-table>
</template>

<style scoped lang="scss">
.v-data-table-column-title {
  display: flex;
  align-items: center;
  flex-grow: 1;
  width: 0;
  text-decoration: none;
  color: inherit;
}
</style>

<style lang="scss">
.v-data-table-header__sort-icon {
  margin-left: 8px;
}

tbody > tr > td:nth-child(1) {
  display: flex;
}
</style>
