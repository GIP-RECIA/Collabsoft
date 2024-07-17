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
import { file1 } from '../config/samples.ts';
// @ts-ignore
import { useFileStore } from '@/stores/fileStore.ts';
// @ts-ignore
import { useHomeStore } from '@/stores/homeStore.ts';
// @ts-ignore
import { Tabs } from '@/types/enums/Tabs.ts';
import { createPinia, setActivePinia, storeToRefs } from 'pinia';
import { beforeEach, describe, expect, it } from 'vitest';

describe('homeStore', () => {
  beforeEach(() => {
    setActivePinia(createPinia());
  });

  it('reset', () => {
    const fileStore = useFileStore();
    const { file } = storeToRefs(fileStore);

    const homeStore = useHomeStore();
    const { reset } = homeStore;
    const { isDrawer, drawerTab, isNew, isRoom, isShareInRoom, isDelete, search } = storeToRefs(homeStore);

    fileStore.$patch({
      file: file1,
    });
    homeStore.$patch({
      isDrawer: true,
      drawerTab: Tabs.Share,
      isNew: true,
      isRoom: true,
      isShareInRoom: true,
      isDelete: true,
      search: 'foo',
    });
    expect(file.value).toStrictEqual(file1);
    reset();
    expect(file.value).toBeUndefined();
    expect(isDrawer.value).toBe(false);
    expect(drawerTab.value).toBe(Tabs.Information);
    expect(isNew.value).toBe(false);
    expect(isRoom.value).toBe(false);
    expect(isShareInRoom.value).toBe(false);
    expect(isDelete.value).toBe(false);
    expect(search.value).toBe(undefined);
  });
});
