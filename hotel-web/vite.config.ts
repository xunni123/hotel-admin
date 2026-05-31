import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import { resolve } from 'path'

// elment-plus
import AutoImport from 'unplugin-auto-import/vite'
import Components from 'unplugin-vue-components/vite'
import { ElementPlusResolver } from 'unplugin-vue-components/resolvers'

// https://vite.dev/config/
export default defineConfig({
  plugins: [
    vue(),
    AutoImport({
      resolvers: [ElementPlusResolver({ importStyle: 'css' })],
    }),
    Components({
      resolvers: [ElementPlusResolver({ importStyle: 'css' })],
    }),
  ],
  server: {
    host: '0.0.0.0',
    port: 3000,
    open: true,
    proxy: {
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true, //解决跨域
        rewrite: (path) => path.replace(/^\/api/, ''),  //去掉api前缀
      },
    },
  },
  resolve: {
    // 配置别名
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
        manualChunks: {
          'vendor-vue': ['vue', 'vue-router', 'pinia'],
          'vendor-element-plus': ['element-plus'],
          'vendor-echarts': ['echarts', 'vue-echarts'],
          'vendor-xlsx': ['xlsx'],
        },
      },
    },
  },
})
