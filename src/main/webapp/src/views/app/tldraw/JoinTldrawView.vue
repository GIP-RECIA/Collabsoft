<script setup lang="ts">
import { onMounted, onUnmounted } from 'vue';
import { useTheme } from 'vuetify';

const { VITE_USER_INFO_API_URI } = import.meta.env;

const theme = useTheme();

const styleObserver = new MutationObserver((mutationList: Array<MutationRecord>) => {
  for (const mutation of mutationList) {
    if (mutation.type === 'attributes' && mutation.attributeName === 'style') {
      document.body.removeAttribute('style');
    }
  }
});

onMounted(() => {
  styleObserver.observe(document.body, { attributes: true });
});

onUnmounted(() => {
  styleObserver.disconnect();
});
</script>

<template>
  <tldraw-editor :user-info-api-url="VITE_USER_INFO_API_URI" :dark-mode="theme.global.name.value == 'dark'" />
</template>
