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
import { library } from '@fortawesome/fontawesome-svg-core';
import { faSquare as farSquare, faStar as farStar } from '@fortawesome/free-regular-svg-icons';
import {
  faArrowLeft,
  faArrowRightToBracket,
  faArrowRotateRight,
  faArrowsRotate,
  faCaretDown,
  faChalkboardUser,
  faCheckSquare,
  faCircleInfo,
  faClockRotateLeft,
  faCloud,
  faDownload,
  faEllipsisVertical,
  faFile,
  faFolder,
  faGear,
  faGlobe,
  faMagnifyingGlass,
  faPen,
  faPlus,
  faRightToBracket,
  faSave,
  faShareNodes,
  faSortDown,
  faSortUp,
  faStar,
  faTimesCircle,
  faTrash,
  faXmark,
} from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import type { App } from 'vue';

const register = (app: App | undefined): void => {
  library.add(farSquare, farStar);
  library.add(
    faArrowLeft,
    faArrowRightToBracket,
    faArrowRotateRight,
    faArrowsRotate,
    faCaretDown,
    faChalkboardUser,
    faCheckSquare,
    faCircleInfo,
    faClockRotateLeft,
    faCloud,
    faDownload,
    faEllipsisVertical,
    faFile,
    faFolder,
    faGear,
    faGlobe,
    faMagnifyingGlass,
    faPen,
    faPlus,
    faRightToBracket,
    faSave,
    faShareNodes,
    faSortDown,
    faSortUp,
    faStar,
    faTimesCircle,
    faTrash,
    faXmark,
  );
  app?.component('font-awesome-icon', FontAwesomeIcon);
};

export { register };
