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

const getFiles = async () => await axios.get('/api/file')

const getShared = async () => await axios.get('/api/file/shared')

const getStarred = async () => await axios.get('/api/file/starred')

const getPublic = async () => await axios.get('/api/file/public')

const saveFile = async (body: FileBody) => await axios.post('/api/file', body)

const getFile = async (fileId: number) => await axios.get(`/api/file/${fileId}`)

const setFile = async (fileId: number, body: FileBody) => await axios.put(`/api/file/${fileId}`, body)

const deleteFile = async (fileId: number) => await axios.delete(`/api/file/${fileId}`)

const setMetadata = async (fileId: number, body: MetadataBody) => await axios.put(`/api/file/${fileId}/metadata`, body)

const star = async (fileId: number) => await setMetadata(fileId, { starred: true })

const unstar = async (fileId: number) => await setMetadata(fileId, { starred: false })

const getShare = async (fileId: number) => await axios.get(`/api/file/${fileId}`)

async function saveShare(fileId: number, body: CollaborationBody) {
  return await axios.post(`/api/file/${fileId}/share`, body)
}

async function setShare(fileId: number, userId: number, body: CollaborationBody) {
  return await axios.put(`/api/file/${fileId}/share/${userId}`, body)
}

const deleteShare = async (fileId: number, userId: number) => await axios.delete(`/api/file/${fileId}/share/${userId}`)

const deleteSharing = async (fileId: number) => await axios.delete(`/api/file/${fileId}/share`)

const getHistories = async (fileId: number) => await axios.get(`/api/file/${fileId}/history`)

async function createHistory(fileId: number, body: HistoryBody) {
  return await axios.post(`/api/file/${fileId}/history`, body)
}

async function getHistory(fileId: number, historyId: number) {
  return await axios.get(`/api/file/${fileId}/history/${historyId}`)
}

async function deleteHistory(fileId: number, historyId: number) {
  return await axios.delete(`/api/file/${fileId}/history/${historyId}`)
}

async function revertHistory(fileId: number, historyId: number) {
  return await axios.post(`/api/file/${fileId}/history/${historyId}/revert`)
}

const deleteHistories = async (fileId: number) => await axios.delete(`/api/file/${fileId}/history`)

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
  star,
  unstar,
}
