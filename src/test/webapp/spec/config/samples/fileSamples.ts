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
import type { File } from '@/types'
import { associatedApp1 } from './assocatedAppSamples.ts'
import { user1, user2, user3 } from './userSamples.ts'

const date = '2024-07-15 17:50:00.000'

const file1: File = {
  id: 1,
  uuid: 'file1',
  title: 'file1',
  description: null,
  data: '',
  creator: user1,
  creationDate: date,
  lastEditor: user1,
  editionDate: date,
  associatedApp: associatedApp1,
  pub: false,
  collaborations: undefined,
  histories: undefined,
}

const file2: File = {
  id: 2,
  uuid: 'file2',
  title: 'file2',
  description: null,
  data: '',
  creator: user1,
  creationDate: date,
  lastEditor: user1,
  editionDate: date,
  associatedApp: associatedApp1,
  pub: false,
  collaborations: undefined,
  histories: undefined,
}

const file3: File = {
  id: 3,
  uuid: 'file3',
  title: 'file3',
  description: null,
  data: '',
  creator: user2,
  creationDate: date,
  lastEditor: user2,
  editionDate: date,
  associatedApp: associatedApp1,
  pub: false,
  collaborations: undefined,
  histories: undefined,
}

const file4: File = {
  id: 4,
  uuid: 'file4',
  title: 'file4',
  description: null,
  data: '',
  creator: user2,
  creationDate: date,
  lastEditor: user2,
  editionDate: date,
  associatedApp: associatedApp1,
  pub: true,
  collaborations: undefined,
  histories: undefined,
}

const file5: File = {
  id: 5,
  uuid: 'file5',
  title: 'file5',
  description: null,
  data: '',
  creator: user3,
  creationDate: date,
  lastEditor: user3,
  editionDate: date,
  associatedApp: associatedApp1,
  pub: false,
  collaborations: undefined,
  histories: undefined,
}

const file6: File = {
  id: 6,
  uuid: 'file6',
  title: 'file6',
  description: null,
  data: '',
  creator: user3,
  creationDate: date,
  lastEditor: user3,
  editionDate: date,
  associatedApp: associatedApp1,
  pub: false,
  collaborations: undefined,
  histories: undefined,
}

export { file1, file2, file3, file4, file5, file6 }
