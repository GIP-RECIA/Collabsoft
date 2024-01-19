import { nextcloud } from '@/constants.ts';
import i18n from '@/plugins/i18n.ts';
import { saveNcFile } from '@/services/nextcloudService.ts';
import { useConfigurationStore } from '@/stores/configurationStore.ts';
import { errorHandler } from '@/utils/axiosUtils.ts';
import axios from 'axios';
import { storeToRefs } from 'pinia';
import { useToast } from 'vue-toastification';

const { t } = i18n.global;
const toast = useToast();

const { VITE_AXIOS_TIMEOUT } = import.meta.env;

const instance = axios.create({
  baseURL: `${nextcloud.uri}/remote.php/dav/files/`,
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
            window.open(`${nextcloud.uri}/`, '_blank');
          },
        },
      );
    }
  } catch (error: any) {
    if (error.response?.status === 401) {
      toast.error(t('toast.nextcloud.401'), {
        timeout: false,
        onClick: () => {
          window.open(`${nextcloud.uri}/apps/user_cas/login`, '_blank');
        },
      });
    } else errorHandler(error, true);
  }
};

export { instance, saveOnNextcloud };
