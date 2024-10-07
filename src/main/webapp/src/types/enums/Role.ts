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

export enum Role {
  owner = 'OWNER',
  editor = 'EDITOR',
  readonly = 'READONLY',
}

function getRole(role: string): Role {
  switch (role.toUpperCase()) {
    case Role.owner.toString():
      return Role.owner
    case Role.editor.toString():
      return Role.editor
    case Role.readonly.toString():
      return Role.readonly
    default:
      throw new Error(`No matching role found for : ${role.toUpperCase()}`)
  }
}

export { getRole }
