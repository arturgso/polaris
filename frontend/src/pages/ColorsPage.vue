<script setup lang="ts">
import { Paintbrush } from 'lucide-vue-next'
import { computed, onMounted, ref } from 'vue'

import AppLayout from '@/components/layout/AppLayout.vue'

type ColorToken = {
  category: string
  hex: string
  name: string
  value: string
}

const tokens = ref<ColorToken[]>([])

const groupedTokens = computed(() => {
  return tokens.value.reduce<Record<string, ColorToken[]>>((groups, token) => {
    groups[token.category] = groups[token.category] ?? []
    groups[token.category].push(token)

    return groups
  }, {})
})

function getTokenCategory(name: string) {
  if (name.includes('text')) {
    return 'Text'
  }

  if (['--success', '--warning', '--danger'].includes(name)) {
    return 'Feedback'
  }

  if (['--hover', '--disabled'].includes(name)) {
    return 'State'
  }

  if (['--primary', '--secondary', '--accent'].includes(name)) {
    return 'Brand'
  }

  if (name.includes('bg') || name.includes('card')) {
    return 'Surface'
  }

  return 'Theme'
}

function cssColorToHex(value: string) {
  const probe = document.createElement('span')

  probe.style.color = value
  document.body.append(probe)

  const rgb = getComputedStyle(probe).color
  probe.remove()

  const channels = rgb
    .match(/\d+(\.\d+)?/g)
    ?.slice(0, 3)
    .map(Number)

  if (!channels || channels.length < 3) {
    return value
  }

  const hex = `#${channels
    .map((channel) => Math.round(channel).toString(16).padStart(2, '0'))
    .join('')}`

  return hex
}

function isColorValue(value: string) {
  if (!value) {
    return false
  }

  const probe = document.createElement('span')

  probe.style.color = ''
  probe.style.color = value

  return probe.style.color !== ''
}

function getDeclaredColorTokenNames() {
  const declaredNames = new Set<string>()

  for (const sheet of Array.from(document.styleSheets)) {
    try {
      for (const rule of Array.from(sheet.cssRules)) {
        for (const match of rule.cssText.matchAll(/--[a-z0-9-]+(?=\s*:)/gi)) {
          const name = match[0]
          const value = getComputedStyle(document.documentElement)
            .getPropertyValue(name)
            .trim()

          if (isColorValue(value)) {
            declaredNames.add(name)
          }
        }
      }
    } catch {
      continue
    }
  }

  return Array.from(declaredNames).sort((first, second) => first.localeCompare(second))
}

onMounted(() => {
  const styles = getComputedStyle(document.documentElement)

  tokens.value = getDeclaredColorTokenNames().map((name) => {
    const value = styles.getPropertyValue(name).trim()

    return {
      category: getTokenCategory(name),
      hex: cssColorToHex(value),
      name,
      value,
    }
  })
})
</script>

<template>
  <AppLayout>
    <main class="mx-auto w-full max-w-6xl px-6 py-10 sm:py-14">
      <div class="mb-10 flex items-center gap-3">
        <div
          class="flex size-10 items-center justify-center rounded-app border border-card-border bg-card text-accent shadow-subtle"
        >
          <Paintbrush :size="20" aria-hidden="true" />
        </div>
        <div>
          <h1 class="text-3xl font-semibold tracking-normal text-text-primary">Colors</h1>
          <p class="text-sm text-text-secondary">Design tokens loaded from CSS.</p>
        </div>
      </div>

      <div class="space-y-8">
        <section
          v-for="(items, category) in groupedTokens"
          :key="category"
          class="space-y-3"
        >
          <h2 class="text-sm font-medium uppercase tracking-normal text-text-secondary">
            {{ category }}
          </h2>

          <div class="grid gap-3 sm:grid-cols-2 lg:grid-cols-3">
            <article
              v-for="token in items"
              :key="token.name"
              class="overflow-hidden rounded-app border border-card-border bg-card shadow-subtle"
            >
              <div
                class="h-20 border-b border-card-border"
                :style="{ background: token.value }"
              />
              <div class="space-y-2 p-4">
                <div class="flex items-start justify-between gap-3">
                  <h3 class="font-medium text-text-primary">{{ token.name }}</h3>
                  <span
                    class="rounded-sm bg-secondary px-2 py-1 text-xs text-text-secondary"
                  >
                    {{ token.hex }}
                  </span>
                </div>
                <p class="font-mono text-xs text-text-secondary">{{ token.value }}</p>
              </div>
            </article>
          </div>
        </section>
      </div>
    </main>
  </AppLayout>
</template>
