import { createApp } from 'vue'
import './style.css'
import App from './App.vue'

import { router } from '@/router/index'
import { createPinia } from 'pinia'
import piniaPluginPersistedstate from 'pinia-plugin-persistedstate'
import 'element-plus/theme-chalk/index.css'
import '@/assets/style/common.scss'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'

// echarts
import VChart from 'vue-echarts'
import { use, registerMap } from 'echarts/core'
import { CanvasRenderer } from 'echarts/renderers'
import { MapChart, PieChart } from 'echarts/charts'
import {
  TitleComponent,
  TooltipComponent,
  VisualMapComponent,
  LegendComponent,
} from 'echarts/components'

// 注册 ECharts 组件
use([
  CanvasRenderer,
  MapChart,
  PieChart,
  TitleComponent,
  TooltipComponent,
  VisualMapComponent,
  LegendComponent,
])

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
