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
import { plugins } from '../config/index.ts';
// @ts-ignore
import DurationSpan from '@/components/DurationSpan.vue';
import { VueWrapper, mount } from '@vue/test-utils';
import { beforeEach, describe, expect, it } from 'vitest';

global.ResizeObserver = require('resize-observer-polyfill');

describe('DurationSpan', () => {
  let date: Date;
  let wrapper: VueWrapper;

  beforeEach(() => {
    date = new Date();
    wrapper = mount(DurationSpan, {
      global: {
        plugins: [...plugins],
      },
      props: {
        date: date.toString(),
      },
    });
  });

  it('test 1 - seconds', async () => {
    expect(wrapper.text()).toBe('1 second ago');

    setTimeout(() => {
      expect(wrapper.text()).toBe('30 seconds ago');
    }, 30000);

    date.setSeconds(date.getSeconds() - 59);
    await wrapper.setProps({ date: date.toString() });
    expect(wrapper.text()).toBe('59 seconds ago');
  });

  it('test 2 - minutes', async () => {
    date.setMinutes(date.getMinutes() - 59);
    await wrapper.setProps({ date: date.toString() });
    expect(wrapper.text()).toBe('59 minutes ago');
  });

  it('test 3 - hours', async () => {
    date.setHours(date.getHours() - 23);
    await wrapper.setProps({ date: date.toString() });
    expect(wrapper.text()).toBe('23 hours ago');
  });

  it('test 4 - days', async () => {
    date.setDate(date.getDate() - 29);
    await wrapper.setProps({ date: date.toString() });
    expect(wrapper.text()).toBe('29 days ago');
  });

  it('test 5 - months', async () => {
    date.setMonth(date.getMonth() - 11);
    await wrapper.setProps({ date: date.toString() });
    expect(wrapper.text()).toBe('11 months ago');
  });

  it('test 7 - years', async () => {
    date.setFullYear(date.getFullYear() - 2);
    await wrapper.setProps({ date: date.toString() });
    expect(wrapper.text()).toBe('2 years ago');
  });
});
