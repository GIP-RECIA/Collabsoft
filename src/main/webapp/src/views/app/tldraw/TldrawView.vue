<!--
 Copyright (C) 2023 GIP-RECIA, Inc.

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
-->

<script setup lang="ts">
import { useAppStore, useConfigurationStore, useFileStore } from '@/stores/index.ts';
import { headObserver, styleObserver } from '@/utils/tldrawUtils.ts';
import '@gip-recia/tldraw-webcomponent';
import { storeToRefs } from 'pinia';
import { computed, onMounted, onUnmounted } from 'vue';
import { useTheme } from 'vuetify';

const { VITE_API_URI } = import.meta.env;

const appStore = useAppStore();
const { isAutoSave, canAutoSave } = storeToRefs(appStore);

const configurationStore = useConfigurationStore();
const { user } = storeToRefs(configurationStore);

const fileStore = useFileStore();
const { file } = storeToRefs(fileStore);

const theme = useTheme();

const apiUrl = computed<string | undefined>(() =>
  file.value ? `${VITE_API_URI}/api/file/${file.value.id}` : undefined,
);

onMounted(() => {
  styleObserver.observe(document.body, { attributes: true });
  headObserver.observe(document.head, { childList: true });
});

onUnmounted(() => {
  styleObserver.disconnect();
  headObserver.disconnect();
});
</script>

<template>
  <tldraw-editor
    v-if="file"
    mode="single"
    :persistance-api-url="apiUrl"
    :assets-api-url="`${apiUrl}/resource`"
    :token="user.token"
    :dark-mode="theme.global.name.value == 'dark'"
    :auto-save="canAutoSave && isAutoSave"
    :open="true"
  />
</template>
