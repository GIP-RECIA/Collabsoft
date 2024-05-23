import type { AssociatedApp } from '@/types/associatedAppType.ts';
import type { Collaboration } from '@/types/collaborationType.ts';
import type { History } from '@/types/historyType.ts';
import type { User } from '@/types/userType.ts';

export type File = {
  id: number;
  uuid: string;
  title: string;
  description: string | null;
  data: any;
  creator: User;
  creationDate: string;
  lastEditor: User;
  editionDate: string;
  associatedApp: AssociatedApp;
  pub: boolean;
  collaborations?: Array<Collaboration>;
  histories?: Array<History>;
};
