import i18n from '@/plugins/i18n.ts';
import { saveNcFile } from '@/services/api/nextcloudService.ts';
import { useConfigurationStore } from '@/stores/configurationStore.ts';
import { errorHandler } from '@/utils/axiosUtils.ts';
import axios from 'axios';
import { storeToRefs } from 'pinia';
import { toast } from 'vue3-toastify';

const { VITE_NEXTCLOUD_URI, VITE_AXIOS_TIMEOUT } = import.meta.env;

const { t } = i18n.global;

const instance = axios.create({
  baseURL: `${VITE_NEXTCLOUD_URI}/remote.php/dav/files/`,
  timeout: VITE_AXIOS_TIMEOUT,
  headers: {
    Authorization: 'Bearer null',
  },
});

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
            window.open(`${VITE_NEXTCLOUD_URI}/`, '_blank');
          },
        },
      );
    }
  } catch (error: any) {
    if (error.response?.status === 401) {
      toast.error(t('toast.nextcloud.401'), {
        autoClose: false,
        onClick: () => {
          window.open(`${VITE_NEXTCLOUD_URI}/apps/user_cas/login`, '_blank');
        },
      });
    } else errorHandler(error, true);
  }
};

export { instance, saveOnNextcloud };
