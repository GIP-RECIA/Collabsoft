import oidc, { type JWT } from '@uportal/open-id-connect';

const { VITE_USER_INFO_API_URI } = import.meta.env;

const getToken = async (): Promise<{ encoded: string; decoded: JWT }> => {
  const { encoded, decoded } = await oidc({ userInfoApiUrl: VITE_USER_INFO_API_URI });

  return { encoded, decoded };
};

export { getToken };
