import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import { resolve } from 'path'

import AutoImport from 'unplugin-auto-import/vite'
import Components from 'unplugin-vue-components/vite'
import { ElementPlusResolver } from 'unplugin-vue-components/resolvers'

import viteCompression from 'vite-plugin-compression'
// 删除这行 - import importToCDN from 'vite-plugin-cdn-import'

export default defineConfig({
  plugins: [
    vue(),
    AutoImport({
      resolvers: [ElementPlusResolver({ importStyle: 'css' })],
    }),
    Components({
      resolvers: [ElementPlusResolver({ importStyle: 'css' })],
    }),
    viteCompression({
      verbose: true,
      disable: false,
      threshold: 10240,
      algorithm: 'gzip',
      ext: '.gz',
    }),
    // 删除整个 importToCDN 配置块
  ],
  server: {
    host: '0.0.0.0',
    port: 3000,
    open: true,
    proxy: {
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/api/, ''),
      },
    },
  },
  resolve: {
    alias: [
      {
        find: '@',
        replacement: resolve(__dirname, 'src'),
      },
    ],
  },
  build: {
    target: 'es2020',
    rollupOptions: {
      output: {
        manualChunks(id) {
          if (
            id.includes('vue') ||
            id.includes('vue-router') ||
            id.includes('pinia')
          ) {
            return 'vendor-vue'
          }
          if (id.includes('element-plus')) {
            return 'vendor-element-plus'
          }
          if (id.includes('echarts')) {
            return 'vendor-echarts'
          }
          if (id.includes('xlsx')) {
            return 'vendor-xlsx'
          }
        },
      },
    },
    assetsDir: 'static/assets',
  },
})
