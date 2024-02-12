import oidc, { type JWT } from '@uportal/open-id-connect';

const getToken = async (userInfoApiUrl: string): Promise<{ encoded: string; decoded: JWT }> => {
  const { encoded, decoded } = await oidc({ userInfoApiUrl });

  return { encoded, decoded };
};

export { getToken };
