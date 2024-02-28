import i18n from '@/plugins/i18n.ts';
import { useConfigurationStore } from '@/stores/configurationStore.ts';
import { getToken } from '@/utils/soffitUtils.ts';
import axios from 'axios';
import throttle from 'lodash.throttle';
import { storeToRefs } from 'pinia';
import { type ToastContainerOptions, toast } from 'vue3-toastify';

const { VITE_API_URI, VITE_AXIOS_TIMEOUT } = import.meta.env;

const { t } = i18n.global;

const instance = axios.create({
  baseURL: VITE_API_URI,
  timeout: VITE_AXIOS_TIMEOUT,
});

let token: string | undefined = undefined;
let timeout: number | undefined = undefined;
let renewToken: any;

const initToken = async (userInfoApiUrl: string): Promise<void> => {
  const configurationStore = useConfigurationStore();
  const { user } = storeToRefs(configurationStore);

  try {
    const {
      encoded,
      decoded: { exp, iat, sub },
    } = await getToken(userInfoApiUrl);
    token = `Bearer ${encoded}`;
    timeout = (exp - iat) * 1000 * 0.75;
    renewToken = throttle(
      async () => {
        try {
          const {
            encoded,
            decoded: { sub },
          } = await getToken(userInfoApiUrl);
          token = `Bearer ${encoded}`;
          user.value = { ...user.value, sub };
        } catch (e) {
          // nothing to do
        }
      },
      timeout,
      { trailing: false },
    );
    user.value = { ...user.value, sub };
  } catch (e) {
    // nothing to do
  }
};

instance.interceptors.request.use(async (config) => {
  if (config.url == '/api/config') return config;

  await renewToken();
  config.headers['Authorization'] = token;

  return config;
});

const errorHandler = (e: any, toastOrI18n?: boolean | string): void => {
  const showToast: boolean = typeof toastOrI18n == 'boolean' && toastOrI18n;
  const i18nHandled: Array<number> = [401, 404, 500];
  let message: string, error: any;

  if (axios.isAxiosError(e)) {
    if (typeof toastOrI18n == 'string') message = `toast.${toastOrI18n}`;
    else {
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
