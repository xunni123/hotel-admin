
import * as echarts from 'echarts/core'

// 1. 按需引入图表类型
import { LineChart, BarChart, PieChart } from 'echarts/charts'

// 2. 按需引入组件
import {
  TitleComponent,
  TooltipComponent,
  LegendComponent,
  GridComponent,
  DatasetComponent,
  ToolboxComponent,
  DataZoomComponent,
} from 'echarts/components'

// 3. 引入渲染器
import { CanvasRenderer } from 'echarts/renderers'

// 4. 引入特性（
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

// 导出已配置好的 echarts 实例（
export default echarts
