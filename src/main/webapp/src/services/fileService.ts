import { instance as axios } from '@/utils/axiosUtils';

const getFiles = async () => await axios.get('/api/file');

const getShared = async () => await axios.get('/api/file/shared');

const getStarred = async () => await axios.get('/api/file/starred');

const getPublic = async () => await axios.get('/api/file/public');

const saveFile = async (body) => await axios.post('/api/file', body);

const getFile = async (fileId: number) => await axios.get(`/api/file/${fileId}`);

const setFile = async (fileId: number, body) => await axios.put(`/api/file/${fileId}`, body);

const deleteFile = async (fileId: number) => await axios.delete(`/api/file/${fileId}`);

const setMetadata = async (fileId: number, body) => await axios.put(`/api/file/${fileId}/metadata`, body);

const getShare = async (fileId: number) => await axios.get(`/api/file/${fileId}`);

const saveShare = async (fileId: number, body) => await axios.post(`/api/file/${fileId}/share`, body);

const setShare = async (fileId: number, userId: number, body) =>
  await axios.put(`/api/file/${fileId}/share/${userId}`, body);

const deleteShare = async (fileId: number, userId: number) => await axios.delete(`/api/file/${fileId}/share/${userId}`);

const deleteSharing = async (fileId: number) => await axios.delete(`/api/file/${fileId}/share`);

const getHistories = async (fileId: number) => await axios.get(`/api/file/${fileId}/history`);

const createHistory = async (fileId: number, body) => await axios.post(`/api/file/${fileId}/history`, body);

const getHistory = async (fileId: number, historyId: number) =>
  await axios.get(`/api/file/${fileId}/history/${historyId}`);

const deleteHistory = async (fileId: number, historyId: number) =>
  await axios.delete(`/api/file/${fileId}/history/${historyId}`);

const revertHistory = async (fileId: number, historyId: number) =>
  await axios.post(`/api/file/${fileId}/history/${historyId}/revert`);

const deleteHistories = async (fileId: number) => await axios.delete(`/api/file/${fileId}/history`);

export {
  getFiles,
  getShared,
  getStarred,
  getPublic,
  saveFile,
  getFile,
  setFile,
  deleteFile,
  setMetadata,
  getShare,
  saveShare,
  setShare,
  deleteShare,
  deleteSharing,
  getHistories,
  createHistory,
  getHistory,
  deleteHistory,
  revertHistory,
  deleteHistories,
};
