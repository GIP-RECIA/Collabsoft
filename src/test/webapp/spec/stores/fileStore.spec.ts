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
import { file1, file2 } from '../config/samples.ts';
// @ts-ignore
import { useConfigurationStore } from '@/stores/configurationStore.ts';
// @ts-ignore
import { useFileStore } from '@/stores/fileStore.ts';
import { createPinia, setActivePinia, storeToRefs } from 'pinia';
import { beforeEach, describe, expect, it } from 'vitest';

// TODO
describe('fileStore', () => {
  beforeEach(() => {
    setActivePinia(createPinia());
  });

  it('refreshFiles', () => {
    const fileStore = useFileStore();
    const { files } = storeToRefs(fileStore);
    const { refreshFiles } = fileStore;

    // refreshFiles();
    expect(true).toBe(true);
  });

  it('addFile', () => {
    const fileStore = useFileStore();
    const { addFile } = fileStore;
    const { files, file } = storeToRefs(fileStore);

    // addFile();
    expect(true).toBe(true);
  });

  it('deleteFile', () => {
    const fileStore = useFileStore();
    const { deleteFile } = fileStore;
    const { files } = storeToRefs(fileStore);

    // deleteFile();
    expect(true).toBe(true);
  });

  it('setFile files', () => {
    const fileStore = useFileStore();
    const { setFile } = fileStore;
    const { files } = storeToRefs(fileStore);

    // setFile();
    expect(true).toBe(true);
  });

  it('setFile file', () => {
    const fileStore = useFileStore();
    const { setFile } = fileStore;
    const { file } = storeToRefs(fileStore);

    // setFile();
    expect(true).toBe(true);
  });

  it('setFile both', () => {
    const fileStore = useFileStore();
    const { setFile } = fileStore;
    const { files, file } = storeToRefs(fileStore);

    // setFile();
    expect(true).toBe(true);
  });

  it('loadFile', () => {
    const fileStore = useFileStore();
    const { loadFile } = fileStore;
    const { file } = storeToRefs(fileStore);

    // loadFile();
    expect(true).toBe(true);
  });

  it('refreshFile', () => {
    const fileStore = useFileStore();
    const { refreshFile } = fileStore;
    const { file } = storeToRefs(fileStore);

    // refreshFile();
    expect(true).toBe(true);
  });
});
