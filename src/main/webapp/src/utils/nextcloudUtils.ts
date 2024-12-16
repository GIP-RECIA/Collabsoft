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

import i18n from '@/plugins/i18n.ts'
import { saveNcFile } from '@/services/api'
import { useConfigurationStore } from '@/stores'
import { errorHandler, interpolate } from '@/utils'
import axios from 'axios'
import { storeToRefs } from 'pinia'
import { toast } from 'vue3-toastify'

const { VITE_AXIOS_TIMEOUT } = import.meta.env

const { t } = i18n.global

let ncBaseUri: string | undefined

const instance = axios.create({
  timeout: VITE_AXIOS_TIMEOUT,
  headers: {
    Authorization: 'Bearer null',
  },
})

function setNcUri(uri: string): void {
  if (uri.length <= 0)
    return
  ncBaseUri = interpolate(uri, { domain: window.location.hostname })
  instance.defaults.baseURL = `${ncBaseUri}/remote.php/dav/files/`
}

async function saveOnNc(file: File, type: string): Promise<void> {
  const configurationStore = useConfigurationStore()
  const { user } = storeToRefs(configurationStore)

  try {
    const response = await saveNcFile(user.value.sub, file, type)
    if ([201, 204].includes(response.status)) {
      toast.success(
        t('toast.nc.200', {
          fileName: `${file.name}.${type}`,
          status: t(`toast.nc.${response.status}`),
        }),
        {
          onClick: () => {
            window.open(`${ncBaseUri}/`, '_blank')
          },
        },
      )
    }
  }
  catch (error: any) {
    if (error.response?.status === 401) {
      toast.error(t('toast.nc.401'), {
        autoClose: false,
        onClick: () => {
          window.open(`${ncBaseUri}/apps/user_cas/login`, '_blank')
        },
      })
    }
    else {
      errorHandler(error, true)
    }
  }
}

export { instance as ncInstance, saveOnNc, setNcUri }
