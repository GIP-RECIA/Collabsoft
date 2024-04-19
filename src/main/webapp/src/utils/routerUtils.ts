import type { AssociatedApp } from '@/types/associatedAppType.ts';
import { capitalize } from 'vue';
import { type RouteRecordRaw, type Router } from 'vue-router';

const initAppsRoutes = async (apps: Array<AssociatedApp>): Promise<void> => {
  const router: Router = (await import('@/router/index.ts')).default;

  apps.forEach((app) => {
    const solo: RouteRecordRaw = {
      path: `${app.slug}/:fileId(\\d+)`,
      name: app.slug,
      component: () => import(`@/views/app/${app.slug}/${capitalize(app.slug)}View.vue`),
    };
    router.addRoute('app', solo);

    const multi: RouteRecordRaw = {
      path: `${app.slug}/:roomId([A-Z]{6})`,
      name: `collaborative-${app.slug}`,
      component: () => import(`@/views/app/${app.slug}/Collaborative${capitalize(app.slug)}View.vue`),
    };
    router.addRoute('app', multi);
  });
};

export { initAppsRoutes };
