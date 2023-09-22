<script setup lang="ts">
import InformationDrawer from '@/components/drawers/InformationDrawer.vue';
import NavigationDrawer from '@/components/drawers/NavigationDrawer.vue';
import SettingsModal from '@/components/modals/SettingsModal.vue';
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
    <settings-modal />
  </v-layout>
</template>
