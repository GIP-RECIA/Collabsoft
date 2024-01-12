import { library } from '@fortawesome/fontawesome-svg-core';
import { faStar as farStar } from '@fortawesome/free-regular-svg-icons';
import {
  fa1,
  fa2,
  faArrowLeft,
  faCircleInfo,
  faClockRotateLeft,
  faCloud,
  faDownload,
  faEllipsisVertical,
  faFolder,
  faGear,
  faGlobe,
  faPlus,
  faShareNodes,
  faSortDown,
  faSortUp,
  faStar,
  faTrash,
  faXmark,
} from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import type { App } from 'vue';

const register = (app: App): void => {
  library.add(farStar);
  library.add(
    fa1,
    fa2,
    faCircleInfo,
    faClockRotateLeft,
    faCloud,
    faDownload,
    faEllipsisVertical,
    faFolder,
    faGear,
    faGlobe,
    faPlus,
    faShareNodes,
    faSortDown,
    faSortUp,
    faStar,
    faTrash,
    faXmark,
    faArrowLeft,
  );
  app.component('font-awesome-icon', FontAwesomeIcon);
};

export { register };
