<script setup lang="ts">
import ConfirmationDialog from '@/components/dialogs/ConfirmationDialog.vue';
import { deleteFile } from '@/services/fileService';
import { useConfigurationStore } from '@/stores/configurationStore';
import { Response } from '@/types/enums/Response';
import { errorHandler } from '@/utils/axiosUtils';
import { usePreferredDark } from '@vueuse/core';
import { storeToRefs } from 'pinia';
import { computed, onBeforeMount, watch } from 'vue';
import { useRouter } from 'vue-router';
import { useTheme } from 'vuetify';

const configurationStore = useConfigurationStore();
const { refresh, resetState } = configurationStore;
const { lastNavigation, currentFile, isConfirmation } = storeToRefs(configurationStore);

const router = useRouter();

router.beforeEach((to) => {
  resetState();
  if (to.name != undefined && to.name != null) lastNavigation.value = to.name as string;
  refresh(true, true);
});

const theme = useTheme();
const isDark = usePreferredDark();

watch(isDark, (newValue) => {
  theme.global.name.value = !newValue ? 'light' : 'dark';
});

onBeforeMount(() => {
  let extendedUportalHeaderScript = document.createElement('script');
  extendedUportalHeaderScript.setAttribute('src', '/commun/extended-uportal-header.min.js');
  document.head.appendChild(extendedUportalHeaderScript);
  let extendedUportalFooterScript = document.createElement('script');
  extendedUportalFooterScript.setAttribute('src', '/commun/extended-uportal-footer.min.js');
  document.head.appendChild(extendedUportalFooterScript);
});

const domain = window.location.hostname;

const confirmationDelete = computed<boolean>({
  get() {
    return currentFile.value != undefined && isConfirmation.value;
  },
  set(newValue) {
    isConfirmation.value = newValue;
  },
});

const deleteItem = async (result: Response) => {
  if (result == Response.yes && currentFile.value) {
    try {
      await deleteFile(currentFile.value.id);
      refresh(true);
    } catch (e) {
      errorHandler(e);
    }
  }
};
</script>

<template>
  <v-app class="app-container">
    <header>
      <extended-uportal-header
        :domain="domain"
        service-name="Collabsoft"
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
      <router-view />
      <confirmation-dialog
        v-model="confirmationDelete"
        title=""
        yes-value="button.delete"
        no-value="button.cancel"
        @close="deleteItem"
      />
    </main>
    <footer>
      <extended-uportal-footer :domain="domain" template-api-path="/commun/portal_template_api.tpl.json" />
    </footer>
  </v-app>
</template>

<style scoped lang="scss">
.app-container {
  height: 100vh;
  width: 100vw;
}

extended-uportal-footer {
  display: block;
}
</style>
