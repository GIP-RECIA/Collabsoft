import type { AssociatedApp } from '@/types/associatedAppType.ts';

export type Configuration = {
  front: {
    appName: string;
    userInfoApiUrl: string;
    nextcloudUri: string;
    collaboration: {
      websocketApiUrl: string;
    };
    extendedUportalHeader: {
      componentPath: string;
      contextApiUrl: string;
      signOutUrl: string;
      defaultOrgLogoPath: string;
      defaultAvatarPath: string;
      defaultOrgIconPath: string;
      favoriteApiUrl: string;
      layoutApiUrl: string;
      organizationApiUrl: string;
      portletApiUrl: string;
      userInfoApiUrl: string;
      userInfoPortletUrl: string;
      sessionApiUrl: string;
      templateApiPath: string;
      switchOrgPortletUrl: string;
      favoritesPortletCardSize: string;
      gridPortletCardSize: string;
      hideActionMode: string;
      showFavoritesInSlider: string;
      returnHomeTitle: string;
      returnHomeTarget: string;
      iconType: string;
    };
    extendedUportalFooter: {
      componentPath: string;
      templateApiPath: string;
    };
    apps: Array<AssociatedApp>;
  };
};
