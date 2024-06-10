const charOTP = (length: number = 6) => {
  let otp = '';
  const characters = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ';
  for (let i = 0; i < length; i++) {
    const index = Math.floor(Math.random() * characters.length);
    otp += characters[index];
  }

  return otp;
};

const slugify = (value: string): string => {
  return String(value)
    .normalize('NFKD') // split accented characters into their base characters and diacritical marks
    .replace(/[\u0300-\u036f]/g, '') // remove all the accents, which happen to be all in the \u03xx UNICODE block.
    .trim() // trim leading or trailing whitespace
    .toLowerCase() // convert to lowercase
    .replace(/[^a-z0-9 -]/g, '') // remove non-alphanumeric characters
    .replace(/\s+/g, '-') // replace spaces with hyphens
    .replace(/-+/g, '-'); // remove consecutive hyphens
};

const interpolate = (input: string, params: Object): string => {
  const names = Object.keys(params);
  const vals = Object.values(params);

  return new Function(...names, `return \`${input}\`;`)(...vals);
};

export { charOTP, slugify, interpolate };
