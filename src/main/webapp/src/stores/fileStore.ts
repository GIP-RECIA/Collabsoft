import { getFile, getFiles, getPublic, getShared, getStarred } from '@/services/fileService.ts';
import { useConfigurationStore } from '@/stores/configurationStore.ts';
import { Navigation } from '@/types/enums/Navigation.ts';
import type { FileBody } from '@/types/fileBodyType.ts';
import type { File } from '@/types/fileType.ts';
import { errorHandler } from '@/utils/axiosUtils.ts';
import { differenceInMilliseconds } from 'date-fns';
import debounce from 'lodash.debounce';
import { defineStore, storeToRefs } from 'pinia';
import { ref } from 'vue';

export const useFileStore = defineStore('file', () => {
  const configurationStore = useConfigurationStore();

  /* -- Files -- */

  let lastUpdated = new Date();

  /**
   * List of files
   */
  const files = ref<Array<File> | undefined>();

  /**
   * Load files for a Navigation
   */
  const loadFiles = debounce(async (requestedFiles: Navigation): Promise<void> => {
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
      errorHandler(e, 'loadFiles');
    }
    lastUpdated = new Date();
  }, 200);

  /**
   * Refresh files
   */
  const refreshFiles = (instant: boolean = false, loading: boolean = false): void => {
    const { lastNavigation } = storeToRefs(configurationStore);

    if (instant || differenceInMilliseconds(new Date(), lastUpdated) > 5000) {
      if (loading) files.value = undefined;
      if (lastNavigation.value != undefined) loadFiles(lastNavigation.value as Navigation);
    }
  };

  /**
   * Add a file
   */
  const addFile = (file: File): void => {
    files.value?.push(file);
  };

  /**
   * Delete a file
   */
  const deleteFile = (fileId: number): void => {
    const index = files.value?.findIndex((file) => file.id === fileId);
    if (index && index > -1) files.value?.splice(index, 1);
  };

  /**
   * Set a file title, description and pub form store ref files, file or both
   */
  const setFile = (from: 'files' | 'file' | 'both', fileId: number | undefined, value: FileBody): void => {
    const _setFile = (file: File | undefined, value: FileBody): void => {
      if (!file) return;
      if (value.title) file.title = value.title;
      if (value.description) file.description = value.description;
      if (value.pub) file.pub = value.pub;
      file.editionDate = new Date() as unknown as string;
    };

    switch (from) {
      case 'files':
        _setFile(
          files.value?.find((file) => file.id === fileId),
          value,
        );
        break;
      case 'file':
        _setFile(file.value, value);
        break;
      case 'both':
        _setFile(
          files.value?.find((file) => file.id === fileId),
          value,
        );
        _setFile(file.value, value);
        break;
    }
  };

  /* -- File -- */

  /**
   * Current selected file
   */
  const file = ref<File>();

  /**
   * Load current file
   */
  const loadFile = async (fileId: number, force: boolean = false): Promise<void> => {
    if (!file.value || file.value.id != fileId || force) {
      try {
        const response = await getFile(fileId);
        file.value = response.data;
      } catch (e) {
        errorHandler(e, 'loadFile');
      }
    }
  };

  /**
   * Refresh current file
   */
  const refreshFile = (): void => {
    if (file.value) loadFile(file.value.id, true);
  };

  return {
    files,
    refreshFiles,
    addFile,
    deleteFile,
    setFile,
    file,
    loadFile,
    refreshFile,
  };
});
