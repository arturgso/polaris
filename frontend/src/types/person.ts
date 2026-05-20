export type Person = {
  id: string
  name: string
  birthday: string
  createdAt: string
  updatedAt: string
}

export type UpdatePersonPayload = {
  name: string
  birthdayDay: number
  birthdayMonth: number
}
