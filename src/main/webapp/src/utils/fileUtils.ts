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
import type { File as cFile } from '@/types'

function downloadFileOrBlob(fileOrBlob: File | Blob, filename: string): void {
  const link = document.createElement('a')

  link.href = URL.createObjectURL(fileOrBlob)
  link.download = filename

  document.body.appendChild(link)

  link.dispatchEvent(
    new MouseEvent('click', {
      bubbles: true,
      cancelable: true,
      view: window,
    }),
  )

  document.body.removeChild(link)
}

function toFile(file: cFile): File {
  return new File([file.data], file.title, {
    type: `application/${file.associatedApp.type};charset=utf-8`,
  })
}

export { downloadFileOrBlob, toFile }
