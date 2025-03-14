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

import type { AssociatedApp } from './associatedAppType.ts'
import type { Collaboration } from './collaborationType.ts'
import type { History } from './historyType.ts'
import type { User } from './userType.ts'

export interface File {
  id: number
  uuid: string
  title: string
  description: string | null
  data: any
  creator: User
  creationDate: string
  lastEditor: User
  editionDate: string
  associatedApp: AssociatedApp
  pub: boolean
  collaborations?: Array<Collaboration>
  histories?: Array<History>
}
