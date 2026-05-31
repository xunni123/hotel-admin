// src/plugins/echarts.ts
import * as echarts from 'echarts/core'

// 1. 按需引入图表类型（只引入你需要的）
import { LineChart, BarChart, PieChart } from 'echarts/charts'

// 2. 按需引入组件（标题、提示框、图例、网格、数据集等）
import {
  TitleComponent,
  TooltipComponent,
  LegendComponent,
  GridComponent,
  DatasetComponent,
  ToolboxComponent,
  DataZoomComponent,
} from 'echarts/components'

// 3. 引入渲染器（Canvas 或 SVG，二选一即可）
import { CanvasRenderer } from 'echarts/renderers'

// 4. 引入特性（可选，如标签自动布局、全局过渡动画）
import { LabelLayout, UniversalTransition } from 'echarts/features'

// 注册所有按需引入的模块
echarts.use([
  // 图表类型
  LineChart,
  BarChart,
  PieChart,
  // 组件
  TitleComponent,
  TooltipComponent,
  LegendComponent,
  GridComponent,
  DatasetComponent,
  ToolboxComponent,
  DataZoomComponent,
  // 渲染器
  CanvasRenderer,
  // 特性
  LabelLayout,
  UniversalTransition,
])

// 导出已配置好的 echarts 实例（也可以直接导出，组件中 import 这个文件）
export default echarts
