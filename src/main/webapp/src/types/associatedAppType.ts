export type AssociatedApp = {
  id: number;
  enabled: boolean;
  slug: string;
  primaryColor: string | null;
  iconPath: string | null;
  extension: string;
  type: string;
};
