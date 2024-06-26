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

const styleObserver = new MutationObserver((mutationList: Array<MutationRecord>) => {
  for (const mutation of mutationList) {
    if (mutation.type === 'attributes' && mutation.attributeName === 'style') {
      document.body.removeAttribute('style');
    }
  }
});

const headObserver = new MutationObserver((mutationList: Array<MutationRecord>) => {
  for (const mutation of mutationList) {
    if (mutation.type == 'childList' && mutation.addedNodes.length == 1) {
      if (mutation.addedNodes[0].textContent?.includes('--removed-body-scroll-bar-size')) {
        document.head.removeChild(mutation.addedNodes[0]);
      }
    }
  }
});

export { styleObserver, headObserver };
