# Frontend Engineering Guide

## Stack

- Framework: Vue 3 detected from `@vitejs/plugin-vue`, `.vue` files, and `createApp`.
- Language: TypeScript detected from `vite.config.ts`, `main.ts`, and Vue SFCs using `lang="ts"`.
- Bundler: Vite 8.
- Package manager: Bun.
- Routing: Vue Router with centralized route definitions.
- Styling: Tailwind CSS 4 with semantic CSS design tokens.
- Icons: Lucide Vue.
- Quality: ESLint and Prettier.

## Architecture

The frontend lives in `frontend/` and uses `src/` as the application root.

- `components/ui`: reusable primitive UI components.
- `components/layout`: layout components shared by pages.
- `pages`: route-level Vue components.
- `router`: route registration and router setup.
- `styles`: global CSS, Tailwind entrypoint, and theme tokens.
- `lib`: framework-agnostic helpers.
- `hooks`: Vue composables.
- `services`: API clients and integration modules.
- `types`: shared TypeScript types.
- `constants`: shared constants.
- `assets`: static assets imported by the app.
- `config`: runtime-safe app configuration.

## Conventions

- Use Vue single-file components with `<script setup lang="ts">`.
- Use `@/` for imports from `src`.
- Keep pages thin; move reusable UI into `components/ui` and page shells into `components/layout`.
- Register application routes in `src/router/index.ts`.
- Prefer semantic theme tokens over hard-coded color values.
- Use Lucide Vue icons for command and interface icons when an icon is needed.

## Styling

Theme tokens are defined in `src/styles/theme.css` and consumed by Tailwind through `src/styles/globals.css`.

Supported semantic color variables:

- `--bg`
- `--bg-secondary`
- `--card`
- `--card-border`
- `--primary`
- `--secondary`
- `--accent`
- `--hover`
- `--disabled`
- `--text-primary`
- `--text-secondary`
- `--success`
- `--warning`
- `--danger`

Light mode is defined on `:root`. Dark mode is enabled by applying `.dark` to the document root.

## Commands

- Install dependencies: `bun install`
- Start dev server: `bun run dev`
- Build: `bun run build`
- Preview production build: `bun run preview`
- Lint: `bun run lint`
- Format: `bun run format`
- Check formatting: `bun run format:check`

## Routes

- `/`: home page using the shared layout and theme.
- `/colors`: automatic color-token preview generated from loaded CSS variables.

To add a page, create a Vue component in `src/pages` and add a route record in `src/router/index.ts`.

## Adding Colors

Add new semantic color variables in `src/styles/theme.css` for both `:root` and `.dark`. The `/colors` route automatically lists CSS custom properties whose computed values are valid colors.

## Commits

Use concise conventional commits, for example:

- `feat(frontend): add account settings page`
- `fix(frontend): correct color token contrast`
- `chore(frontend): update lint config`
