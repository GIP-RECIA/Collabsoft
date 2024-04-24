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

const register = (app: App): void => {
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
  app.component('font-awesome-icon', FontAwesomeIcon);
};

export { register };
