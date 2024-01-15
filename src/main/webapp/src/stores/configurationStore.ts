import { getConfiguration } from '@/services/configurationService.ts';
import { getFile, getFiles, getPublic, getShared, getStarred } from '@/services/fileService.ts';
import type { Configuration } from '@/types/configurationType.ts';
import { Navigation } from '@/types/enums/Navigation.ts';
import { Tabs } from '@/types/enums/Tabs.ts';
import type { File } from '@/types/fileType.ts';
import { errorHandler } from '@/utils/axiosUtils.ts';
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

  const files = ref<Array<File> | undefined>();

  const loadFiles = debounce(async (requestedFiles: Navigation | string): Promise<void> => {
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
    lastUpdated = new Date();
  }, 200);

  const refresh = (instant?: boolean, loading?: boolean): void => {
    if (instant || differenceInMilliseconds(new Date(), lastUpdated) > 5000) {
      if (loading) files.value = undefined;
      if (lastNavigation.value != undefined) loadFiles(lastNavigation.value);
    }
  };

  const currentFile = ref<File>();

  const loadFile = async (fileId: number, force?: boolean): Promise<void> => {
    if (!currentFile.value || currentFile.value.id != fileId || force) {
      try {
        const response = await getFile(fileId);
        currentFile.value = response.data;
      } catch (e) {
        errorHandler(e);
      }
    }
  };

  const refreshCurrentFile = (): void => {
    if (currentFile.value) loadFile(currentFile.value.id, true);
  };

  const isInfo = ref<boolean>(false);
  const currentTab = ref<number>(Tabs.Information);
  const isConfirmation = ref<boolean>(false);
  const confirmationTitle = computed<string | undefined>(() =>
    currentFile.value ? `"${currentFile.value.title}"` : undefined,
  );
  const isNew = ref<boolean>(false);

  const resetState = (): void => {
    currentFile.value = undefined;
    isInfo.value = false;
    isConfirmation.value = false;
  };

  /* -- Gestion de l'affichage -- */

  const isGrid = ref<boolean>(false);

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
    refreshCurrentFile,
    isInfo,
    currentTab,
    isConfirmation,
    confirmationTitle,
    isNew,
    resetState,
    isGrid,
    isSettings,
  };
});
