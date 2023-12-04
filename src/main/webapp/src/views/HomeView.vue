<script setup lang="ts">
import BottomNavigation from '@/components/BottomNavigation.vue';
import FileDialog from '@/components/dialogs/FileDialog.vue';
import SettingsDialog from '@/components/dialogs/SettingsDialog.vue';
import InformationDrawer from '@/components/drawers/InformationDrawer.vue';
import NavigationDrawer from '@/components/drawers/NavigationDrawer.vue';
import { Navigation } from '@/types/enums/Navigation';
import { useRoute, useRouter } from 'vue-router';

const route = useRoute();
const router = useRouter();

if (route.name == 'home') router.replace({ name: Navigation.projects });
</script>

<template>
  <v-layout full-height>
    <navigation-drawer />
    <v-main>
      <v-container fluid class="h-100 overflow-y-auto">
        <div class="h-0">
          <router-view />
        </div>
      </v-container>
    </v-main>
    <bottom-navigation />
    <information-drawer />
    <file-dialog />
    <settings-dialog />
  </v-layout>
</template>
