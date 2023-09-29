<script setup lang="ts">
import SettingsDialog from '@/components/dialogs/SettingsDialog.vue';
import InformationDrawer from '@/components/drawers/InformationDrawer.vue';
import NavigationDrawer from '@/components/drawers/NavigationDrawer.vue';
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
    <information-drawer />
    <settings-dialog />
  </v-layout>
</template>
