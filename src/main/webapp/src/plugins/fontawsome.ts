import { library } from '@fortawesome/fontawesome-svg-core';
import { faSquare as farSquare, faStar as farStar } from '@fortawesome/free-regular-svg-icons';
import {
  fa1,
  fa2,
  faArrowLeft,
  faCaretDown,
  faCheckSquare,
  faCircleInfo,
  faClockRotateLeft,
  faCloud,
  faDownload,
  faEllipsisVertical,
  faFolder,
  faGear,
  faGlobe,
  faMagnifyingGlass,
  faPen,
  faPlus,
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

const register = (app: App): void => {
  library.add(farSquare, farStar);
  library.add(
    fa1,
    fa2,
    faArrowLeft,
    faCaretDown,
    faCheckSquare,
    faCircleInfo,
    faClockRotateLeft,
    faCloud,
    faDownload,
    faEllipsisVertical,
    faFolder,
    faGear,
    faGlobe,
    faMagnifyingGlass,
    faPen,
    faPlus,
    faSave,
    faShareNodes,
    faSortDown,
    faSortUp,
    faStar,
    faTimesCircle,
    faTrash,
    faXmark,
  );
  app.component('font-awesome-icon', FontAwesomeIcon);
};

export { register };
