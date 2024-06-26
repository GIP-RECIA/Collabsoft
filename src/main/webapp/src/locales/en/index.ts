/**
 * Copyright (C) 2023 GIP-RECIA, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
