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
import { useConfigurationStore, useFileStore } from '@/stores';
import '@gip-recia/wisemapping-webcomponent';
import { storeToRefs } from 'pinia';
import { computed } from 'vue';

const isDev = import.meta.env.DEV;

const { VITE_API_URI } = import.meta.env;

const configurationStore = useConfigurationStore();
const { user } = storeToRefs(configurationStore);

const fileStore = useFileStore();
const { file } = storeToRefs(fileStore);

const apiUrl = computed<string | undefined>(() => (file.value ? `${VITE_API_URI}/api/file` : undefined));
</script>

<template>
  <wisemapping-editor
    v-if="isDev && file"
    :persistance-api-url="apiUrl"
    :file-id="file.id"
    :token="user.token"
    mode="edition-owner"
  />
</template>

<style lang="scss">
wisemapping-editor {
  display: block;
  position: relative;
  width: 100%;
  height: 100%;
  z-index: 0;
}
</style>
