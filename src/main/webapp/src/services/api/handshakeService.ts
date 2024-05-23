import { instance as axios } from '@/utils/axiosUtils.ts';

const handshake = async () => await axios.get('/api/handshake');

export { handshake };
