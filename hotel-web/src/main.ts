import { createApp } from 'vue'
import './style.css'
import App from './App.vue'

import { router } from '@/router/index'
import { createPinia } from 'pinia'
import piniaPluginPersistedstate from 'pinia-plugin-persistedstate'
import '@/assets/style/common.scss'

import 'element-plus/es/components/message-box/style/css'
import 'element-plus/es/components/message/style/css'
import 'element-plus/es/components/notification/style/css'

import '@/plugins/echart'
import VChart from 'vue-echarts'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
import { loadConfig } from '@/config/index'

async function initApp() {
  await loadConfig()
  
  const app = createApp(App)
  
  for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
    app.component(key, component)
  }
  
  app.use(router)
  
  const pinia = createPinia()
  pinia.use(piniaPluginPersistedstate)
  app.use(pinia)
  
  app.component('v-chart', VChart)
  
  app.mount('#app')
}

initApp()
