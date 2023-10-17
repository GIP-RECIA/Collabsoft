import { getConfiguration } from '@/services/configurationService';
import { getFile, getFiles, getPublic, getShared, getStarred } from '@/services/fileService';
import type { Configuration } from '@/types/configurationType';
import { Navigation } from '@/types/enums/Navigation';
import { Tabs } from '@/types/enums/Tabs';
import { errorHandler } from '@/utils/axiosUtils';
import { differenceInMilliseconds } from 'date-fns';
import debounce from 'lodash.debounce';
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

  /* -- Gestion de la navigation -- */

  const lastNavigation = ref<string | undefined>();

  /* -- Gestion des fichier -- */

  let lastUpdated = new Date();

  const files = ref<Array<any> | undefined>();

  const loadFiles = async (requestedFiles: Navigation | string) => {
    try {
      let response;
      switch (requestedFiles) {
        case Navigation.projects:
          response = await getFiles();
          break;
        case Navigation.favorites:
          response = await getStarred();
          break;
        case Navigation.shared:
          response = await getShared();
          break;
        case Navigation.public:
          response = await getPublic();
          break;
      }
      if (response) files.value = response.data;
    } catch (e) {
      errorHandler(e);
    }
  };

  const refresh = (instant?: boolean, loading?: boolean) => {
    if (instant || differenceInMilliseconds(new Date(), lastUpdated) > 5000) {
      if (loading) files.value = undefined;
      const launch = debounce(() => {
        if (lastNavigation.value != undefined) {
          loadFiles(lastNavigation.value);
          lastUpdated = new Date();
        }
      }, 200);
      launch();
    }
  };

  const currentFile = ref<any>();

  const loadFile = async (fileId: number) => {
    if (!currentFile.value || currentFile.value.id != fileId) {
      try {
        const response = await getFile(fileId);
        currentFile.value = response.data;
      } catch (e) {
        errorHandler(e);
      }
    }
  };

  const isInfo = ref<boolean>(false);
  const currentTab = ref<number>(Tabs.Information);
  const isConfirmation = ref<boolean>(false);
  const isNew = ref<boolean>(false);

  const resetState = () => {
    currentFile.value = undefined;
    isInfo.value = false;
    isConfirmation.value = false;
  };

  /* -- Gestion des paramètres -- */

  const isSettings = ref<boolean>(false);

  return {
    init,
    isInit,
    lastNavigation,
    files,
    loadFiles,
    refresh,
    currentFile,
    loadFile,
    isInfo,
    currentTab,
    isConfirmation,
    isNew,
    resetState,
    isSettings,
  };
});
