<script setup lang="ts">
import { Menu } from 'lucide-vue-next'
import { computed, onBeforeUnmount, onMounted, ref } from 'vue'

import AppLayout from '@/components/layout/AppLayout.vue'
import DeletePersonModal from '@/components/modals/DeletePersonModal.vue'
import EditPersonModal from '@/components/modals/EditPersonModal.vue'
import PersonsSidebar from '@/components/sidebar/PersonsSidebar.vue'
import ThemeToggle from '@/components/theme/ThemeToggle.vue'
import { usePersons } from '@/hooks/usePersons'
import { useTheme } from '@/hooks/useTheme'
import type { Person, UpdatePersonPayload } from '@/types/person'

const { persons, loading, error, refresh, updatePersonById, deletePersonById } = usePersons()
const { isDark, toggleTheme } = useTheme()

const selectedPerson = ref<Person | null>(null)
const editingPerson = ref<Person | null>(null)
const deletingPerson = ref<Person | null>(null)
const feedback = ref('')
const sidebarOpen = ref(false)

const selectedPersonId = computed(() => selectedPerson.value?.id ?? null)

function selectPerson(person: Person) {
  selectedPerson.value = person
  if (window.matchMedia('(max-width: 767px)').matches) {
    sidebarOpen.value = false
  }
}

function openEdit(person: Person) {
  editingPerson.value = person
}

function openDelete(person: Person) {
  deletingPerson.value = person
}

async function handleUpdate(id: string, payload: UpdatePersonPayload) {
  try {
    await updatePersonById(id, payload)
    feedback.value = 'Pessoa atualizada com sucesso.'
    editingPerson.value = null
  } catch (err) {
    feedback.value = err instanceof Error ? err.message : 'Falha ao atualizar pessoa.'
  }
}

async function handleDelete(id: string) {
  try {
    await deletePersonById(id)
    feedback.value = 'Pessoa excluida com sucesso.'
    deletingPerson.value = null
    if (selectedPerson.value?.id === id) {
      selectedPerson.value = null
    }
  } catch (err) {
    feedback.value = err instanceof Error ? err.message : 'Falha ao excluir pessoa.'
  }
}

function onEscape(event: KeyboardEvent) {
  if (event.key === 'Escape') {
    sidebarOpen.value = false
  }
}

onMounted(async () => {
  await refresh()
  window.addEventListener('keydown', onEscape)
})

onBeforeUnmount(() => window.removeEventListener('keydown', onEscape))
</script>

<template>
  <AppLayout>
    <div class="flex min-h-screen bg-bg text-text-primary">
      <div class="hidden md:block">
        <PersonsSidebar
          :persons="persons"
          :loading="loading"
          :error="error"
          :selected-person-id="selectedPersonId"
          @select="selectPerson"
          @edit="openEdit"
          @remove="openDelete"
        />
      </div>

      <transition name="fade">
        <div
          v-if="sidebarOpen"
          class="fixed inset-0 z-30 bg-black/45 md:hidden"
          @click="sidebarOpen = false"
        />
      </transition>

      <transition name="slide">
        <div v-if="sidebarOpen" class="fixed inset-y-0 left-0 z-40 md:hidden">
          <PersonsSidebar
            :persons="persons"
            :loading="loading"
            :error="error"
            :selected-person-id="selectedPersonId"
            @select="selectPerson"
            @edit="openEdit"
            @remove="openDelete"
          />
        </div>
      </transition>

      <main class="flex-1 p-4 sm:p-6">
        <header class="mb-6 flex items-center justify-between gap-3">
          <div class="flex items-center gap-2">
            <button
              type="button"
              class="rounded-md border border-card-border bg-card p-2 text-text-primary md:hidden"
              aria-label="Abrir pessoas"
              @click="sidebarOpen = true"
            >
              <Menu :size="18" />
            </button>
            <h1 class="text-2xl font-semibold">Agenda</h1>
          </div>

          <ThemeToggle :is-dark="isDark" @toggle="toggleTheme" />
        </header>

        <section class="rounded-lg border border-card-border bg-card p-5 shadow-subtle">
          <p v-if="feedback" class="mb-3 text-sm text-success">{{ feedback }}</p>
          <template v-if="selectedPerson">
            <h2 class="text-lg font-semibold">{{ selectedPerson.name }}</h2>
            <p class="mt-2 text-sm text-text-secondary">
              Aniversario: {{ selectedPerson.birthday || '-' }}
            </p>
            <p class="mt-1 text-xs text-text-secondary">ID: {{ selectedPerson.id }}</p>
          </template>
          <p v-else class="text-sm text-text-secondary">
            Selecione uma pessoa na sidebar para ver detalhes.
          </p>
        </section>
      </main>
    </div>

    <EditPersonModal
      :open="Boolean(editingPerson)"
      :person="editingPerson"
      @close="editingPerson = null"
      @save="handleUpdate"
    />

    <DeletePersonModal
      :open="Boolean(deletingPerson)"
      :person="deletingPerson"
      @close="deletingPerson = null"
      @confirm="handleDelete"
    />
  </AppLayout>
</template>

<style scoped>
.fade-enter-active,
.fade-leave-active {
  transition: opacity 220ms ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}

.slide-enter-active,
.slide-leave-active {
  transition: transform 240ms ease;
}

.slide-enter-from,
.slide-leave-to {
  transform: translateX(-100%);
}
</style>
