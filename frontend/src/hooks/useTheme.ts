import { computed, ref } from 'vue'

const THEME_KEY = 'polaris-theme'
type Theme = 'light' | 'dark'

const stored = (localStorage.getItem(THEME_KEY) as Theme | null) ?? 'light'
const theme = ref<Theme>(stored)

function applyTheme(nextTheme: Theme) {
  theme.value = nextTheme
  document.documentElement.classList.toggle('dark', nextTheme === 'dark')
  localStorage.setItem(THEME_KEY, nextTheme)
}

applyTheme(theme.value)

export function useTheme() {
  const isDark = computed(() => theme.value === 'dark')

  function toggleTheme() {
    applyTheme(isDark.value ? 'light' : 'dark')
  }

  return {
    isDark,
    theme,
    toggleTheme,
  }
}
