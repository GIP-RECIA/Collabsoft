import type { AppSlug } from '@/types/enums/AppSlug.ts';
import { useSessionStorage } from '@vueuse/core';
import { defineStore } from 'pinia';
import { ref } from 'vue';
import { useRouter } from 'vue-router';

export const useAppStore = defineStore('app', () => {
  const router = useRouter();

  /**
   * App context state
   */
  const isApp = ref<boolean>(false);

  /* -- Rooms -- */

  const ownedRooms = useSessionStorage<
    Array<{ roomId: string; appType: AppSlug; fileId: number | undefined; saveOnFile: boolean }>
  >(`${__APP_SLUG__}.owned-rooms`, []);

  /**
   * Id of file to load in the room
   */
  const initFileId = ref<number | undefined>();

  const initRoom = (roomId: string, appType: AppSlug, fileId: number | undefined, saveOnFile: boolean): void => {
    initFileId.value = fileId;
    ownedRooms.value.push({ roomId, appType, fileId, saveOnFile });
    router.push({ name: `collaborative-${appType}`, params: { roomId } });
  };

  /* -- Store actions -- */

  const reset = (): void => {
    initFileId.value = undefined;
  };

  return {
    isApp,
    ownedRooms,
    initFileId,
    initRoom,
    reset,
  };
});
