import { getConfiguration } from '@/services/configurationService';
import type { Configuration } from '@/types/configurationType';
import { errorHandler } from '@/utils/axiosUtils';
import { defineStore } from 'pinia';
import { computed, ref } from 'vue';

export const useConfigurationStore = defineStore('configuration', () => {
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

  /* -- Gestion des fichier -- */

  const selectedFile = ref<number | undefined>();
  const isSelectedFile = computed<boolean>(() => selectedFile.value != undefined);
  const isShare = ref<boolean>(false);
  const isInfo = ref<boolean>(false);
  const isConfirmation = ref<boolean>(false);

  const resetState = () => {
    selectedFile.value = undefined;
    isShare.value = false;
    isInfo.value = false;
    isConfirmation.value = false;
  };

  /* -- Gestion des paramètres -- */

  const isSettings = ref<boolean>(false);

  return {
    init,
    isInit,
    selectedFile,
    isSelectedFile,
    isShare,
    isInfo,
    isConfirmation,
    resetState,
    isSettings,
  };
});
