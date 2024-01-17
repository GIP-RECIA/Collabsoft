<script setup lang="ts">
import { saveFile } from '@/services/fileService.ts';
import { useConfigurationStore } from '@/stores/configurationStore.ts';
import { errorHandler } from '@/utils/axiosUtils.ts';
import { storeToRefs } from 'pinia';
import { computed, ref } from 'vue';
import { useI18n } from 'vue-i18n';

const configurationStore = useConfigurationStore();
const { refresh } = configurationStore;
const { isNew } = storeToRefs(configurationStore);

const isDev = import.meta.env.DEV;

const { t } = useI18n();

const modelValue = computed<boolean>({
  get() {
    return isNew.value;
  },
  set() {},
});

const fileType = ref<number | undefined>(isDev ? undefined : 1);
const title = ref<string>();
const description = ref<string>();
const pub = ref<boolean>(false);

const canSave = computed<boolean>(
  () => fileType.value != undefined && title.value != undefined && title.value.trim().length > 0,
);

const save = async (): Promise<void> => {
  if (!canSave.value) return;
  try {
    await saveFile({
      title: title.value!,
      description: description.value && description.value.trim().length > 0 ? description.value : null,
      blob: '',
      associatedAppId: fileType.value!,
      pub: pub.value,
    });
    isNew.value = false;
    refresh(true);
  } catch (e) {
    errorHandler(e, true);
  }
};
</script>

<template>
  <v-dialog v-model="modelValue" :max-width="1024">
    <v-card rounded="xl">
      <v-toolbar :title="t('dialog.file.title')" color="rgba(255, 255, 255, 0)">
        <template #append>
          <v-btn icon="fas fa-xmark" color="default" variant="plain" :alt="t('button.close')" @click="isNew = false" />
        </template>
      </v-toolbar>
      <v-card-text>
        <div v-if="isDev">
          <div class="ml-2 mb-2">{{ t('dialog.file.description') }}</div>
          <v-btn-toggle v-model="fileType" mandatory class="mb-3">
            <v-btn text="tldraw" :value="1" rounded="xl" />
            <v-btn text="WiseMapping" :value="2" rounded="xl" />
          </v-btn-toggle>
        </div>
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
          :maxlength="45"
          variant="solo-filled"
          rounded="xl"
          flat
          hide-details
          class="mb-3"
        />
        <div v-if="isDev">
          <div class="ml-2 mb-1">{{ t('information.visibility') }}</div>
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
        <v-btn prepend-icon="fas fa-save" :text="t('button.save')" color="success" :disabled="!canSave" @click="save" />
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>
