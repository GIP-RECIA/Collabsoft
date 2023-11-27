<script setup lang="ts">
import FileMenu from '@/components/FileMenu.vue';
import { useConfigurationStore } from '@/stores/configurationStore.ts';
import { Tabs } from '@/types/enums/Tabs.ts';
import { storeToRefs } from 'pinia';
import { computed, ref } from 'vue';
import { useI18n } from 'vue-i18n';

const configurationStore = useConfigurationStore();
const { loadFile } = configurationStore;
const { isInfo, currentTab } = storeToRefs(configurationStore);

const { t } = useI18n();

const props = defineProps<{
  file: any;
}>();

const isStarred = ref(false);

const star = () => {
  isStarred.value = !isStarred.value;
};

const share = () => {
  loadFile(props.file.id);
  currentTab.value = Tabs.Share;
  isInfo.value = true;
};

const filePreview = computed(() => {
  switch (props.file.associatedApp.id) {
    case 1:
      return 'https://cdn.vuetifyjs.com/docs/images/cards/docks.jpg';
    case 2:
      return 'https://cdn.vuetifyjs.com/docs/images/cards/purple-flowers.jpg';
    default:
      return '';
  }
});
</script>

<template>
  <v-card class="w-100" rounded="xl" flat>
    <v-img height="200" :src="filePreview" cover class="text-white">
      <div class="d-flex flex-column h-100">
        <v-toolbar color="rgba(255, 255, 255, 0)" class="text-white">
          <template #prepend>
            <v-btn
              :icon="`${isStarred ? 'fas' : 'far'} fa-star`"
              :color="isStarred ? 'yellow' : 'default'"
              :alt="t(`button.${isStarred ? 'unstar' : 'star'}`)"
              @click="star"
            />
          </template>
          <template #append>
            <v-btn icon="fas fa-share-nodes" :alt="t('button.share')" @click="share" />
          </template>
        </v-toolbar>
        <router-link
          :to="{ name: 'app', params: { appSlug: file.associatedApp.slug, fileId: file.id } }"
          class="flex-grow-1"
        />
        <v-toolbar color="rgba(255, 255, 255, 0)" :title="file.title" class="text-white">
          <template #append>
            <file-menu @click="loadFile(file.id)" />
          </template>
        </v-toolbar>
      </div>
    </v-img>
  </v-card>
</template>
