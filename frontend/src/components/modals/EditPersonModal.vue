<script setup lang="ts">
import { nextTick, onBeforeUnmount, onMounted, ref, watch } from 'vue'

import type { Person, UpdatePersonPayload } from '@/types/person'

const props = defineProps<{
  open: boolean
  person: Person | null
}>()

const emit = defineEmits<{
  close: []
  save: [id: string, payload: UpdatePersonPayload]
}>()

const dialogRef = ref<HTMLElement | null>(null)
const lastFocusedElement = ref<HTMLElement | null>(null)
const name = ref('')
const birthdayDay = ref<number>(1)
const birthdayMonth = ref<number>(1)

function parseBirthday(birthday: string) {
  const [month, day] = birthday.split('-').map(Number)
  return {
    day: Number.isFinite(day) ? day : 1,
    month: Number.isFinite(month) ? month : 1,
  }
}

watch(
  () => props.person,
  (person) => {
    if (!person) {
      return
    }

    const birthday = parseBirthday(person.birthday)
    name.value = person.name
    birthdayDay.value = birthday.day
    birthdayMonth.value = birthday.month
  },
  { immediate: true },
)

watch(
  () => props.open,
  async (open) => {
    if (open) {
      lastFocusedElement.value = document.activeElement as HTMLElement
      await nextTick()
      dialogRef.value?.querySelector<HTMLInputElement>('input')?.focus()
      return
    }

    lastFocusedElement.value?.focus()
  },
)

function close() {
  emit('close')
}

function onSubmit() {
  if (!props.person) {
    return
  }

  emit('save', props.person.id, {
    name: name.value,
    birthdayDay: birthdayDay.value,
    birthdayMonth: birthdayMonth.value,
  })
}

function onEscape(event: KeyboardEvent) {
  if (event.key === 'Escape' && props.open) {
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
      ref="dialogRef"
      role="dialog"
      aria-modal="true"
      aria-labelledby="edit-person-title"
      class="w-full max-w-md rounded-lg border border-card-border bg-card p-5 shadow-card"
    >
      <h2 id="edit-person-title" class="text-lg font-semibold text-text-primary">Editar pessoa</h2>

      <form class="mt-4 space-y-4" @submit.prevent="onSubmit">
        <label class="block text-sm text-text-secondary">
          Nome
          <input
            v-model="name"
            required
            class="mt-1 w-full rounded-md border border-card-border bg-bg px-3 py-2 text-text-primary"
          />
        </label>

        <div class="grid grid-cols-2 gap-3">
          <label class="block text-sm text-text-secondary">
            Dia
            <input
              v-model.number="birthdayDay"
              min="1"
              max="31"
              required
              type="number"
              class="mt-1 w-full rounded-md border border-card-border bg-bg px-3 py-2 text-text-primary"
            />
          </label>

          <label class="block text-sm text-text-secondary">
            Mês
            <input
              v-model.number="birthdayMonth"
              min="1"
              max="12"
              required
              type="number"
              class="mt-1 w-full rounded-md border border-card-border bg-bg px-3 py-2 text-text-primary"
            />
          </label>
        </div>

        <div class="flex justify-end gap-2 pt-2">
          <button
            type="button"
            class="rounded-md border border-card-border px-4 py-2 text-sm text-text-secondary hover:bg-secondary"
            @click="close"
          >
            Cancelar
          </button>
          <button
            type="submit"
            class="rounded-md bg-primary px-4 py-2 text-sm font-medium text-text-primary hover:brightness-95"
          >
            Salvar
          </button>
        </div>
      </form>
    </section>
  </div>
</template>
