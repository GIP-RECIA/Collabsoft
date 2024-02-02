<script setup lang="ts">
import ConfirmationDialog from '@/components/dialogs/ConfirmationDialog.vue';
import LoginDialog from '@/components/dialogs/LoginDialog.vue';
import SettingsDialog from '@/components/dialogs/SettingsDialog.vue';
import { deleteFile } from '@/services/fileService.ts';
import { useConfigurationStore } from '@/stores/configurationStore.ts';
import { useFileStore } from '@/stores/fileStore.ts';
import type { Confirmation } from '@/types/confirmationType.ts';
import { errorHandler } from '@/utils/axiosUtils.ts';
import { watchOnce } from '@vueuse/core';
import { storeToRefs } from 'pinia';
import { computed, onBeforeMount } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRouter } from 'vue-router';

const configurationStore = useConfigurationStore();
const { resetState } = configurationStore;
const { isReady, lastNavigation, isConfirmation, confirmationTitle } = storeToRefs(configurationStore);

const fileStore = useFileStore();
const { refresh } = fileStore;
const { currentFile } = storeToRefs(fileStore);

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
        :domain="domain"
        :service-name="appName"
        context-api-url="/portail"
        sign-out-url="/portail/Logout"
        default-org-logo-path="/annuaire_images/default_banner_v1.jpg"
        default-avatar-path="/images/icones/noPictureUser.svg"
        default-org-icon-path="/images/partners/netocentre-simple.svg"
        favorite-api-url="/portail/api/layout"
        layout-api-url="/portail/api/v4-3/dlm/layout.json"
        organization-api-url="/change-etablissement/rest/v2/structures/structs/"
        portlet-api-url="/portail/api/v4-3/dlm/portletRegistry.json?category=All%20categories"
        user-info-api-url="/portail/api/v5-1/userinfo?claims=private,name,ESCOSIRENCourant,ESCOSIREN&groups="
        user-info-portlet-url="/portail/p/ESCO-MCE"
        session-api-url="/portail/api/session.json"
        template-api-path="/commun/portal_template_api.tpl.json"
        switch-org-portlet-url="/portail/p/etablissement-swapper"
        favorites-portlet-card-size="small"
        grid-portlet-card-size="auto"
        hide-action-mode="never"
        show-favorites-in-slider="true"
        return-home-title="Aller à l'accueil"
        return-home-target="_self"
        icon-type="nine-square"
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
      <extended-uportal-footer :domain="domain" template-api-path="/commun/portal_template_api.tpl.json" />
    </footer>
  </v-app>
</template>

<style scoped lang="scss">
header {
  z-index: 1000;
}
</style>
