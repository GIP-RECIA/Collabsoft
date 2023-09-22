import { getConfiguration } from '@/services/configurationService';
import type { Configuration } from '@/types/configurationType';
import type { Identity } from '@/types/identityType';
import { errorHandler } from '@/utils/axiosUtils';
import isEmpty from 'lodash.isempty';
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

  /* -- Gestion de l'authentification -- */

  const identity = ref<Identity | undefined>();
  const isAuthenticated = computed<boolean>(() => !isEmpty(identity.value));
  const isAdmin = computed<boolean>(() => {
    const hasRoleAdmin = identity.value?.roles.includes('ROLE_ADMIN');

    return hasRoleAdmin != undefined && hasRoleAdmin;
  });

  return {
    init,
    isInit,
    isSettings,
    identity,
    isAuthenticated,
    isAdmin,
  };
});
