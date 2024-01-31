import { AppSlug } from '@/types/enums/AppSlug.ts';
import { Navigation } from '@/types/enums/Navigation.ts';
import { createRouter, createWebHistory } from 'vue-router';

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      redirect: () => {
        return { name: Navigation.projects };
      },
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
      redirect: () => {
        return { name: Navigation.projects };
      },
      component: () => import('@/views/AppView.vue'),
      children: [
        {
          path: `${AppSlug.tldraw}/:fileId(\\d+)`,
          name: AppSlug.tldraw,
          component: () => import('@/views/app/tldraw/TldrawView.vue'),
        },
        {
          path: `${AppSlug.tldraw}/:roomId([A-Z]{6})`,
          name: `collaborative-${AppSlug.tldraw}`,
          component: () => import('@/views/app/tldraw/CollaborativeTldrawView.vue'),
        },
        {
          path: `${AppSlug.wisemapping}/:fileId(\\d+)`,
          name: AppSlug.wisemapping,
          component: () => import('@/views/app/wisemapping/WisemappingView.vue'),
        },
        {
          path: `${AppSlug.wisemapping}/:roomId([A-Z]{6})`,
          name: `collaborative-${AppSlug.wisemapping}`,
          component: () => import('@/views/app/wisemapping/CollaborativeWisemappingView.vue'),
        },
      ],
    },
    {
      path: '/:pathName(.*)',
      redirect: () => {
        return { name: Navigation.projects };
      },
    },
  ],
});

export default router;
