import { getConfiguration } from '@/services/configurationService.ts';
import { useFileStore } from '@/stores/fileStore.ts';
import type { Configuration } from '@/types/configurationType.ts';
import { Tabs } from '@/types/enums/Tabs.ts';
import type { Soffit } from '@/types/soffitType.ts';
import { errorHandler } from '@/utils/axiosUtils.ts';
import { defineStore, storeToRefs } from 'pinia';
import { computed, ref } from 'vue';

export const useConfigurationStore = defineStore('configuration', () => {
  const fileStore = useFileStore();

  /* -- Configuration store -- */

  const configuration = ref<Configuration | undefined>();

  /**
   * Initialise `configuration`
   */
  const init = async (): Promise<void> => {
    if (!isInit.value) {
      try {
        const response = await getConfiguration();
        configuration.value = response.data;
      } catch (e) {
        errorHandler(e, 'initConfigurationStore');
      }
    }
  };

  const isInit = computed<boolean>(() => configuration.value != undefined);

  /* -- Gestion de l'utilisateur -- */

  const user = ref<Soffit>({
    sub: 'guest',
  });

  const isSoffitOk = computed<boolean>(() => !user.value.sub.startsWith('guest'));

  /* -- Gestion de la navigation -- */

  const lastNavigation = ref<string | undefined>();

  const isApp = ref<boolean>(false);

  const isWsOk = ref<boolean>(true);

  /* -- Gestion de l'accueil -- */

  const search = ref<string | undefined>();

  const isInfo = ref<boolean>(false);

  const currentTab = ref<number>(Tabs.Information);

  const isNew = ref<boolean>(false);

  const isJoin = ref<boolean>(false);

  const isConfirmation = ref<boolean>(false);
  const confirmationTitle = computed<string | undefined>(() => {
    const { currentFile } = storeToRefs(fileStore);

    return currentFile.value ? `"${currentFile.value.title}"` : undefined;
  });

  const resetState = (): void => {
    const { currentFile } = storeToRefs(fileStore);

    search.value = undefined;
    currentFile.value = undefined;
    isInfo.value = false;
    isConfirmation.value = false;
    isNew.value = false;
    isJoin.value = false;
  };

  const isGrid = ref<boolean>(false);

  /* -- Gestion des paramètres -- */

  const isSettings = ref<boolean>(false);

  return {
    init,
    isInit,
    user,
    isSoffitOk,
    lastNavigation,
    isApp,
    isWsOk,
    search,
    isInfo,
    currentTab,
    isNew,
    isJoin,
    isConfirmation,
    confirmationTitle,
    resetState,
    isGrid,
    isSettings,
  };
});
