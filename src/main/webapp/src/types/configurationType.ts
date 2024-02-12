export type Configuration = {
  front: {
    collaboration: {
      websocketApiUrl: string;
      userInfoApiUrl: string;
    };
    extendedUportalHeader: {
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
      templateApiPath: string;
    };
  };
};
