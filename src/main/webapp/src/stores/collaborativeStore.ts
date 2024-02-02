import { defineStore } from 'pinia';
import { ref } from 'vue';

export const useCollaborativeStore = defineStore('collaborative', () => {
  const initFileId = ref<number | undefined>();

  return {
    initFileId,
  };
});
