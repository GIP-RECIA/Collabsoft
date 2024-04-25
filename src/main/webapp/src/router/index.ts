import { Navigation } from '@/types/enums/Navigation.ts';
import { redirect } from '@/utils/routerUtils.ts';
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
});

export default router;
