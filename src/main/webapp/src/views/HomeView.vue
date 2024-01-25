<script setup lang="ts">
import BottomNavigation from '@/components/BottomNavigation.vue';
import FileDialog from '@/components/dialogs/FileDialog.vue';
import SettingsDialog from '@/components/dialogs/SettingsDialog.vue';
import InformationDrawer from '@/components/drawers/InformationDrawer.vue';
import NavigationDrawer from '@/components/drawers/NavigationDrawer.vue';
import FilesLayout from '@/components/layouts/FilesLayout.vue';
import { useConfigurationStore } from '@/stores/configurationStore.ts';
import { storeToRefs } from 'pinia';
import { useDisplay } from 'vuetify';

const { mobile } = useDisplay();

const configurationStore = useConfigurationStore();
const { files } = storeToRefs(configurationStore);
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
    <settings-dialog />
  </v-layout>
</template>
