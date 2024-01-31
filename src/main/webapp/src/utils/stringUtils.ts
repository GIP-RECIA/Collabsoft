const capitalize = (value: string): string => {
  return value.charAt(0).toUpperCase() + value.slice(1).toLowerCase();
};

const charOTP = (length: number = 6) => {
  let otp = '';
  const characters = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ';
  for (let i = 0; i < length; i++) {
    const index = Math.floor(Math.random() * characters.length);
    otp += characters[index];
  }

  return otp;
};

export { capitalize, charOTP };
