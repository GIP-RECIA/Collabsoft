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
import { FontAwesomeIcon, plugins, registerFontAwsome } from '../../../config/index.ts';
// @ts-ignore
import CollaborativeTldrawView from '@/views/apps/wisemapping/CollaborativeTldrawView.vue';
import { createTestingPinia } from '@pinia/testing';
import { VueWrapper, mount } from '@vue/test-utils';
import { afterEach, beforeAll, beforeEach, describe, expect, it } from 'vitest';

global.ResizeObserver = require('resize-observer-polyfill');

// TODO
describe('CollaborativeTldrawView', () => {
  const pinia = createTestingPinia();
  let wrapper: VueWrapper;

  beforeAll(() => {
    registerFontAwsome();
  });

  beforeEach(() => {
    // wrapper = mount(CollaborativeTldrawView, {
    //   global: {
    //     plugins: [...plugins, pinia],
    //     stubs: { FontAwesomeIcon },
    //   },
    // });
  });

  afterEach(() => {
    // wrapper.unmount();
  });

  it('test 1 - ', async () => {
    // expect(true).toBe(true);
  });
});
