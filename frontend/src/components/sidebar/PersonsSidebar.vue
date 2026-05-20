<script setup lang="ts">
import { Pencil, Trash2 } from 'lucide-vue-next'

import type { Person } from '@/types/person'

defineProps<{
  persons: Person[]
  loading: boolean
  error: string
  selectedPersonId: string | null
}>()

const emit = defineEmits<{
  select: [person: Person]
  edit: [person: Person]
  remove: [person: Person]
}>()
</script>

<template>
  <aside class="flex h-full w-80 flex-col border-r border-card-border bg-card">
    <header class="border-b border-card-border p-4">
      <h2 class="text-lg font-semibold text-text-primary">Pessoas</h2>
    </header>

    <div class="flex-1 overflow-y-auto p-2">
      <p v-if="loading" class="p-3 text-sm text-text-secondary">Carregando pessoas...</p>
      <p v-else-if="error" class="p-3 text-sm text-danger">{{ error }}</p>
      <ul v-else class="space-y-1">
        <li v-for="person in persons" :key="person.id" class="group">
          <button
            type="button"
            class="flex w-full items-center justify-between rounded-md px-3 py-2 text-left transition"
            :class="[
              selectedPersonId === person.id
                ? 'bg-hover text-text-primary'
                : 'text-text-secondary hover:bg-secondary hover:text-text-primary',
            ]"
            @click="emit('select', person)"
          >
            <span class="truncate">{{ person.name }}</span>

            <span
              class="flex items-center gap-1 opacity-0 translate-x-1 transition duration-200 group-hover:translate-x-0 group-hover:opacity-100"
            >
              <button
                type="button"
                class="rounded p-1 text-text-secondary hover:bg-bg hover:text-text-primary"
                aria-label="Editar pessoa"
                @click.stop="emit('edit', person)"
              >
                <Pencil :size="14" />
              </button>
              <button
                type="button"
                class="rounded p-1 text-danger hover:bg-bg"
                aria-label="Excluir pessoa"
                @click.stop="emit('remove', person)"
              >
                <Trash2 :size="14" />
              </button>
            </span>
          </button>
        </li>
      </ul>
    </div>
  </aside>
</template>
