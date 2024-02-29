import { useFileStore } from '@/stores/fileStore.ts';
import type { AppSlug } from '@/types/enums/AppSlug.ts';
import { useSessionStorage } from '@vueuse/core';
import { defineStore, storeToRefs } from 'pinia';
import { computed, ref } from 'vue';
import { type RouteLocationRaw, useRouter } from 'vue-router';

export const useAppStore = defineStore('app', () => {
  const router = useRouter();
  const fileStore = useFileStore();

  /* -- App -- */

  /**
   * App context state
   */
  const isApp = ref<boolean>(false);

  /**
   * Room context state
   */
  const isRoom = computed<boolean>(() => _roomId.value != undefined);

  /**
   * Type of app state
   */
  const _appType = ref<AppSlug | undefined>();

  /**
   * File / Room title
   */
  const title = computed<string>(() => {
    const { file } = storeToRefs(fileStore);

    const fileName = file.value ? file.value.title : '';

    return isRoom.value ? _roomId.value! : fileName;
  });

  /**
   * Auto save state
   */
  const _autoSave = ref<boolean>(true);
  const isAutoSave = computed<boolean>({
    get() {
      return _room.value ? _room.value.saveOnFile : _autoSave.value;
    },
    set(value) {
      if (canAutoSave.value) _autoSave.value = value;
      if (_room.value?.fileId) _room.value.saveOnFile = value;
    },
  });

  /**
   * Auto save is available state
   */
  const canAutoSave = computed<boolean>(() => {
    return !isRoom.value ? true : _room.value?.fileId != undefined;
  });

  /* -- File -- */

  /**
   * Current file id
   */
  const _fileId = ref<number | undefined>();

  /* -- Room -- */

  /**
   * Current room id
   */
  const _roomId = ref<string | undefined>();

  /**
   * List of owned rooms
   */
  const _ownedRooms = useSessionStorage<
    Array<{ roomId: string; appType: AppSlug; fileId: number | undefined; saveOnFile: boolean }>
  >(`${__APP_SLUG__}.owned-rooms`, []);

  /**
   * Room information if owner
   */
  const _room = computed(() =>
    _ownedRooms.value.find((room) => room.roomId == _roomId.value && room.appType == _appType.value),
  );

  /**
   * Chech if user is the owner of the room
   */
  const isRoomOwner = computed<boolean>(() => _room.value != undefined);

  /**
   * Id of file to load in the room
   */
  const initRoomFileId = ref<number | undefined>();

  /**
   * Initialize room and navigate to it
   */
  const initRoom = (
    roomId: string,
    appType: AppSlug,
    fileId: number | undefined,
    saveOnFile: boolean = false,
  ): void => {
    initRoomFileId.value = fileId;
    _ownedRooms.value.push({ roomId, appType, fileId, saveOnFile });
    const route: RouteLocationRaw = { name: `collaborative-${appType}`, params: { roomId } };
    isApp.value ? router.replace(route) : router.push(route);
  };

  /* -- Store actions -- */

  /**
   * Initialize app context
   */
  const initAppContext = (roomId: string | undefined, fileId: number | undefined, routeName: string): void => {
    const { loadFile } = fileStore;
    const { file } = storeToRefs(fileStore);

    isApp.value = true;
    _fileId.value = fileId;
    _roomId.value = roomId;
    _appType.value = routeName.startsWith('collaborative-')
      ? (routeName.substring('collaborative-'.length) as AppSlug)
      : (routeName as AppSlug);

    if (fileId != undefined) {
      if (!file.value) loadFile(fileId);
    } else file.value = undefined;
  };

  /**
   * Exit app context
   */
  const exitAppContext = (): void => {
    isApp.value = false;
    _fileId.value = undefined;
    _roomId.value = undefined;
    _appType.value = undefined;
    initRoomFileId.value = undefined;
  };

  return {
    isApp,
    isRoom,
    title,
    isAutoSave,
    canAutoSave,
    roomId: computed<string | undefined>(() => _roomId.value),
    isRoomOwner,
    initRoomFileId,
    initRoom,
    initAppContext,
    exitAppContext,
  };
});
