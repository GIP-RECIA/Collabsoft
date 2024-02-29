import { library } from '@fortawesome/fontawesome-svg-core';
import { faSquare as farSquare, faStar as farStar } from '@fortawesome/free-regular-svg-icons';
import {
  faArrowLeft,
  faArrowRightToBracket,
  faArrowsRotate,
  faCaretDown,
  faChalkboardUser,
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
    faArrowLeft,
    faArrowRightToBracket,
    faArrowsRotate,
    faCaretDown,
    faChalkboardUser,
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
