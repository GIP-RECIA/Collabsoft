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

import type { CollaborationBody, FileBody, HistoryBody, MetadataBody } from '@/types'
import { axiosInstance as axios } from '@/utils'

async function getFiles() {
  return await axios.get(
    '/api/file',
  )
}

async function getShared() {
  return await axios.get(
    '/api/file/shared',
  )
}

async function getStarred() {
  return await axios.get(
    '/api/file/starred',
  )
}

async function getPublic() {
  return await axios.get(
    '/api/file/public',
  )
}

async function saveFile(
  body: FileBody,
) {
  return await axios.post(
    '/api/file',
    body,
  )
}

async function getFile(
  fileId: number,
) {
  return await axios.get(
    `/api/file/${fileId}`,
  )
}

async function setFile(
  fileId: number,
  body: FileBody,
) {
  return await axios.put(
    `/api/file/${fileId}`,
    body,
  )
}

async function deleteFile(
  fileId: number,
) {
  return await axios.delete(
    `/api/file/${fileId}`,
  )
}

async function setMetadata(
  fileId: number,
  body: MetadataBody,
) {
  return await axios.put(
    `/api/file/${fileId}/metadata`,
    body,
  )
}

async function getShare(
  fileId: number,
) {
  return await axios.get(
    `/api/file/${fileId}`,
  )
}

async function saveShare(
  fileId: number,
  body: CollaborationBody,
) {
  return await axios.post(
    `/api/file/${fileId}/share`,
    body,
  )
}

async function setShare(
  fileId: number,
  userId: number,
  body: CollaborationBody,
) {
  return await axios.put(
    `/api/file/${fileId}/share/${userId}`,
    body,
  )
}

async function deleteShare(
  fileId: number,
  userId: number,
) {
  return await axios.delete(
    `/api/file/${fileId}/share/${userId}`,
  )
}

async function deleteSharing(
  fileId: number,
) {
  return await axios.delete(
    `/api/file/${fileId}/share`,
  )
}

async function getHistories(
  fileId: number,
) {
  return await axios.get(
    `/api/file/${fileId}/history`,
  )
}

async function createHistory(
  fileId: number,
  body: HistoryBody,
) {
  return await axios.post(
    `/api/file/${fileId}/history`,
    body,
  )
}

async function getHistory(
  fileId: number,
  historyId: number,
) {
  return await axios.get(
    `/api/file/${fileId}/history/${historyId}`,
  )
}

async function deleteHistory(
  fileId: number,
  historyId: number,
) {
  return await axios.delete(
    `/api/file/${fileId}/history/${historyId}`,
  )
}

async function revertHistory(
  fileId: number,
  historyId: number,
) {
  return await axios.post(
    `/api/file/${fileId}/history/${historyId}/revert`,
  )
}

async function deleteHistories(
  fileId: number,
) {
  return await axios.delete(
    `/api/file/${fileId}/history`,
  )
}

export {
  createHistory,
  deleteFile,
  deleteHistories,
  deleteHistory,
  deleteShare,
  deleteSharing,
  getFile,
  getFiles,
  getHistories,
  getHistory,
  getPublic,
  getShare,
  getShared,
  getStarred,
  revertHistory,
  saveFile,
  saveShare,
  setFile,
  setMetadata,
  setShare,
}
