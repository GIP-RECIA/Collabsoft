import { fileURLToPath } from 'node:url'
import { mergeConfig, defineConfig, configDefaults } from 'vitest/config'
import viteConfig from './vite.config'
import { dirname, resolve } from 'node:path';

export default mergeConfig(
  viteConfig,
  defineConfig({
    test: {
      environment: 'jsdom',
      exclude: [...configDefaults.exclude, 'e2e/*'],
      root: fileURLToPath(new URL('./src/test/webapp/spec', import.meta.url)),
      server: {
        deps: {
          inline: ['vuetify'],
        },
      },
      cache: { dir: resolve(dirname(fileURLToPath(import.meta.url)), './') },
    }
  })
)
