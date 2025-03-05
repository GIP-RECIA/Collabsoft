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

import type { ToastContainerOptions } from 'vue3-toastify'
import i18n from '@/plugins/i18n.ts'
import { handshake } from '@/services/api'
import { useConfigurationStore } from '@/stores'
import oidc from '@uportal/open-id-connect'
import axios from 'axios'
import { storeToRefs } from 'pinia'
import { toast } from 'vue3-toastify'

const isDev = import.meta.env.DEV

const { VITE_API_URI, VITE_AXIOS_TIMEOUT } = import.meta.env

const { t } = i18n.global

const instance = axios.create({
  baseURL: VITE_API_URI,
  timeout: VITE_AXIOS_TIMEOUT,
})

let token: string | undefined

async function initToken(userInfoApiUrl: string): Promise<void> {
  const configurationStore = useConfigurationStore()
  const { user } = storeToRefs(configurationStore)

  try {
    const {
      encoded,
      decoded: { exp, iat, sub },
    } = await oidc({ userInfoApiUrl })
    token = `Bearer ${encoded}`
    const timeout = (exp - iat) * 1000 * 0.75
    setInterval(async () => {
      try {
        const {
          encoded,
          decoded: { sub },
        } = await oidc({ userInfoApiUrl })
        token = `Bearer ${encoded}`
        user.value = { sub, token }
        if (isDev)
          // eslint-disable-next-line no-console
          console.debug('Token has been renewed')
      }
      catch (e) {
        console.error('Unable to renew token', e)
      }
    }, timeout)
    user.value = { sub, token }
    await handshake()
  }
  catch (e) {
    throw new Error(`Unable to init token : ${e}`)
  }
}

instance.interceptors.request.use(async (config) => {
  if (config.url === '/api/config')
    return config
  config.headers.Authorization = token

  return config
})

function errorHandler(e: any, toastOrI18n?: boolean | string): void {
  let showToast: boolean = typeof toastOrI18n == 'boolean' && toastOrI18n
  const i18nHandled: Array<number> = [401, 404, 500]
  let message: string, error: any

  if (axios.isAxiosError(e)) {
    if (typeof toastOrI18n == 'string' && toastOrI18n.trim().length > 0) {
      message = `toast.${toastOrI18n}`
      showToast = true
    }
    else {
      message = i18nHandled.includes(e.response?.status ?? -1)
        ? `toast.error.${e.response!.status}`
        : 'toast.error.unmanaged'
    }
    error = e.message
  }
  else if (e instanceof Error) {
    message = 'toast.error.stock'
    error = e.message
  }
  else {
    message = 'toast.error.unknown'
    error = e
  }

  if (showToast)
    toast.error(t(message), { clearOnUrlChange: false } as ToastContainerOptions)
  console.error(error)
}

export { instance as axiosInstance, errorHandler, initToken }
