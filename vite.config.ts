/* eslint-disable node/prefer-global/process */
import { readFileSync } from 'node:fs'
import { fileURLToPath, URL } from 'node:url'
import VueI18nPlugin from '@intlify/unplugin-vue-i18n/vite'
import vue from '@vitejs/plugin-vue'
import { defineConfig, loadEnv } from 'vite'
import vuetify from 'vite-plugin-vuetify'
import { parseString } from 'xml2js'
import pkg from './package.json'
import { slugify } from './src/main/webapp/src/utils/stringUtils.ts'

// https://vitejs.dev/config/
export default ({ mode }: { mode: string }) => {
  process.env = { ...process.env, ...loadEnv(mode, process.cwd()) }

  const appName = JSON.stringify(process.env.VITE_APP_NAME)

  const appSlug = JSON.stringify(process.env.VITE_APP_SLUG ? process.env.VITE_APP_SLUG : slugify(appName))

  const backVersion = (): string => {
    let version
    const pomXml = readFileSync('./pom.xml', 'utf8')
    parseString(pomXml, (err, result) => {
      if (err)
        console.error(err)
      else version = result.project.version[0]
    })

    return `${version}`
  }

  const appsVersions = (): string => {
    return Object.entries(pkg.dependencies)
      .filter(dep => dep[0].startsWith('@gip-recia/'))
      .map(([key, value]) => `\n - ${key} ${value}`)
      .join('')
  }

  const infoLog = (): string => {
    return JSON.stringify(`Version: ${backVersion()}\n\nApps: ${appsVersions()}`)
  }

  return defineConfig({
    base: `${process.env.VITE_BASE_URI}/ui`,
    root: './src/main/webapp',
    envDir: '../../../',
    plugins: [
      vue({
        template: {
          compilerOptions: {
            isCustomElement: tag =>
              [
                'extended-uportal-header',
                'extended-uportal-footer',
                'tldraw-editor',
                'wisemapping-editor',
              ].includes(tag),
          },
        },
      }),
      vuetify({ styles: 'sass' }),
      VueI18nPlugin({}),
    ],
    resolve: {
      alias: {
        '@': fileURLToPath(new URL('./src/main/webapp/src', import.meta.url)),
      },
    },
    optimizeDeps: {
      include: ['vuetify'],
    },
    server: {
      proxy: {
        '^(?:/[a-zA-Z0-9_-]+)?/api': {
          target: process.env.VITE_PROXY_API_URL,
          changeOrigin: true,
        },
      },
    },
    build: {
      rollupOptions: {
        output: {
          assetFileNames: 'assets/[name].[ext]',
          entryFileNames: 'assets/[name].js',
          chunkFileNames: 'assets/[name].js',
        },
      },
    },
    define: {
      __APP_NAME__: appName,
      __APP_SLUG__: appSlug,
      __INFO_LOG__: infoLog(),
    },
  })
}
