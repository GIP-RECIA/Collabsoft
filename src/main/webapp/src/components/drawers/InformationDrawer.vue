<script setup lang="ts">
import { useConfigurationStore } from '@/stores/configurationStore';
import { Tabs } from '@/types/enums/Tabs';
import { format, parseISO } from 'date-fns';
import { storeToRefs } from 'pinia';
import { computed, ref } from 'vue';
import { useI18n } from 'vue-i18n';

const configurationStore = useConfigurationStore();
const { refreshCurrentFile } = configurationStore;
const { currentFile, isInfo, currentTab } = storeToRefs(configurationStore);

const { t } = useI18n();

const modelValue = computed<boolean>({
  get() {
    return currentFile.value != undefined && isInfo.value;
  },
  set() {
    isInfo.value = false;
  },
});

const roles = [];

const newUser = ref<any>();
const newRole = ref<string>();

const updateRole = (share: any, newValue: string) => {
  console.log(share, newValue);
  refreshCurrentFile();
};

const updateVisibility = (newValue: boolean) => {
  console.log(newValue);
  refreshCurrentFile();
};

const addUser = () => {
  newUser.value = undefined;
  newRole.value = undefined;
};
</script>

<template>
  <v-navigation-drawer v-model="modelValue" location="right" :width="460">
    <v-toolbar :title="t('navigation.title.information')" color="rgba(255, 255, 255, 0)">
      <template #append>
        <v-btn
          icon="fas fa-xmark"
          color="default"
          variant="plain"
          :alt="t('button.close')"
          @click="modelValue = false"
        />
      </template>
    </v-toolbar>
    <v-tabs
      v-model="currentTab"
      align-tabs="center"
      :show-arrows="false"
      hide-slider
      selected-class="slide-group-item--activate"
      class="mx-2"
      fixed-tabs
    >
      <v-tab :value="Tabs.Information" :title="t('navigation.title.information')">
        <v-icon icon="fas fa-circle-info" />
      </v-tab>
      <v-tab :value="Tabs.Share" :title="t('navigation.title.share')">
        <v-icon icon="fas fa-share-nodes" />
      </v-tab>
      <v-tab :value="Tabs.Histories" :title="t('navigation.title.histories')">
        <v-icon icon="fas fa-clock-rotate-left" />
      </v-tab>
    </v-tabs>
    <v-window v-if="currentFile" v-model="currentTab" class="pa-2">
      <v-window-item :value="Tabs.Information">
        <v-text-field
          :model-value="currentFile.title"
          :label="t('information.title')"
          variant="solo"
          bg-color="grey-lighten-3"
          rounded="xl"
          class="mb-2"
          flat
          hide-details
          readonly
        />
        <v-textarea
          :model-value="currentFile.description"
          :label="t('information.description')"
          variant="solo"
          bg-color="grey-lighten-3"
          rounded="xl"
          class="mb-2"
          flat
          hide-details
          readonly
        />
        <div class="ml-2 mb-2">
          {{ t('information.creationDate') }} {{ format(parseISO(currentFile.creationDate), 'Pp') }}
        </div>
        <div class="ml-2 mb-2">
          {{ t('information.editionDate') }} {{ format(parseISO(currentFile.editionDate), 'Pp') }}
        </div>
      </v-window-item>

      <v-window-item :value="Tabs.Share">
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
        <v-list class="pt-0">
          <v-list-item
            v-for="(share, index) in currentFile.shared"
            :key="index"
            rounded="xl"
            :class="[
              index < currentFile.shared.length - 1 ? 'mb-2' : '',
              'pr-2',
              'bg-grey-lighten-3',
              'list-item--custom',
            ]"
          >
            <template #default>
              <div class="d-flex">
                <div class="d-flex align-center share-item--name">
                  {{ share.name }}
                </div>
                <v-select
                  :model-value="share.role"
                  :items="roles"
                  variant="solo"
                  density="compact"
                  rounded="xl"
                  bg-color="rgba(255,255,255, 0)"
                  class="share-item--role"
                  flat
                  hide-details
                  hide-no-data
                  @update:model-value="(newValue) => updateRole(share, newValue)"
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
          @update:model-value="updateVisibility"
        />
      </v-window-item>

      <v-window-item :value="Tabs.Histories">
        <v-list class="py-0">
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
