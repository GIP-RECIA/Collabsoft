<script setup lang="ts">
import BottomNavigation from '@/components/BottomNavigation.vue';
import FileDialog from '@/components/dialogs/FileDialog.vue';
import JoinDialog from '@/components/dialogs/JoinDialog.vue';
import InformationDrawer from '@/components/drawers/InformationDrawer.vue';
import NavigationDrawer from '@/components/drawers/NavigationDrawer.vue';
import FilesLayout from '@/components/layouts/FilesLayout.vue';
import { useFileStore } from '@/stores/fileStore.ts';
import { storeToRefs } from 'pinia';
import { useDisplay } from 'vuetify';

const { mobile } = useDisplay();

const fileStore = useFileStore();
const { files } = storeToRefs(fileStore);
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
    <join-dialog />
  </v-layout>
</template>
