import type { File } from '@/types/fileType.ts';

const isStarred = (file: File | undefined): boolean =>
  file && file.metadata.length > 0 ? file.metadata[0].starred : undefined;

const toggleStarred = (file: File): void => {
  file.metadata[0].starred = !isStarred(file);
};

const toggleStarredInArray = (files: Array<File>, fileId: number): void => {
  const file = files.find((file) => file.id === fileId);
  if (!file) return;
  toggleStarred(file);
};

export { isStarred, toggleStarred, toggleStarredInArray };
