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
import { Navigation } from '@/types/enums';
import { redirect } from '@/utils';
import { createRouter, createWebHistory } from 'vue-router';

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      redirect,
      component: () => import('@/views/HomeView.vue'),
      children: [
        {
          path: Navigation.projects,
          name: Navigation.projects,
          component: () => import('@/views/home/ProjectsView.vue'),
        },
        {
          path: Navigation.favorites,
          name: Navigation.favorites,
          component: () => import('@/views/home/FavoritesView.vue'),
        },
        {
          path: Navigation.shared,
          name: Navigation.shared,
          component: () => import('@/views/home/SharedView.vue'),
        },
        {
          path: Navigation.public,
          name: Navigation.public,
          component: () => import('@/views/home/PublicView.vue'),
        },
      ],
    },
    {
      path: '/app',
      name: 'app',
      redirect,
      component: () => import('@/views/AppView.vue'),
      children: [],
    },
  ],
  strict: true,
  sensitive: true,
});

export default router;
