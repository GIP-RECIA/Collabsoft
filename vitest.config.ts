import { configDefaults, defineConfig, mergeConfig } from 'vitest/config'
import viteConfig from './vite.config'

export default mergeConfig(
  viteConfig({ command: 'serve', mode: 'test' }),
  defineConfig({
    test: {
      globals: true,
      dir: './src/test/webapp/spec',
      environment: 'jsdom',
      exclude: [...configDefaults.exclude],
      server: {
        deps: {
          inline: ['vuetify'],
        },
      },
    },
  }),
)
