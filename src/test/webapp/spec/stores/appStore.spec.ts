/**
 * Copyright (C) 2023 GIP-RECIA, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
// @ts-ignore
import { useAppStore } from '@/stores/appStore.ts';
import { createPinia, setActivePinia, storeToRefs } from 'pinia';
import { setTimeout } from 'timers';
import { beforeEach, describe, expect, it } from 'vitest';

// TODO
describe('appStore', () => {
  beforeEach(() => {
    setActivePinia(createPinia());
  });

  it('initRoom', () => {
    // const appStore = useAppStore();
    // const { initRoom } = appStore;
    // const {
    //   isApp,
    //   isRoom,
    //   title,
    //   isAutoSave,
    //   canAutoSave,
    //   roomId,
    //   room,
    //   isRoomOwner,
    //   initRoomFileId,
    //   destroy,
    //   websocketApiUrl,
    // } = storeToRefs(appStore);

    // initRoom();
    expect(true).toBe(true);
  });

  it('joinRoom', () => {
    // const appStore = useAppStore();
    // const { joinRoom } = appStore;
    // const {
    //   isApp,
    //   isRoom,
    //   title,
    //   isAutoSave,
    //   canAutoSave,
    //   roomId,
    //   room,
    //   isRoomOwner,
    //   initRoomFileId,
    //   destroy,
    //   websocketApiUrl,
    // } = storeToRefs(appStore);

    // joinRoom();
    expect(true).toBe(true);
  });

  it('destroyRoom app context', () => {
    const appStore = useAppStore();
    const { destroyRoom } = appStore;
    const { destroy } = storeToRefs(appStore);

    appStore.$patch({ isApp: true });
    destroyRoom();
    expect(destroy.value).toBe(false);
  });

  it('destroyRoom room context', () => {
    const appStore = useAppStore();
    const { destroyRoom } = appStore;
    const { destroy } = storeToRefs(appStore);

    appStore.$patch({ isRoom: true });
    destroyRoom();
    expect(destroy.value).toBe(false);
  });

  it('destroyRoom app and room context', () => {
    const appStore = useAppStore();
    const { destroyRoom } = appStore;
    const { destroy } = storeToRefs(appStore);

    appStore.$patch({ isApp: true, isRoom: true });
    destroyRoom();
    setTimeout(() => {
      expect(destroy.value).toBe(true);
    }, 10);
    setTimeout(() => {
      expect(destroy.value).toBe(false);
    }, 250);
  });

  it('initAppContext', () => {
    // const appStore = useAppStore();
    // const { initAppContext } = appStore;
    // const {
    //   isApp,
    //   isRoom,
    //   title,
    //   isAutoSave,
    //   canAutoSave,
    //   roomId,
    //   room,
    //   isRoomOwner,
    //   initRoomFileId,
    //   destroy,
    //   websocketApiUrl,
    // } = storeToRefs(appStore);

    // initAppContext();
    expect(true).toBe(true);
  });

  it('exitAppContext', () => {
    // const appStore = useAppStore();
    // const { exitAppContext } = appStore;
    // const {
    //   isApp,
    //   isRoom,
    //   title,
    //   isAutoSave,
    //   canAutoSave,
    //   roomId,
    //   room,
    //   isRoomOwner,
    //   initRoomFileId,
    //   destroy,
    //   websocketApiUrl,
    // } = storeToRefs(appStore);

    // exitAppContext();
    expect(true).toBe(true);
  });
});
