import { useFileStore } from '@/stores/fileStore.ts';
import { Tabs } from '@/types/enums/Tabs.ts';
import { defineStore, storeToRefs } from 'pinia';
import { computed, ref } from 'vue';

export const useHomeStore = defineStore('home', () => {
  const fileStore = useFileStore();

  /* -- Drawer -- */

  /**
   * Information drawer state
   */
  const isDrawer = ref<boolean>(false);

  /**
   * Drawer tab state
   */
  const drawerTab = ref<Tabs>(Tabs.Information);

  /* -- Dialogs -- */

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
  const isDelete = ref<boolean>(false);
  const deleteTitle = computed<string | undefined>(() => {
    const { file } = storeToRefs(fileStore);

    return file.value ? `"${file.value.title}"` : undefined;
  });

  /* -- Display -- */

  /**
   * Search input value
   */
  const search = ref<string | undefined>();

  /**
   * Files diplay state
   */
  const isGrid = ref<boolean>(false);

  /* -- Store actions -- */

  /**
   * Reset home
   */
  const reset = (): void => {
    const { file } = storeToRefs(fileStore);

    // Clear current file
    file.value = undefined;

    // Close and reset drawer tab
    isDrawer.value = false;
    drawerTab.value = Tabs.Information;

    // Close dialogs
    isNew.value = false;
    isRoom.value = false;
    isShareInRoom.value = false;
    isDelete.value = false;

    // Clear search
    search.value = undefined;
  };

  return {
    isDrawer,
    drawerTab,
    isNew,
    isRoom,
    isShareInRoom,
    isDelete,
    deleteTitle,
    search,
    isGrid,
    reset,
  };
});
