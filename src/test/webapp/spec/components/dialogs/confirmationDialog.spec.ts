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
import { ResizeObserver } from '@juggle/resize-observer'
import { plugins } from '../../config/index.ts'
// @ts-expect-error project location
import ConfirmationDialog from '@/components/dialogs/ConfirmationDialog.vue'
import { mount } from '@vue/test-utils'
import { afterEach, beforeEach, describe, expect, it } from 'vitest'
import { VBtn, VCardText } from 'vuetify/components'

globalThis.ResizeObserver = ResizeObserver

describe('confirmationDialog', () => {
  let wrapper: VueWrapper
  const dialog = () => wrapper.getComponent(ConfirmationDialog)

  beforeEach(() => {
    const el = document.createElement('div')
    el.id = 'modal'
    document.body.appendChild(el)

    wrapper = mount(ConfirmationDialog, {
      global: {
        plugins: [...plugins],
      },
      props: {
        'onUpdate:modelValue': async (modelValue: any) => await wrapper.setProps({ modelValue }),
        'title': 'Foo',
        'description': 'foo bar',
        'yesValue': 'button.add',
        'noValue': 'button.save',
        'cancelable': true,
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
    await wrapper.setProps({ modelValue: true })
    expect(dialog().html()).not.toBe('')
  })

  it('test 3 - description', async () => {
    await wrapper.setProps({ modelValue: true })
    expect(dialog().getComponent(VCardText).html()).toContain('foo bar')

    await wrapper.setProps({ description: undefined })
    expect(dialog().findComponent(VCardText).exists()).toBe(false)
  })

  it('test 4 - buttons', async () => {
    await wrapper.setProps({ modelValue: true })
    expect(dialog().findAllComponents(VBtn)[0].html()).toContain('Cancel')
    expect(dialog().findAllComponents(VBtn)[1].html()).toContain('Save')
    expect(dialog().findAllComponents(VBtn)[2].html()).toContain('Add')

    await wrapper.setProps({ cancelable: false })
    expect(dialog().findAllComponents(VBtn)).toHaveLength(2)
  })

  it('test 5 - yes action', async () => {
    await wrapper.setProps({ modelValue: true })
    await dialog().findAllComponents(VBtn)[2].trigger('click')
    expect(wrapper.emitted('close')![0]).toEqual(['yes'])
  })

  it('test 6 - no action', async () => {
    await wrapper.setProps({ modelValue: true })
    await dialog().findAllComponents(VBtn)[1].trigger('click')
    expect(wrapper.emitted('close')![0]).toEqual(['no'])
  })

  it('test 7 - cancel action', async () => {
    await wrapper.setProps({ modelValue: true })
    await dialog().findAllComponents(VBtn)[0].trigger('click')
    expect(wrapper.emitted('close')![0]).toEqual(['cancel'])
  })
})
