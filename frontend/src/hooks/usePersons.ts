import { ref } from 'vue'

import { personsService } from '@/services/persons'
import type { Person, UpdatePersonPayload } from '@/types/person'

export function usePersons() {
  const persons = ref<Person[]>([])
  const loading = ref(false)
  const error = ref('')

  async function refresh() {
    loading.value = true
    error.value = ''

    try {
      persons.value = await personsService.list()
    } catch (err) {
      error.value = err instanceof Error ? err.message : 'Failed to load persons'
    } finally {
      loading.value = false
    }
  }

  async function updatePersonById(id: string, payload: UpdatePersonPayload) {
    const previous = [...persons.value]
    persons.value = persons.value.map((person) =>
      person.id === id
        ? {
            ...person,
            name: payload.name,
            birthday: `${String(payload.birthdayMonth).padStart(2, '0')}-${String(payload.birthdayDay).padStart(2, '0')}`,
          }
        : person,
    )

    try {
      await personsService.update(id, payload)
      await refresh()
    } catch (err) {
      persons.value = previous
      throw err
    }
  }

  async function deletePersonById(id: string) {
    const previous = [...persons.value]
    persons.value = persons.value.filter((person) => person.id !== id)

    try {
      await personsService.remove(id)
      await refresh()
    } catch (err) {
      persons.value = previous
      throw err
    }
  }

  return {
    persons,
    loading,
    error,
    refresh,
    updatePersonById,
    deletePersonById,
  }
}
