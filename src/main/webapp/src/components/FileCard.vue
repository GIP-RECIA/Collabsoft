<script setup lang="ts">
import { ref } from 'vue';
import { useI18n } from 'vue-i18n';

const { t } = useI18n();

defineProps<{
  file: any;
}>();

const isStarred = ref(false);

const star = () => {
  isStarred.value = !isStarred.value;
};

const share = () => {};

const options = () => {};

const information = () => {};
const createHistory = () => {};
const exportOnNextloud = () => {};
const download = () => {};
const deleteItem = () => {};

let rounded: string = 'xl';
</script>

<template>
  <v-card class="w-100" flat>
    <v-img height="200" src="https://cdn.vuetifyjs.com/docs/images/cards/purple-flowers.jpg" cover class="text-white">
      <div class="d-flex flex-column h-100">
        <v-toolbar color="rgba(255, 255, 255, 0)" class="text-white">
          <template #prepend>
            <v-btn :icon="`${isStarred ? 'fas' : 'far'} fa-star`" @click="star" />
          </template>
          <template #append>
            <v-btn icon="fas fa-share-nodes" @click="share" />
          </template>
        </v-toolbar>
        <router-link
          :to="{ name: 'app', params: { appSlug: file.associatedApp.slug, fileId: file.id } }"
          class="flex-grow-1"
        >
        </router-link>
        <v-toolbar color="rgba(255, 255, 255, 0)" class="text-white">
          <v-toolbar-title class="text-h6">{{ file.title }}</v-toolbar-title>
          <template #append>
            <v-menu>
              <template #activator="{ props }">
                <v-btn v-bind="props" icon="fas fa-ellipsis-vertical" @click="options" />
              </template>

              <v-list :rounded="rounded" class="pa-2">
                <v-list-item
                  prepend-icon="fas fa-circle-info"
                  :title="t('menu.item.information')"
                  :rounded="rounded"
                  @click="information"
                />
                <v-list-item
                  prepend-icon="fas fa-clock-rotate-left"
                  :title="t('menu.item.createHistory')"
                  :rounded="rounded"
                  @click="createHistory"
                />
                <v-list-item
                  prepend-icon="fas fa-cloud"
                  :title="t('menu.item.exportOnNextcloud')"
                  :rounded="rounded"
                  @click="exportOnNextloud"
                />
                <v-list-item
                  prepend-icon="fas fa-download"
                  :title="t('menu.item.download')"
                  :rounded="rounded"
                  @click="download"
                />
                <v-divider class="my-2" />
                <v-list-item
                  prepend-icon="fas fa-trash"
                  :title="t('menu.item.delete')"
                  :rounded="rounded"
                  @click="deleteItem"
                />
              </v-list>
            </v-menu>
          </template>
        </v-toolbar>
      </div>
    </v-img>
  </v-card>
</template>
