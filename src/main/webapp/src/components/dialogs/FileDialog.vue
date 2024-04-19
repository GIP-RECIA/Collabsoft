<script setup lang="ts">
import { saveFile } from '@/services/fileService.ts';
import { useConfigurationStore } from '@/stores/configurationStore.ts';
import { useFileStore } from '@/stores/fileStore.ts';
import { useHomeStore } from '@/stores/homeStore.ts';
import { errorHandler } from '@/utils/axiosUtils.ts';
import debounce from 'lodash.debounce';
import { storeToRefs } from 'pinia';
import { computed, ref } from 'vue';
import { useI18n } from 'vue-i18n';

const isDev = import.meta.env.DEV;

const configurationStore = useConfigurationStore();
const { availableApps } = storeToRefs(configurationStore);

const fileStore = useFileStore();
const { refreshFiles } = fileStore;

const homeStore = useHomeStore();
const { isNew } = storeToRefs(homeStore);

const { t } = useI18n();

const modelValue = computed<boolean>({
  get() {
    return isNew.value;
  },
  set() {},
});

const fileType = ref<number | undefined>(availableApps.value[0].id);
const title = ref<string | undefined>();
const description = ref<string | undefined>();
const pub = ref<boolean>(false);

const canSave = computed<boolean>(
  () => fileType.value != undefined && title.value != undefined && title.value.trim().length > 0,
);

const onSave = async (): Promise<void> => {
  if (!canSave.value) return;
  try {
    await saveFile({
      title: title.value!,
      description: description.value && description.value.trim().length > 0 ? description.value : null,
      blob: '',
      associatedAppId: fileType.value!,
      pub: pub.value,
    });
    refreshFiles(true);
    onClose();
  } catch (e) {
    errorHandler(e, true);
  }
};

const onClose = (): void => {
  isNew.value = false;
  reset();
};

const reset = debounce((): void => {
  fileType.value = availableApps.value.length > 1 ? undefined : availableApps.value[0].id;
  title.value = undefined;
  description.value = undefined;
  pub.value = false;
}, 200);
</script>

<template>
  <v-dialog v-model="modelValue" :max-width="1024">
    <v-card rounded="xl">
      <v-toolbar :title="t('dialog.file.title')" color="rgba(255, 255, 255, 0)">
        <template #append>
          <v-btn icon="fas fa-xmark" color="default" variant="plain" :alt="t('button.close')" @click="onClose" />
        </template>
      </v-toolbar>
      <v-card-text>
        <div class="ms-2 mb-2">{{ t('dialog.file.description') }}</div>
        <v-btn-toggle v-model="fileType" mandatory class="mb-3">
          <v-btn
            v-for="(app, index) in availableApps"
            :key="index"
            :text="t(`application.${app.slug}`)"
            :value="app.id"
            rounded="xl"
          />
        </v-btn-toggle>
        <v-text-field
          v-model="title"
          :label="t('information.title')"
          :maxlength="45"
          variant="solo-filled"
          rounded="xl"
          flat
          hide-details
          class="mb-3"
        />
        <v-textarea
          v-model="description"
          :label="t('information.description')"
          :maxlength="255"
          variant="solo-filled"
          rounded="xl"
          flat
          hide-details
          class="mb-3"
        />
        <div v-if="isDev">
          <div class="ms-2 mb-1">{{ t('information.visibility') }}</div>
          <v-switch
            v-model="pub"
            :label="t(`visibility.${pub ? 'public' : 'private'}`)"
            density="compact"
            inset
            hide-details
          />
        </div>
      </v-card-text>
      <v-card-actions>
        <v-spacer />
        <v-btn
          prepend-icon="fas fa-save"
          :text="t('button.save')"
          color="success"
          :disabled="!canSave"
          @click="onSave"
        />
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>
