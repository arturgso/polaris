<script setup lang="ts">
import { onBeforeUnmount, onMounted } from 'vue'

import type { Person } from '@/types/person'

defineProps<{
  open: boolean
  person: Person | null
}>()

const emit = defineEmits<{
  close: []
  confirm: [id: string]
}>()

function close() {
  emit('close')
}

function confirmDelete(id: string) {
  emit('confirm', id)
}

function onEscape(event: KeyboardEvent) {
  if (event.key === 'Escape') {
    close()
  }
}

onMounted(() => window.addEventListener('keydown', onEscape))
onBeforeUnmount(() => window.removeEventListener('keydown', onEscape))
</script>

<template>
  <div
    v-if="open"
    class="fixed inset-0 z-40 flex items-center justify-center bg-black/50 p-4"
    @click.self="close"
  >
    <section
      role="dialog"
      aria-modal="true"
      aria-labelledby="delete-person-title"
      class="w-full max-w-sm rounded-lg border border-card-border bg-card p-5 shadow-card"
    >
      <h2 id="delete-person-title" class="text-lg font-semibold text-text-primary">Excluir pessoa</h2>
      <p class="mt-2 text-sm text-text-secondary">
        Tem certeza que deseja excluir
        <strong class="text-text-primary">{{ person?.name }}</strong>
        ?
      </p>

      <div class="mt-5 flex justify-end gap-2">
        <button
          type="button"
          class="rounded-md border border-card-border px-4 py-2 text-sm text-text-secondary hover:bg-secondary"
          @click="close"
        >
          Cancelar
        </button>
        <button
          type="button"
          class="rounded-md bg-danger px-4 py-2 text-sm font-medium text-white hover:brightness-95"
          @click="person && confirmDelete(person.id)"
        >
          Confirmar
        </button>
      </div>
    </section>
  </div>
</template>
