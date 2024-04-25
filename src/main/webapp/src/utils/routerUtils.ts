import type { AssociatedApp } from '@/types/associatedAppType.ts';
import { Navigation } from '@/types/enums/Navigation.ts';
import { capitalize } from 'vue';
import { type RouteRecordRaw, type RouteRecordRedirectOption, type Router } from 'vue-router';

const redirect: RouteRecordRedirectOption = () => {
  return { name: Navigation.projects };
};

const initAppsRoutes = async (apps: Array<AssociatedApp>): Promise<void> => {
  const router: Router = (await import('@/router/index.ts')).default;

  apps
    .filter((app) => app.enabled)
    .forEach((app) => {
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

  router.addRoute({ path: '/:pathName(.*)', redirect });

  router.replace(router.currentRoute.value);
};

export { redirect, initAppsRoutes };
