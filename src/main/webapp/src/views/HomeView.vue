<script setup lang="ts">
import ConfirmationDialog from '@/components/dialogs/ConfirmationDialog.vue';
import SettingsDialog from '@/components/dialogs/SettingsDialog.vue';
import ShareDialog from '@/components/dialogs/ShareDialog.vue';
import InformationDrawer from '@/components/drawers/InformationDrawer.vue';
import NavigationDrawer from '@/components/drawers/NavigationDrawer.vue';
import { deleteFile } from '@/services/fileService';
import { useConfigurationStore } from '@/stores/configurationStore';
import { Response } from '@/types/enums/Response';
import { errorHandler } from '@/utils/axiosUtils';
import { storeToRefs } from 'pinia';
import { computed } from 'vue';
import { useRouter } from 'vue-router';

const configurationStore = useConfigurationStore();
const { resetState } = configurationStore;
const { selectedFile, isSelectedFile, isConfirmation } = storeToRefs(configurationStore);

const router = useRouter();

router.beforeEach(() => {
  resetState();
});

const confirmationDelete = computed<boolean>({
  get() {
    return isSelectedFile.value && isConfirmation.value;
  },
  set(newValue) {
    isConfirmation.value = newValue;
  },
});

const deleteItem = async (result: Response) => {
  if (result == Response.yes && isSelectedFile.value) {
    try {
      await deleteFile(selectedFile.value!);
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
      <v-container fluid>
        <router-view />
      </v-container>
    </v-main>
    <information-drawer />
    <settings-dialog />
    <share-dialog />
    <confirmation-dialog
      v-model="confirmationDelete"
      title=""
      yes-value="button.delete"
      no-value="button.cancel"
      @close="deleteItem"
    />
  </v-layout>
</template>
