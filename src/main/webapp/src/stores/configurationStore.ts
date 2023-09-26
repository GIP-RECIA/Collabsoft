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
    try {
      const response = await getConfiguration();
      configuration.value = response.data;
    } catch (e) {
      errorHandler(e, 'initConfigurationStore');
    }
  };

  const isInit = computed<boolean>(() => configuration.value != undefined);

  /* -- Gestion des paramètres -- */

  const isSettings = ref<boolean>(false);

  return {
    init,
    isInit,
    isSettings,
  };
});
