<script setup lang="ts">
import { setFile } from '@/services/fileService.ts';
import { useFileStore } from '@/stores/fileStore.ts';
import { useHomeStore } from '@/stores/homeStore.ts';
import type { FileBody } from '@/types/fileBodyType.ts';
import { errorHandler } from '@/utils/axiosUtils.ts';
import { format, parseISO } from 'date-fns';
import { storeToRefs } from 'pinia';
import { computed, onMounted, ref, watch } from 'vue';
import { useI18n } from 'vue-i18n';

const fileStore = useFileStore();
const { refresh, refreshCurrentFile } = fileStore;
const { currentFile } = storeToRefs(fileStore);

const homeStore = useHomeStore();
const { isInfo, currentTab } = storeToRefs(homeStore);

const { t } = useI18n();

const isEdit = ref<boolean>(false);
const tmp = ref<{ title: string; description: string | null }>({ title: '', description: '' });

const initForm = (): void => {
  isEdit.value = false;
  if (currentFile.value == undefined) return;
  tmp.value = {
    title: currentFile.value.title,
    description: currentFile.value.description,
  };
};

const edit = (): void => {
  isEdit.value = true;
  document.getElementById('tmp-title')?.focus();
};

const canSave = computed<boolean>(() => {
  if (!currentFile.value) return false;

  const hasTitle = tmp.value.title != undefined && tmp.value.title.trim().length > 0;
  const titleHasChanged = tmp.value.title != currentFile.value.title;

  const tmpDesctiption =
    tmp.value.description && tmp.value.description.trim().length > 0 ? tmp.value.description : null;
  const descriptionHasChanged = tmpDesctiption != currentFile.value?.description;

  return hasTitle && (titleHasChanged || descriptionHasChanged);
});

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

watch(currentFile, (): void => {
  initForm();
});

watch(currentTab, (): void => {
  initForm();
});

watch(isInfo, (): void => {
  initForm();
});

onMounted(() => {
  initForm();
});
</script>

<template>
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
    :maxlength="255"
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
        class="me-2"
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
  <div class="ms-2 mb-2">
    {{ t('information.creationDate', { date: currentFile ? format(parseISO(currentFile.creationDate), 'Pp') : '' }) }}
  </div>
  <div class="ms-2 mb-2">
    {{ t('information.editionDate', { date: currentFile ? format(parseISO(currentFile.editionDate), 'Pp') : '' }) }}
  </div>
</template>
