import i18n from '@/plugins/i18n.ts';
import { saveNcFile } from '@/services/api/nextcloudService.ts';
import { useConfigurationStore } from '@/stores/configurationStore.ts';
import { errorHandler } from '@/utils/axiosUtils.ts';
import { interpolate } from '@/utils/stringUtils.ts';
import axios from 'axios';
import { storeToRefs } from 'pinia';
import { toast } from 'vue3-toastify';

const { VITE_AXIOS_TIMEOUT } = import.meta.env;

const { t } = i18n.global;

let nextcloudBaseUri: string | undefined;

const instance = axios.create({
  timeout: VITE_AXIOS_TIMEOUT,
  headers: {
    Authorization: 'Bearer null',
  },
});

const setNextcloudUri = (uri: string): void => {
  if (uri.length <= 0) return;
  nextcloudBaseUri = interpolate(uri, { domain: window.location.hostname });
  instance.defaults.baseURL = `${nextcloudBaseUri}/remote.php/dav/files/`;
};

const saveOnNextcloud = async (file: File, type: string): Promise<void> => {
  const configurationStore = useConfigurationStore();
  const { user } = storeToRefs(configurationStore);

  try {
    const response = await saveNcFile(user.value.sub, file, type);
    if ([201, 204].includes(response.status)) {
      toast.success(
        t('toast.nextcloud.200', {
          fileName: `${file.name}.${type}`,
          status: t(`toast.nextcloud.${response.status}`),
        }),
        {
          onClick: () => {
            window.open(`${nextcloudBaseUri}/`, '_blank');
          },
        },
      );
    }
  } catch (error: any) {
    if (error.response?.status === 401) {
      toast.error(t('toast.nextcloud.401'), {
        autoClose: false,
        onClick: () => {
          window.open(`${nextcloudBaseUri}/apps/user_cas/login`, '_blank');
        },
      });
    } else errorHandler(error, true);
  }
};

export { instance, setNextcloudUri, saveOnNextcloud };
