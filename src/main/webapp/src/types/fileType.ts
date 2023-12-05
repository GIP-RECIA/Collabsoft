import type { AssociatedApp } from '@/types/associatedAppType.ts';
import type { User } from '@/types/userType.ts';

export type File = {
  id: number;
  title: string;
  description: string | null;
  blob: any;
  creator: User;
  creationDate: string;
  lastEditor: User;
  editionDate: string;
  associatedApp: AssociatedApp;
  pub: boolean;
};
