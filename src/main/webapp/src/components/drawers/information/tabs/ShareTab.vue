<script setup lang="ts">
import { useConfigurationStore } from '@/stores/configurationStore.ts';
import { useFileStore } from '@/stores/fileStore.ts';
import type { Collaboration } from '@/types/collaborationType.ts';
import { Role, getRole } from '@/types/enums/Role.ts';
import { storeToRefs } from 'pinia';
import { ref, watch } from 'vue';
import { useI18n } from 'vue-i18n';

const isDev = import.meta.env.DEV;

const configurationStore = useConfigurationStore();
const { isShareInRoom } = storeToRefs(configurationStore);

const fileStore = useFileStore();
const { refreshCurrentFile } = fileStore;
const { currentFile } = storeToRefs(fileStore);

const { t } = useI18n();

const roles: Array<Role> = [Role.editor, Role.readonly];

const newUser = ref<any>();
const newRole = ref<Role>();

const updateRole = (collaboration: Collaboration, newValue: Role): void => {
  if (newValue != getRole(collaboration.role)) refreshCurrentFile();
};

const addUser = (): void => {
  newUser.value = undefined;
  newRole.value = undefined;
};

const visibility = ref<boolean>(false);

watch(
  () => currentFile.value?.pub,
  (newValue) => {
    if (newValue == undefined) return;
    visibility.value = newValue;
  },
  { immediate: true },
);

watch(visibility, (newValue) => {
  refreshCurrentFile();
});
</script>

<template>
  <div v-if="isDev" class="d-flex align-center mb-2 bg-grey-lighten-3 rounded-xl">
    <v-text-field
      v-model="newUser"
      variant="solo"
      bg-color="rgba(255, 255, 255, 0)"
      rounded="xl"
      class="share-item--name"
      flat
      hide-details
    />
    <v-select
      v-model="newRole"
      :items="roles"
      variant="solo"
      rounded="xl"
      bg-color="rgba(255, 255, 255, 0)"
      class="share-item--role"
      flat
      hide-details
      hide-no-data
    >
      <v-list rounded="xl" class="pa-2"> </v-list>
      <template #item="{ props }">
        <v-list-item v-bind="props" rounded="xl" />
      </template>
    </v-select>
    <v-btn
      icon="fas fa-plus"
      color="success"
      variant="text"
      size="small"
      :alt="t('button.add')"
      class="me-2"
      @click="addUser"
    />
  </div>

  <v-list v-if="currentFile?.collaborations" class="pt-0">
    <v-list-item
      v-for="(collaboration, index) in currentFile.collaborations"
      :key="index"
      rounded="xl"
      :class="[
        index < currentFile.collaborations.length - 1 ? 'mb-2' : '',
        'pr-2',
        'bg-grey-lighten-3',
        'list-item--custom',
      ]"
    >
      <template #default>
        <div class="d-flex">
          <div class="d-flex align-center share-item--name">
            {{ collaboration.user.displayName }}
          </div>
          <v-select
            :model-value="getRole(collaboration.role)"
            :items="roles"
            variant="solo"
            density="compact"
            rounded="xl"
            bg-color="rgba(255,255,255, 0)"
            flat
            hide-details
            hide-no-data
            class="share-item--role"
            @update:model-value="(newValue) => updateRole(collaboration, newValue)"
          >
            <v-list rounded="xl" class="pa-2"> </v-list>
            <template #item="{ props }">
              <v-list-item v-bind="props" rounded="xl" />
            </template>
          </v-select>
        </div>
      </template>
      <template #append>
        <v-btn icon="fas fa-trash" color="error" variant="text" size="small" :alt="t('button.delete')" />
      </template>
    </v-list-item>
  </v-list>

  <div :class="[isDev ? 'my-2' : '']">
    <div class="ml-2 mb-1">{{ t('shareOptions') }}</div>
    <v-btn
      prepend-icon="fas fa-chalkboard-user"
      :text="t('button.shareInRoom')"
      variant="tonal"
      block
      @click="isShareInRoom = true"
    />
  </div>

  <div v-if="isDev">
    <div class="ml-2 my-1">{{ t('information.visibility') }}</div>
    <v-switch
      v-model="visibility"
      :label="t(`visibility.${visibility ? 'public' : 'private'}`)"
      density="compact"
      inset
      hide-details
    />
  </div>
</template>