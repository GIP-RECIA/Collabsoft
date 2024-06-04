import i18n from '@/plugins/i18n.ts';
import { handshake } from '@/services/api/handshakeService.ts';
import { useConfigurationStore } from '@/stores/configurationStore.ts';
import { getToken } from '@/utils/soffitUtils.ts';
import axios from 'axios';
import { storeToRefs } from 'pinia';
import { type ToastContainerOptions, toast } from 'vue3-toastify';

const isDev = import.meta.env.DEV;

const { VITE_API_URI, VITE_AXIOS_TIMEOUT } = import.meta.env;

const { t } = i18n.global;

const instance = axios.create({
  baseURL: VITE_API_URI,
  timeout: VITE_AXIOS_TIMEOUT,
});

let token: string | undefined = undefined;

const initToken = async (userInfoApiUrl: string): Promise<void> => {
  const configurationStore = useConfigurationStore();
  const { user } = storeToRefs(configurationStore);

  try {
    const {
      encoded,
      decoded: { exp, iat, sub },
    } = await getToken(userInfoApiUrl);
    token = `Bearer ${encoded}`;
    const timeout = (exp - iat) * 1000 * 0.75;
    setInterval(async () => {
      try {
        const {
          encoded,
          decoded: { sub },
        } = await getToken(userInfoApiUrl);
        token = `Bearer ${encoded}`;
        user.value = { sub, token };
        if (isDev) console.debug('Token has been renewed');
      } catch (e) {
        console.error('Unable to renew token', e);
      }
    }, timeout);
    user.value = { sub, token };
    await handshake();
  } catch (e) {
    throw new Error(`Unable to init token : ${e}`);
  }
};

instance.interceptors.request.use(async (config) => {
  if (config.url == '/api/config') return config;
  config.headers['Authorization'] = token;

  return config;
});

const errorHandler = (e: any, toastOrI18n?: boolean | string): void => {
  let showToast: boolean = typeof toastOrI18n == 'boolean' && toastOrI18n;
  const i18nHandled: Array<number> = [401, 404, 500];
  let message: string, error: any;

  if (axios.isAxiosError(e)) {
    if (typeof toastOrI18n == 'string' && toastOrI18n.trim().length > 0) {
      message = `toast.${toastOrI18n}`;
      showToast = true;
    } else {
      message = i18nHandled.includes(e.response?.status ?? -1)
        ? `toast.error.${e.response!.status}`
        : 'toast.error.unmanaged';
    }
    error = e.message;
  } else if (e instanceof Error) {
    message = 'toast.error.stock';
    error = e.message;
  } else {
    message = 'toast.error.unknown';
    error = e;
  }

  if (showToast) toast.error(t(message), { clearOnUrlChange: false } as ToastContainerOptions);
  console.error(error);
};

export { instance, initToken, errorHandler };
