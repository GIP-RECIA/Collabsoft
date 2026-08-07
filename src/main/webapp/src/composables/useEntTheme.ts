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

import { onMounted, onUnmounted } from 'vue'
import vuetify from '@/plugins/vuetify.ts'
import { Theme } from '@/types/enums/index.ts'

export function useEntTheme() {
  let lastPrimary = ''
  let lastPrimaryDark = ''

  function setTheme(): void {
    const style = getComputedStyle(document.body)

    const primary = style
      .getPropertyValue('--recia-primary')
      .trim()

    if (primary && primary !== lastPrimary) {
      vuetify.theme.themes.value[Theme.light].colors.primary = primary
      lastPrimary = primary
    }

    const primaryDark = style
      .getPropertyValue('--recia-primary-dark')
      .trim()

    if (primaryDark && primaryDark !== lastPrimaryDark) {
      vuetify.theme.themes.value[Theme.dark].colors.primary = primaryDark
      lastPrimaryDark = primaryDark
    }
  }

  const observer = new MutationObserver(setTheme)

  onMounted(() => {
    setTheme()

    observer.observe(document.body, {
      attributes: true,
      attributeFilter: ['class'],
    })
  })

  onUnmounted(() => {
    observer.disconnect()
  })
}
