<script setup lang="ts">
import { setFile } from '@/services/fileService.ts';
import { useConfigurationStore } from '@/stores/configurationStore.ts';
import { useFileStore } from '@/stores/fileStore.ts';
import type { Collaboration } from '@/types/collaborationType.ts';
import { Role, getRole } from '@/types/enums/Role.ts';
import { Tabs } from '@/types/enums/Tabs.ts';
import type { FileBody } from '@/types/fileBodyType.ts';
import { errorHandler } from '@/utils/axiosUtils.ts';
import { format, parseISO } from 'date-fns';
import { storeToRefs } from 'pinia';
import { computed, ref, watch } from 'vue';
import { useI18n } from 'vue-i18n';

const configurationStore = useConfigurationStore();
const { isInfo, currentTab } = storeToRefs(configurationStore);

const fileStore = useFileStore();
const { refresh, refreshCurrentFile } = fileStore;
const { currentFile } = storeToRefs(fileStore);

const isDev = import.meta.env.DEV;

const { t } = useI18n();

const modelValue = computed<boolean>({
  get() {
    return currentFile.value != undefined && isInfo.value;
  },
  set() {
    isInfo.value = false;
  },
});

const isEdit = ref<boolean>(false);
const tmp = ref<{ title: string; description: string | null }>({ title: '', description: '' });

watch(currentFile, (newValue, oldValue): void => {
  if (newValue != oldValue) initForm();
});

watch(currentTab, (newValue, oldValue): void => {
  if (newValue != oldValue) initForm();
});

watch(modelValue, (newValue, oldValue): void => {
  if (newValue != oldValue) initForm();
});

const canSave = computed<boolean>(() => {
  if (!currentFile.value) return false;

  const hasTitle = tmp.value.title != undefined && tmp.value.title.trim().length > 0;
  const titleHasChanged = tmp.value.title != currentFile.value.title;

  const tmpDesctiption =
    tmp.value.description && tmp.value.description.trim().length > 0 ? tmp.value.description : null;
  const descriptionHasChanged = tmpDesctiption != currentFile.value?.description;

  return (hasTitle && titleHasChanged) || descriptionHasChanged;
});

const edit = (): void => {
  isEdit.value = true;
  document.getElementById('tmp-title')?.focus();
};

const initForm = (): void => {
  isEdit.value = false;
  if (currentFile.value == undefined) return;
  tmp.value = {
    title: currentFile.value.title,
    description: currentFile.value.description,
  };
};

const save = async (): Promise<void> => {
  if (!canSave.value || currentFile.value == undefined) return;
  try {
    await setFile(currentFile.value.id, tmp.value as FileBody);
    refresh(true);
    refreshCurrentFile();
    isEdit.value = false;
  } catch (e) {
    errorHandler(e, true);
  }
};

const roles: Array<Role> = [Role.editor, Role.readonly];

const newUser = ref<any>();
const newRole = ref<Role>();

const updateRole = (collaboration: Collaboration, newValue: Role): void => {
  if (newValue != getRole(collaboration.role)) refreshCurrentFile();
};

const updateVisibility = (newValue: boolean | null): void => {
  if (typeof newValue == 'boolean') refreshCurrentFile();
};

const addUser = (): void => {
  newUser.value = undefined;
  newRole.value = undefined;
};

const onClose = (): void => {
  modelValue.value = false;
};
</script>

<template>
  <v-navigation-drawer v-model="modelValue" location="right" :width="460">
    <v-toolbar :title="t('navigation.title.information')" color="rgba(255, 255, 255, 0)">
      <template #append>
        <v-btn icon="fas fa-xmark" color="default" variant="plain" :alt="t('button.close')" @click="onClose" />
      </template>
    </v-toolbar>
    <v-tabs
      v-model="currentTab"
      align-tabs="center"
      :show-arrows="false"
      hide-slider
      selected-class="slide-group-item--activate"
      fixed-tabs
      class="mx-2"
    >
      <v-tab :value="Tabs.Information" :title="t('navigation.title.information')">
        <v-icon icon="fas fa-circle-info" />
      </v-tab>
      <v-tab v-if="isDev" :value="Tabs.Share" :title="t('navigation.title.share')">
        <v-icon icon="fas fa-share-nodes" />
      </v-tab>
      <v-tab v-if="isDev" :value="Tabs.Histories" :title="t('navigation.title.histories')">
        <v-icon icon="fas fa-clock-rotate-left" />
      </v-tab>
    </v-tabs>
    <v-window v-if="currentFile" v-model="currentTab" class="pa-2">
      <v-window-item :value="Tabs.Information">
        <v-text-field
          id="tmp-title"
          v-model="tmp.title"
          :label="t('information.title')"
          :maxlength="45"
          variant="solo-filled"
          rounded="xl"
          flat
          hide-details
          :readonly="!isEdit"
          class="mb-2"
        />
        <v-textarea
          v-model="tmp.description"
          :label="t('information.description')"
          :maxlength="45"
          variant="solo-filled"
          rounded="xl"
          flat
          hide-details
          :readonly="!isEdit"
          class="mb-2"
        />
        <div class="d-flex mb-2">
          <v-spacer />
          <v-btn v-if="!isEdit" prepend-icon="fas fa-pen" :text="t('button.edit')" variant="tonal" @click="edit" />
          <div v-else>
            <v-btn
              prepend-icon="fas fa-xmark"
              :text="t('button.cancel')"
              variant="tonal"
              color="secondary"
              @click="initForm"
              class="mr-2"
            />
            <v-btn
              prepend-icon="fas fa-save"
              :text="t('button.save')"
              variant="tonal"
              color="success"
              :disabled="!canSave"
              @click="save"
            />
          </div>
        </div>
        <div class="ml-2 mb-2">
          {{ t('information.creationDate', { date: format(parseISO(currentFile.creationDate), 'Pp') }) }}
        </div>
        <div class="ml-2 mb-2">
          {{ t('information.editionDate', { date: format(parseISO(currentFile.editionDate), 'Pp') }) }}
        </div>
      </v-window-item>

      <v-window-item v-if="isDev" :value="Tabs.Share">
        <div class="d-flex align-center mb-2 bg-grey-lighten-3 rounded-xl">
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
        <v-list v-if="currentFile.collaborations" class="pt-0">
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
        <div class="ml-2 mb-1">{{ t('information.visibility') }}</div>
        <v-switch
          :model-value="currentFile.pub"
          :label="t(`visibility.${currentFile.pub ? 'public' : 'private'}`)"
          density="compact"
          inset
          hide-details
          readonly
          @update:model-value="updateVisibility"
        />
      </v-window-item>

      <v-window-item v-if="isDev" :value="Tabs.Histories">
        <v-list v-if="currentFile.histories" class="py-0">
          <v-list-item
            v-for="(history, index) in currentFile.histories"
            :key="index"
            :title="format(parseISO(history.creationDate), 'Pp')"
            rounded="xl"
            :class="[
              index < currentFile.histories.length - 1 ? 'mb-2' : '',
              'pr-2',
              'bg-grey-lighten-3',
              'list-item--custom',
            ]"
          >
            <template #append>
              <v-btn icon="fas fa-eye" color="info" variant="text" size="small" :alt="t('button.view')" />
              <v-btn
                icon="fas fa-clock-rotate-left"
                color="secondary"
                variant="text"
                size="small"
                :alt="t('button.revert')"
              />
              <v-btn icon="fas fa-trash" color="error" variant="text" size="small" :alt="t('button.delete')" />
            </template>
          </v-list-item>
        </v-list>
      </v-window-item>
    </v-window>
  </v-navigation-drawer>
</template>

<style scoped lang="scss">
.slide-group-item--activate {
  background-color: rgba(var(--v-theme-primary), var(--v-activated-opacity)) !important;
}

.share-item--name {
  width: 100%;
}

.share-item--role {
  min-width: 150px;
}

.list-item--custom {
  height: 56px;
}
</style>
