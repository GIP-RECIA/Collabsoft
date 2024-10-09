import { fileURLToPath } from 'node:url'
import { configDefaults, defineConfig, mergeConfig } from 'vitest/config'
import viteConfig from './vite.config.ts'

export default mergeConfig(
  viteConfig({ mode: 'test' }),
  defineConfig({
    test: {
      globals: true,
      environment: 'jsdom',
      exclude: [...configDefaults.exclude],
      root: fileURLToPath(new URL('./src/test/webapp/spec', import.meta.url)),
      server: {
        deps: {
          inline: ['vuetify'],
        },
      },
    },
  }),
)
