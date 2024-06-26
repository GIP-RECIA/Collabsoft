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
import BottomNavigation from '@/components/BottomNavigation.vue';
import ConfirmationDialog from '@/components/dialogs/ConfirmationDialog.vue';
import FileDialog from '@/components/dialogs/FileDialog.vue';
import RoomDialog from '@/components/dialogs/RoomDialog.vue';
import NavigationDrawer from '@/components/drawers/NavigationDrawer.vue';
import InformationDrawer from '@/components/drawers/information/InformationDrawer.vue';
import FilesLayout from '@/components/layouts/FilesLayout.vue';
import { deleteFile } from '@/services/api/fileService.ts';
import { useFileStore } from '@/stores/fileStore.ts';
import { useHomeStore } from '@/stores/homeStore.ts';
import type { Confirmation } from '@/types/confirmationType.ts';
import { errorHandler } from '@/utils/axiosUtils.ts';
import { storeToRefs } from 'pinia';
import { computed } from 'vue';
import { useI18n } from 'vue-i18n';
import { useDisplay } from 'vuetify';

const fileStore = useFileStore();
const { deleteFile: deleteFileFromStore } = fileStore;
const { files, file } = storeToRefs(fileStore);

const homeStore = useHomeStore();
const { isDelete, deleteTitle } = storeToRefs(homeStore);

const { t } = useI18n();

const { mobile } = useDisplay();

const confirmationDelete = computed<boolean>({
  get() {
    return file.value != undefined && isDelete.value;
  },
  set(newValue) {
    isDelete.value = newValue;
  },
});

const deleteItem = async (result: Confirmation): Promise<void> => {
  if (result === 'yes' && file.value) {
    try {
      await deleteFile(file.value.id);
      deleteFileFromStore(file.value.id);
    } catch (e) {
      errorHandler(e);
    }
  }
};
</script>

<template>
  <v-layout full-height>
    <navigation-drawer />
    <v-main>
      <v-container fluid class="h-100 d-flex flex-column" :class="[mobile ? 'pa-2' : '']">
        <router-view />
        <files-layout :files="files" />
      </v-container>
    </v-main>
    <bottom-navigation />
    <information-drawer />
    <file-dialog />
    <room-dialog />
    <confirmation-dialog
      v-model="confirmationDelete"
      :title="t('dialog.delete.title')"
      :description="deleteTitle"
      yes-value="button.delete"
      no-value="button.cancel"
      yes-color="error"
      no-color="secondary"
      @close="deleteItem"
    />
  </v-layout>
</template>
