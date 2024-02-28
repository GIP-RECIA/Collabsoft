import type { File as cFile } from '@/types/fileType';

const downloadFileOrBlob = (fileOrBlob: File | Blob, filename: string): void => {
  const link = document.createElement('a');

  link.href = URL.createObjectURL(fileOrBlob);
  link.download = filename;

  document.body.appendChild(link);

  link.dispatchEvent(
    new MouseEvent('click', {
      bubbles: true,
      cancelable: true,
      view: window,
    }),
  );

  document.body.removeChild(link);
};

const toFile = (file: cFile): File => {
  return new File([file.blob], file.title, {
    type: `application/${file.associatedApp.type};charset=utf-8`,
  });
};

export { downloadFileOrBlob, toFile };
