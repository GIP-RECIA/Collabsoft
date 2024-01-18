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

export { downloadFileOrBlob };
