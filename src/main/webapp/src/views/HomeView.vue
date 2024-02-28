<script setup lang="ts">
import BottomNavigation from '@/components/BottomNavigation.vue';
import ConfirmationDialog from '@/components/dialogs/ConfirmationDialog.vue';
import FileDialog from '@/components/dialogs/FileDialog.vue';
import RoomDialog from '@/components/dialogs/RoomDialog.vue';
import NavigationDrawer from '@/components/drawers/NavigationDrawer.vue';
import InformationDrawer from '@/components/drawers/information/InformationDrawer.vue';
import FilesLayout from '@/components/layouts/FilesLayout.vue';
import { deleteFile } from '@/services/fileService.ts';
import { useFileStore } from '@/stores/fileStore.ts';
import { useHomeStore } from '@/stores/homeStore.ts';
import type { Confirmation } from '@/types/confirmationType.ts';
import { errorHandler } from '@/utils/axiosUtils.ts';
import { storeToRefs } from 'pinia';
import { computed } from 'vue';
import { useI18n } from 'vue-i18n';
import { useDisplay } from 'vuetify';

const fileStore = useFileStore();
const { refreshFiles } = fileStore;
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
      refreshFiles(true);
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
      <v-container fluid :class="['h-100', 'd-flex', 'flex-column', mobile ? 'pa-2' : '']">
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
