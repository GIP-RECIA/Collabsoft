<script setup lang="ts">
import ConfirmationDialog from '@/components/dialogs/ConfirmationDialog.vue';
import LoginDialog from '@/components/dialogs/LoginDialog.vue';
import SettingsDialog from '@/components/dialogs/SettingsDialog.vue';
import { deleteFile } from '@/services/fileService.ts';
import { useConfigurationStore } from '@/stores/configurationStore.ts';
import { useFileStore } from '@/stores/fileStore.ts';
import { useHomeStore } from '@/stores/homeStore.ts';
import type { Confirmation } from '@/types/confirmationType.ts';
import { errorHandler } from '@/utils/axiosUtils.ts';
import { watchOnce } from '@vueuse/core';
import { storeToRefs } from 'pinia';
import { computed, onBeforeMount } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRouter } from 'vue-router';

const configurationStore = useConfigurationStore();
const { configuration, isInit, isReady, lastNavigation } = storeToRefs(configurationStore);

const fileStore = useFileStore();
const { refresh } = fileStore;
const { currentFile } = storeToRefs(fileStore);

const homeStore = useHomeStore();
const { resetState } = homeStore;
const { isConfirmation, confirmationTitle } = storeToRefs(homeStore);

const { t } = useI18n();
const router = useRouter();

router.beforeEach((to) => {
  resetState();
  if (to.name != undefined && to.name != null) lastNavigation.value = to.name as string;
  if (isReady.value) refresh(true, true);
});

watchOnce(isReady, (newValue) => {
  if (newValue) refresh(true, true);
});

onBeforeMount(() => {
  document.title = __APP_NAME__;

  let extendedUportalHeaderScript = document.createElement('script');
  extendedUportalHeaderScript.setAttribute('src', '/commun/extended-uportal-header.min.js');
  document.head.appendChild(extendedUportalHeaderScript);
  let extendedUportalFooterScript = document.createElement('script');
  extendedUportalFooterScript.setAttribute('src', '/commun/extended-uportal-footer.min.js');
  document.head.appendChild(extendedUportalFooterScript);
});

const confirmationDelete = computed<boolean>({
  get() {
    return currentFile.value != undefined && isConfirmation.value;
  },
  set(newValue) {
    isConfirmation.value = newValue;
  },
});

const deleteItem = async (result: Confirmation): Promise<void> => {
  if (result === 'yes' && currentFile.value) {
    try {
      await deleteFile(currentFile.value.id);
      refresh(true);
    } catch (e) {
      errorHandler(e);
    }
  }
};

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
        :context-api-url="configuration?.front.extendedUportalHeader.contextApiUrl"
        :sign-out-url="configuration?.front.extendedUportalHeader.signOutUrl"
        :default-org-logo-path="configuration?.front.extendedUportalHeader.defaultOrgLogoPath"
        :default-avatar-path="configuration?.front.extendedUportalHeader.defaultAvatarPath"
        :default-org-icon-path="configuration?.front.extendedUportalHeader.defaultOrgIconPath"
        :favorite-api-url="configuration?.front.extendedUportalHeader.favoriteApiUrl"
        :layout-api-url="configuration?.front.extendedUportalHeader.layoutApiUrl"
        :organization-api-url="configuration?.front.extendedUportalHeader.organizationApiUrl"
        :portlet-api-url="configuration?.front.extendedUportalHeader.portletApiUrl"
        :user-info-api-url="configuration?.front.extendedUportalHeader.userInfoApiUrl"
        :user-info-portlet-url="configuration?.front.extendedUportalHeader.userInfoPortletUrl"
        :session-api-url="configuration?.front.extendedUportalHeader.sessionApiUrl"
        :template-api-path="configuration?.front.extendedUportalHeader.templateApiPath"
        :switch-org-portlet-url="configuration?.front.extendedUportalHeader.switchOrgPortletUrl"
        :favorites-portlet-card-size="configuration?.front.extendedUportalHeader.favoritesPortletCardSize"
        :grid-portlet-card-size="configuration?.front.extendedUportalHeader.gridPortletCardSize"
        :hide-action-mode="configuration?.front.extendedUportalHeader.hideActionMode"
        :show-favorites-in-slider="configuration?.front.extendedUportalHeader.showFavoritesInSlider"
        :return-home-title="configuration?.front.extendedUportalHeader.returnHomeTitle"
        :return-home-target="configuration?.front.extendedUportalHeader.returnHomeTarget"
        :icon-type="configuration?.front.extendedUportalHeader.iconType"
      />
    </header>
    <main class="h-100">
      <router-view v-if="isReady" />
      <login-dialog />
      <settings-dialog />
      <confirmation-dialog
        v-model="confirmationDelete"
        :title="t('dialog.delete.title')"
        :description="confirmationTitle"
        yes-value="button.delete"
        no-value="button.cancel"
        yes-color="error"
        no-color="secondary"
        @close="deleteItem"
      />
    </main>
    <footer>
      <extended-uportal-footer
        v-if="isInit"
        :domain="domain"
        :template-api-path="configuration?.front.extendedUportalFooter.templateApiPath"
      />
    </footer>
  </v-app>
</template>

<style scoped lang="scss">
header {
  z-index: 1000;
}
</style>
