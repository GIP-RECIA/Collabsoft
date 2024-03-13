<script setup lang="ts">
import LoginDialog from '@/components/dialogs/LoginDialog.vue';
import SettingsDialog from '@/components/dialogs/SettingsDialog.vue';
import { useAppStore } from '@/stores/appStore.ts';
import { useConfigurationStore } from '@/stores/configurationStore.ts';
import { useFileStore } from '@/stores/fileStore.ts';
import { useHomeStore } from '@/stores/homeStore.ts';
import { watchOnce } from '@vueuse/core';
import { storeToRefs } from 'pinia';
import { onBeforeMount } from 'vue';
import { useRouter } from 'vue-router';

const appStore = useAppStore();
const { isApp } = storeToRefs(appStore);

const configurationStore = useConfigurationStore();
const { configuration, isInit, isReady, lastNavigation } = storeToRefs(configurationStore);

const fileStore = useFileStore();
const { refreshFiles } = fileStore;

const homeStore = useHomeStore();
const { reset: resetHome } = homeStore;
const router = useRouter();

router.beforeEach((to) => {
  resetHome();
  if (to.name != undefined && to.name != null) lastNavigation.value = to.name as string;
  if (isReady.value) refreshFiles(true, true);
});

watchOnce(isInit, (newValue) => {
  if (!newValue) return;

  let extendedUportalHeaderScript = document.createElement('script');
  extendedUportalHeaderScript.setAttribute('src', configuration.value!.front.extendedUportalHeader.componentPath);
  document.head.appendChild(extendedUportalHeaderScript);
  let extendedUportalFooterScript = document.createElement('script');
  extendedUportalFooterScript.setAttribute('src', configuration.value!.front.extendedUportalFooter.componentPath);
  document.head.appendChild(extendedUportalFooterScript);
});

watchOnce(isReady, (newValue) => {
  if (newValue) refreshFiles(true, true);
});

onBeforeMount(() => {
  document.title = __APP_NAME__;
});

const appName = __APP_NAME__;
const domain = window.location.hostname;
</script>

<template>
  <v-app class="app-container">
    <header>
      <extended-uportal-header
        v-if="isInit"
        :domain="domain"
        :service-name="appName"
        :context-api-url="configuration!.front.extendedUportalHeader.contextApiUrl"
        :sign-out-url="configuration!.front.extendedUportalHeader.signOutUrl"
        :default-org-logo-path="configuration!.front.extendedUportalHeader.defaultOrgLogoPath"
        :default-avatar-path="configuration!.front.extendedUportalHeader.defaultAvatarPath"
        :default-org-icon-path="configuration!.front.extendedUportalHeader.defaultOrgIconPath"
        :favorite-api-url="configuration!.front.extendedUportalHeader.favoriteApiUrl"
        :layout-api-url="configuration!.front.extendedUportalHeader.layoutApiUrl"
        :organization-api-url="configuration!.front.extendedUportalHeader.organizationApiUrl"
        :portlet-api-url="configuration!.front.extendedUportalHeader.portletApiUrl"
        :user-info-api-url="configuration!.front.extendedUportalHeader.userInfoApiUrl"
        :user-info-portlet-url="configuration!.front.extendedUportalHeader.userInfoPortletUrl"
        :session-api-url="configuration!.front.extendedUportalHeader.sessionApiUrl"
        :template-api-path="configuration!.front.extendedUportalHeader.templateApiPath"
        :switch-org-portlet-url="configuration!.front.extendedUportalHeader.switchOrgPortletUrl"
        :favorites-portlet-card-size="configuration!.front.extendedUportalHeader.favoritesPortletCardSize"
        :grid-portlet-card-size="configuration!.front.extendedUportalHeader.gridPortletCardSize"
        :hide-action-mode="configuration!.front.extendedUportalHeader.hideActionMode"
        :show-favorites-in-slider="configuration!.front.extendedUportalHeader.showFavoritesInSlider"
        :return-home-title="configuration!.front.extendedUportalHeader.returnHomeTitle"
        :return-home-target="configuration!.front.extendedUportalHeader.returnHomeTarget"
        :icon-type="configuration!.front.extendedUportalHeader.iconType"
      />
    </header>
    <main class="h-100">
      <router-view v-if="isReady" />
      <login-dialog />
      <settings-dialog />
    </main>
    <footer>
      <extended-uportal-footer
        v-if="isInit"
        v-show="!isApp"
        :domain="domain"
        :template-api-path="configuration!.front.extendedUportalFooter.templateApiPath"
      />
    </footer>
  </v-app>
</template>

<style scoped lang="scss">
header {
  z-index: 1000;
}
</style>
