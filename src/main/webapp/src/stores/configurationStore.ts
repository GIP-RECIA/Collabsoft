/**
 * Copyright (C) 2023 GIP-RECIA, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import { getConfiguration } from '@/services/api/configurationService.ts';
import type { AssociatedApp } from '@/types/associatedAppType.ts';
import type { Configuration } from '@/types/configurationType.ts';
import type { Soffit } from '@/types/soffitType.ts';
import { errorHandler, initToken } from '@/utils/axiosUtils.ts';
import { useEntTheme } from '@/utils/entUtils.ts';
import { setNextcloudUri } from '@/utils/nextcloudUtils.ts';
import { initAppsRoutes } from '@/utils/routerUtils.ts';
import { defineStore } from 'pinia';
import { computed, ref, watch } from 'vue';

export const useConfigurationStore = defineStore('configuration', () => {
  const configuration = ref<Configuration | undefined>();

  /**
   * Initialise `configuration`
   */
  const init = async (force: boolean = false): Promise<boolean> => {
    if (force || !isInit.value) {
      try {
        const response = await getConfiguration();
        configuration.value = response.data;
        if (!configuration.value) return false;
        if (isNextcloudAvailable.value) setNextcloudUri(configuration.value.front.nextcloudUri);
        await initToken(configuration.value.front.userInfoApiUrl);
        await useEntTheme(configuration.value.front.extendedUportalHeader.templateApiPath);
        await initAppsRoutes(configuration.value.front.apps);

        return true;
      } catch (e) {
        errorHandler(e);
      }
    }
    return false;
  };

  const isInit = computed<boolean>(() => configuration.value != undefined);

  const isReady = computed<boolean>(() => isInit.value && isSoffitOk.value);

  /* -- App name -- */

  const infoName = ref<string | undefined>();

  const appName = computed<string>(() =>
    isInit.value && configuration.value!.front.appName.trim() != '' ? configuration.value!.front.appName : __APP_NAME__,
  );

  watch(
    [infoName, appName],
    () => {
      document.title = infoName.value ? `${infoName.value} - ${appName.value}` : appName.value;
    },
    { immediate: true },
  );

  /* -- Nextcloud -- */

  const isNextcloudAvailable = computed<boolean>(() => (configuration.value?.front.nextcloudUri ?? '').length > 0);

  /* -- User -- */

  const user = ref<Soffit>({ sub: 'guest', token: undefined });

  const isSoffitOk = computed<boolean>(() => !user.value.sub.startsWith('guest'));

  /* -- Navigation -- */

  const lastNavigation = ref<string | undefined>();

  /**
   * Dialog settings state
   */
  const isSettings = ref<boolean>(false);

  /* -- Apps -- */

  const availableApps = computed<Array<AssociatedApp>>(() =>
    configuration.value!.front.apps.filter((app) => app.enabled),
  );

  return {
    configuration,
    init,
    isInit,
    isReady,
    infoName,
    appName,
    isNextcloudAvailable,
    user,
    isSoffitOk,
    lastNavigation,
    isSettings,
    availableApps,
  };
});
