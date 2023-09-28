<script setup lang="ts">
import FileMenu from '@/components/FileMenu.vue';
import { useConfigurationStore } from '@/stores/configurationStore';
import { storeToRefs } from 'pinia';
import { ref } from 'vue';
import { useI18n } from 'vue-i18n';

const configurationStore = useConfigurationStore();
const { selectedFile, isShare } = storeToRefs(configurationStore);

const { t } = useI18n();

const props = defineProps<{
  file: any;
}>();

const isStarred = ref(false);

const star = () => {
  selectedFile.value = props.file.id;
  isStarred.value = !isStarred.value;
};

const share = () => {
  selectedFile.value = props.file.id;
  isShare.value = true;
};
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
            <file-menu @click="selectedFile = file.id" />
          </template>
        </v-toolbar>
      </div>
    </v-img>
  </v-card>
</template>
