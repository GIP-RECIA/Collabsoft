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
// @ts-expect-error project location
import type { AssociatedApp } from '@/types'

const associatedApp1: AssociatedApp = {
  id: 1,
  enabled: true,
  slug: 'tldraw',
  primaryColor: null,
  iconPath: null,
  extension: 'tldr',
  type: 'json',
}

const associatedApp2: AssociatedApp = {
  id: 2,
  enabled: false,
  slug: 'wisemapping',
  primaryColor: null,
  iconPath: null,
  extension: 'wxml',
  type: 'xml',
}

export { associatedApp1, associatedApp2 }
