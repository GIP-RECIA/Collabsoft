<script setup lang="ts">
import DurationSpan from '@/components/DurationSpan.vue';
import FileMenu from '@/components/FileMenu.vue';
import { useHomeStore } from '@/stores/homeStore.ts';
import type { File } from '@/types/fileType.ts';
import { useSessionStorage } from '@vueuse/core';
import { storeToRefs } from 'pinia';
import { ref, watch } from 'vue';
import { useI18n } from 'vue-i18n';
import { useDisplay } from 'vuetify';

const isDev = import.meta.env.DEV;

const { VITE_API_URI } = import.meta.env;

const homeStore = useHomeStore();
const { search } = storeToRefs(homeStore);

const { t } = useI18n();
const { xs } = useDisplay();

defineProps<{
  files: Array<File> | undefined;
}>();

const headers = ref<Array<any>>([
  { title: t('information.title'), key: 'title', sortable: true, width: '100%' },
  { title: '', key: 'actions', sortable: false, align: 'end' },
]);

const addColumnEditionDate = (): void => {
  const index = headers.value.findIndex((header) => header.key == 'editionDate');
  if (index == -1) {
    headers.value.splice(1, 0, {
      title: t('information.edited'),
      key: 'editionDate',
      sortable: true,
      headerProps: {
        style: 'white-space: nowrap;',
      },
      cellProps: {
        style: 'white-space: nowrap;',
      },
    });
  }
};

const removeColumnEditionDate = (): void => {
  const index = headers.value.findIndex((header) => header.key == 'editionDate');
  if (index >= 0) headers.value.splice(index, 1);
};

watch(xs, (newValue): void => (newValue ? removeColumnEditionDate() : addColumnEditionDate()), { immediate: true });

const sortBy = useSessionStorage<Array<any>>(`${__APP_SLUG__}.sort-by`, [{ key: 'title', order: 'asc' }]);
</script>

<!-- eslint-disable vue/valid-v-slot -->
<template>
  <v-data-table
    :headers="headers"
    :items="files"
    v-model:sort-by="sortBy"
    :search="search"
    filter-keys="title"
    :loading="files == undefined"
    :items-per-page="-1"
    sort-asc-icon="fas fa-sort-up"
    sort-desc-icon="fas fa-sort-down"
    height="0px"
    fixed-header
    hover
    class="h-100 rounded-xl"
  >
    <template #item.title="{ item }">
      <router-link
        :to="item.associatedApp.enabled || isDev ? { name: item.associatedApp.slug, params: { fileId: item.id } } : ''"
        class="d-flex align-center h-100 table-column-title"
      >
        <v-avatar
          icon="fas fa-file"
          :image="
            item.associatedApp.iconPath != null
              ? `${VITE_API_URI}${VITE_API_URI.endsWith('/') ? '' : '/'}${item.associatedApp.iconPath}`
              : undefined
          "
        />
        <span class="text-truncated ms-2">{{ item.title }}</span>
      </router-link>
    </template>
    <template #item.editionDate="{ item }">
      <duration-span :date="item.editionDate" />
    </template>
    <template #item.actions="{ item }">
      <file-menu :file-id="item.id" />
    </template>
    <template #loading>
      <v-skeleton-loader type="table-row@6" />
    </template>
    <template #bottom></template>
  </v-data-table>
</template>

<style scoped lang="scss">
.table-column-title {
  width: 0;
  flex-grow: 1;
  text-decoration: none;
  color: inherit;
}
</style>

<style lang="scss">
thead > tr > th > .v-data-table-header__content > span {
  margin-right: 4px;
}

tbody > tr.v-data-table__tr > td:nth-child(1) {
  display: flex;
}
</style>
