import { instance as axios } from '@/utils/nextcloudUtils.ts';

const getNcFile = async (userID: string, fileUri: string) => await axios.get(`${userID}/${fileUri}`);

const saveNcFile = async (userID: string, file: File, type: string) =>
  await axios.put(`${userID}/${file.name}.${type}`, file);

export { getNcFile, saveNcFile };
