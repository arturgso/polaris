<script setup lang="ts">
import { Moon, Sun } from 'lucide-vue-next'

const props = defineProps<{
  isDark: boolean
}>()

const emit = defineEmits<{
  toggle: []
}>()
</script>

<template>
  <button
    type="button"
    class="inline-flex items-center gap-2 rounded-md border border-card-border bg-card px-3 py-2 text-sm text-text-primary shadow-subtle hover:bg-secondary"
    @click="emit('toggle')"
  >
    <Sun v-if="!props.isDark" :size="16" aria-hidden="true" />
    <Moon v-else :size="16" aria-hidden="true" />
    <span>{{ props.isDark ? 'Modo escuro' : 'Modo claro' }}</span>
  </button>
</template>
