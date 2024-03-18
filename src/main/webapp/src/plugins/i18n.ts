import en from '@/locales/en/index.ts';
import fr from '@/locales/fr/index.ts';
import { createI18n } from 'vue-i18n';

const fallbackLocale: string = 'fr';
const availableLanguages: Array<string> = ['en', 'fr'];

const findLanguage = (): string => {
  if (availableLanguages.includes(window.navigator.language)) return window.navigator.language;

  const matchLanguages = window.navigator.languages.filter((lang) =>
    availableLanguages.find((available) => lang == available),
  );
  if (matchLanguages.length > 0) return matchLanguages[0];

  return fallbackLocale;
};

export default createI18n({
  legacy: false,
  allowComposition: true,
  locale: findLanguage(),
  fallbackLocale,
  messages: {
    en,
    fr,
  },
});
