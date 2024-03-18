import { getConfiguration } from '@/services/configurationService.ts';
import type { Configuration } from '@/types/configurationType.ts';
import type { Soffit } from '@/types/soffitType.ts';
import { errorHandler, initToken } from '@/utils/axiosUtils.ts';
import { useEntTheme } from '@/utils/entUtils.ts';
import { defineStore } from 'pinia';
import { computed, ref } from 'vue';

export const useConfigurationStore = defineStore('configuration', () => {
  const configuration = ref<Configuration | undefined>();

  /**
   * Initialise `configuration`
   */
  const init = async (force: boolean = false): Promise<boolean> => {
    if (force || !isInit.value) {
      try {
        const response = await getConfiguration();
        configuration.value = response.data;
        await initToken(configuration.value!.front.userInfoApiUrl);
        await useEntTheme(configuration.value!.front.extendedUportalHeader.templateApiPath);

        return true;
      } catch (e) {
        errorHandler(e);
      }
    }
    return false;
  };

  const isInit = computed<boolean>(() => configuration.value != undefined);

  const isReady = computed<boolean>(() => isInit.value && isSoffitOk.value);

  /* -- Gestion de l'utilisateur -- */

  const user = ref<Soffit>({ sub: 'guest' });

  const isSoffitOk = computed<boolean>(() => !user.value.sub.startsWith('guest'));

  /* -- Gestion de la navigation -- */

  const lastNavigation = ref<string | undefined>();

  /**
   * Dialog settings state
   */
  const isSettings = ref<boolean>(false);

  return {
    configuration,
    init,
    isInit,
    isReady,
    user,
    isSoffitOk,
    lastNavigation,
    isSettings,
  };
});
