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
import { ref, watch } from 'vue'
import { useI18n } from 'vue-i18n'
import ShareInRoomDialog from '@/components/dialogs/ShareInRoomDialog.vue'
import HistoriesTab from '@/components/drawers/information/tabs/HistoriesTab.vue'
import InformationTab from '@/components/drawers/information/tabs/InformationTab.vue'
import ShareTab from '@/components/drawers/information/tabs/ShareTab.vue'
import DurationSpan from '@/components/DurationSpan.vue'
import { useFileStore, useHomeStore } from '@/stores'
import { Tabs } from '@/types/enums'

const { VITE_API_URI } = import.meta.env

const isDev = import.meta.env.DEV

const fileStore = useFileStore()
const { file } = storeToRefs(fileStore)

const homeStore = useHomeStore()
const { isDrawer, drawerTab } = storeToRefs(homeStore)

const { t } = useI18n()

const show = ref<boolean>(false)

function onClose(): void {
  isDrawer.value = false
}

const reset = debounce((): void => {
  show.value = false
  drawerTab.value = Tabs.Information
}, 200)

watch(isDrawer, (newValue) => {
  if (newValue)
    show.value = true
  else reset()
})
</script>

<template>
  <v-navigation-drawer v-model="isDrawer" location="right" :width="460" temporary class="pa-2">
    <div v-show="show">
      <v-toolbar :title="t(`navigation.title.${drawerTab}`)" color="rgba(255, 255, 255, 0)">
        <template v-if="file" #title>
          <div class="d-flex align-center">
            <v-icon
              icon="fas fa-file"
              :image="
                file.associatedApp.iconPath != null
                  ? `${VITE_API_URI}${VITE_API_URI.endsWith('/') ? '' : '/'}${file.associatedApp.iconPath}`
                  : undefined
              "
              class="text-medium-emphasis me-2"
            />
            <div class="overflow-hidden">
              <div class="text-body-1 font-weight-bold text-truncated">
                {{ file.title }}
              </div>
              <div class="text-caption text-medium-emphasis">
                <DurationSpan :date="file.editionDate" />
              </div>
            </div>
          </div>
        </template>
        <template #append>
          <v-btn icon="fas fa-xmark" color="default" variant="plain" :alt="t('button.close')" @click="onClose" />
        </template>
      </v-toolbar>

      <v-tabs
        v-model="drawerTab"
        align-tabs="center"
        :show-arrows="false"
        hide-slider
        selected-class="slide-group-item--activate"
        fixed-tabs
        class="mb-3"
      >
        <v-tab :value="Tabs.Information" :title="t('navigation.title.information')" class="text-medium-emphasis">
          <v-icon icon="fas fa-circle-info" />
        </v-tab>
        <v-tab :value="Tabs.Share" :title="t('navigation.title.share')" class="text-medium-emphasis">
          <v-icon icon="fas fa-share-nodes" />
        </v-tab>
        <v-tab
          v-if="isDev"
          :value="Tabs.Histories"
          :title="t('navigation.title.histories')"
          class="text-medium-emphasis"
        >
          <v-icon icon="fas fa-clock-rotate-left" />
        </v-tab>
      </v-tabs>
      <v-window v-model="drawerTab">
        <v-window-item :value="Tabs.Information">
          <InformationTab />
        </v-window-item>
        <v-window-item :value="Tabs.Share">
          <ShareTab />
        </v-window-item>
        <v-window-item v-if="isDev" :value="Tabs.Histories">
          <HistoriesTab />
        </v-window-item>
      </v-window>
    </div>
  </v-navigation-drawer>

  <ShareInRoomDialog />
</template>

<style scoped lang="scss">
.slide-group-item--activate {
  background-color: rgba(var(--v-theme-primary), var(--v-activated-opacity)) !important;
}
</style>
