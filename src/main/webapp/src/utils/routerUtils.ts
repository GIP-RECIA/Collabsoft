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

import type { Router, RouteRecordRaw, RouteRecordRedirectOption } from 'vue-router'
import type { AssociatedApp } from '@/types'
import { capitalize } from 'vue'
import { Navigation } from '@/types/enums'

const isDev = import.meta.env.DEV

const redirect: RouteRecordRedirectOption = () => {
  return { name: Navigation.projects }
}

async function initAppsRoutes(apps: Array<AssociatedApp>): Promise<void> {
  const router: Router = (await import('@/router')).default

  apps
    .filter(app => app.enabled || isDev)
    .forEach((app) => {
      const solo: RouteRecordRaw = {
        path: `${app.slug}/:fileId(\\d+)`,
        name: app.slug,
        component: () => import(`@/views/app/${app.slug}/${capitalize(app.slug)}View.vue`),
      }
      router.addRoute('app', solo)

      const multi: RouteRecordRaw = {
        path: `${app.slug}/:roomId([A-Z]{6})`,
        name: `collaborative-${app.slug}`,
        component: () => import(`@/views/app/${app.slug}/Collaborative${capitalize(app.slug)}View.vue`),
      }
      router.addRoute('app', multi)
    })

  router.addRoute({ path: '/:pathName(.*)', redirect })

  router.replace(router.currentRoute.value)
}

function preventExit(e: Event): void {
  e.preventDefault()
}

export { initAppsRoutes, preventExit, redirect }
