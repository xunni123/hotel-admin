import { createApp } from 'vue'
import './style.css'
import App from './App.vue'

import { router } from '@/router/index'
import { createPinia } from 'pinia'
import piniaPluginPersistedstate from 'pinia-plugin-persistedstate'
import '@/assets/style/common.scss'

// echarts - pre-configured plugin registers chart types as side effect
import '@/plugins/echart'
import VChart from 'vue-echarts'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'

const app = createApp(App)
// Icons must be globally registered for dynamic menu icon rendering
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
  app.component(key, component)
}
app.use(router)
const pinia = createPinia()
pinia.use(piniaPluginPersistedstate)
app.use(pinia)
app.component('v-chart', VChart)
app.mount('#app')
