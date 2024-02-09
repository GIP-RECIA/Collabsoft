import { defineStore } from 'pinia';
import { ref } from 'vue';

export const useCollaborativeStore = defineStore('collaborative', () => {
  /**
   * Id of file to load in the room
   */
  const initFileId = ref<number | undefined>();

  return {
    initFileId,
  };
});
