import { Navigation } from '@/types/enums/Navigation';
import { createRouter, createWebHistory } from 'vue-router';

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
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
      path: '/app/:appSlug/:fileId',
      name: 'app',
      component: () => import('@/views/AppView.vue'),
    },
  ],
});

export default router;
