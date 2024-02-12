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
  const toastOptions: ToastContainerOptions = { clearOnUrlChange: false };

  if (axios.isAxiosError(e)) {
    if (typeof toastOrI18n == 'string') toast.error(t(`toast.${toastOrI18n}`), toastOptions);
    else if (showToast) {
      if ([401, 404, 500].includes(e.response ? e.response.status : 0))
        toast.error(t(`toast.error.${e.response!.status}`), toastOptions);
      else toast.error(t('toast.error.unmanaged'), toastOptions);
    }
    console.error(e.message);
  } else if (e instanceof Error) {
    if (showToast) toast.error(t('toast.error.stock') + e.message, toastOptions);
    console.error(e.message);
  } else {
    if (showToast) toast.error(t('toast.error.unknown'), toastOptions);
    console.error(e);
  }
};

export { instance, initToken, errorHandler };
