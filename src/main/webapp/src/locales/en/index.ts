import alerts from './alerts.json';
import applications from './applications.json';
import buttons from './buttons.json';
import dialogs from './dialogs.json';
import errors from './errors.json';
import information from './information.json';
import main from './main.json';
import menus from './menus.json';
import navigations from './navigations.json';
import settings from './settings.json';
import toasts from './toasts.json';
import { en } from 'vuetify/locale';

export default {
  ...alerts,
  ...applications,
  ...buttons,
  ...dialogs,
  ...errors,
  ...information,
  ...main,
  ...menus,
  ...navigations,
  ...settings,
  ...toasts,
  $vuetify: {
    ...en,
  },
};
