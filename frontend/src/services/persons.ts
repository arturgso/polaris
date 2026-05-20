import apiClient from '@/services/api/client'
import type { Person, UpdatePersonPayload } from '@/types/person'

export const personsService = {
  async list(): Promise<Person[]> {
    const { data } = await apiClient.get<Person[]>('/persons')
    return data
  },
  async update(id: string, payload: UpdatePersonPayload): Promise<void> {
    await apiClient.patch(`/persons/${id}`, payload)
  },
  async remove(id: string): Promise<void> {
    await apiClient.delete(`/persons/${id}`)
  },
}
