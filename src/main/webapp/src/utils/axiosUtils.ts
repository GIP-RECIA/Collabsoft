import i18n from '@/plugins/i18n.ts';
import { useConfigurationStore } from '@/stores/configurationStore.ts';
import { getToken } from '@/utils/soffitUtils.ts';
import axios from 'axios';
import throttle from 'lodash.throttle';
import { storeToRefs } from 'pinia';
import { toast } from 'vue3-toastify';

const { t } = i18n.global;

const { VITE_API_URI, VITE_AXIOS_TIMEOUT } = import.meta.env;

const instance = axios.create({
  baseURL: VITE_API_URI,
  timeout: VITE_AXIOS_TIMEOUT,
});

let token: string | undefined = undefined;
let timeout: number | undefined = undefined;
let renewToken: any;

const init = async (): Promise<void> => {
  const configurationStore = useConfigurationStore();
  const { user } = storeToRefs(configurationStore);

  try {
    const {
      encoded,
      decoded: { exp, iat, sub },
    } = await getToken();
    token = `Bearer ${encoded}`;
    timeout = (exp - iat) * 1000 * 0.75;
    renewToken = throttle(
      async () => {
        try {
          const {
            encoded,
            decoded: { sub },
          } = await getToken();
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
  if (timeout == undefined) await init();
  else await renewToken();
  config.headers['Authorization'] = token;

  return config;
});

const errorHandler = (e: any, toastOrI18n?: boolean | string): void => {
  const showToast: boolean = typeof toastOrI18n == 'boolean' && toastOrI18n;

  if (axios.isAxiosError(e)) {
    if (typeof toastOrI18n == 'string') toast.error(t(`toast.${toastOrI18n}`));
    else if (showToast) {
      if ([401, 404, 500].includes(e.response ? e.response.status : 0))
        toast.error(t(`toast.error.${e.response!.status}`));
      else toast.error(t('toast.error.unmanaged'));
    }
    console.error(e.message);
  } else if (e instanceof Error) {
    if (showToast) toast.error(t('toast.error.stock') + e.message);
    console.error(e.message);
  } else {
    if (showToast) toast.error(t('toast.error.unknown'));
    console.error(e);
  }
};

export { instance, errorHandler };
