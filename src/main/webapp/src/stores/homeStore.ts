import { useFileStore } from '@/stores/fileStore.ts';
import { Tabs } from '@/types/enums/Tabs.ts';
import { defineStore, storeToRefs } from 'pinia';
import { computed, ref } from 'vue';

export const useHomeStore = defineStore('home', () => {
  const fileStore = useFileStore();

  /**
   * Search input value
   */
  const search = ref<string | undefined>();

  /**
   * Information drawer state
   */
  const isInfo = ref<boolean>(false);

  /**
   * Drawer tab state
   */
  const currentTab = ref<Tabs>(Tabs.Information);

  /**
   * Dialog new file state
   */
  const isNew = ref<boolean>(false);

  /**
   * Dialog create/join room state
   */
  const isRoom = ref<boolean>(false);

  /**
   * Dialog share room state
   */
  const isShareInRoom = ref<boolean>(false);

  /**
   * Dialog confirmation delete state
   */
  const isConfirmation = ref<boolean>(false);
  const confirmationTitle = computed<string | undefined>(() => {
    const { currentFile } = storeToRefs(fileStore);

    return currentFile.value ? `"${currentFile.value.title}"` : undefined;
  });

  /**
   * Reset all values before this function and current file
   */
  const resetState = (): void => {
    const { currentFile } = storeToRefs(fileStore);

    currentFile.value = undefined;
    search.value = undefined;
    isInfo.value = false;
    currentTab.value = Tabs.Information;
    isNew.value = false;
    isRoom.value = false;
    isShareInRoom.value = false;
    isConfirmation.value = false;
  };

  /**
   * Files diplay state
   */
  const isGrid = ref<boolean>(false);

  return {
    search,
    isInfo,
    currentTab,
    isNew,
    isRoom,
    isShareInRoom,
    isConfirmation,
    confirmationTitle,
    resetState,
    isGrid,
  };
});
