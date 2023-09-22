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
          path: 'projects',
          name: 'projects',
          component: () => import('@/views/home/ProjectsView.vue'),
        },
        {
          path: 'favorites',
          name: 'favorites',
          component: () => import('@/views/home/FavoritesView.vue'),
        },
        {
          path: 'shared',
          name: 'shared',
          component: () => import('@/views/home/SharedView.vue'),
        },
        {
          path: 'public',
          name: 'public',
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
