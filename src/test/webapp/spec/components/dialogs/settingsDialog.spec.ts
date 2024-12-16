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

import type { VueWrapper } from '@vue/test-utils'
// @ts-expect-error project location
import SettingsDialog from '@/components/dialogs/SettingsDialog.vue'
// @ts-expect-error project location
import { useConfigurationStore } from '@/stores'
import { ResizeObserver } from '@juggle/resize-observer'
import { createTestingPinia } from '@pinia/testing'
import { flushPromises, mount } from '@vue/test-utils'
import { afterEach, beforeAll, beforeEach, describe, expect, it } from 'vitest'
import { FontAwesomeIcon, plugins, registerFontAwsome } from '../../config/index.ts'

globalThis.ResizeObserver = ResizeObserver

// TODO
describe('settingsDialog', () => {
  const pinia = createTestingPinia()
  let configurationStore
  let wrapper: VueWrapper
  const dialog = () => wrapper.getComponent(SettingsDialog)

  beforeAll(() => {
    configurationStore = useConfigurationStore(pinia)

    registerFontAwsome()
  })

  beforeEach(() => {
    const el = document.createElement('div')
    el.id = 'modal'
    document.body.appendChild(el)

    wrapper = mount(SettingsDialog, {
      global: {
        plugins: [...plugins, pinia],
        stubs: { FontAwesomeIcon },
      },
    })
  })

  afterEach(() => {
    wrapper.unmount()
    document.body.innerHTML = ''
  })

  it('test 1 - initial state', async () => {
    expect(dialog().html()).toBe('')
  })

  it('test 2 - open modal', async () => {
    configurationStore.isSettings = true
    await flushPromises()
    expect(dialog().html()).not.toBe('')
  })
})
