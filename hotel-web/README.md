# Hotel Web - 酒店管理系统前端

一个基于 Vue 3 + TypeScript + Element Plus 的现代化酒店管理系统前端项目。

## 技术栈

- **框架**: Vue 3.5.x
- **语言**: TypeScript
- **构建工具**: Vite 8.x
- **UI组件库**: Element Plus 2.13.x
- **状态管理**: Pinia 3.x
- **路由**: Vue Router 4.x
- **图表**: ECharts 6.x + vue-echarts
- **HTTP请求**: Axios 1.x
- **Excel导出**: xlsx 0.18.x
- **CSS预处理**: SCSS

## 功能模块

### 1. 仪表盘 (Dashboard)

- 首页数据统计卡片
- 收入趋势折线图
- 支付方式饼图
- 入住日程表
- 欢迎页

### 2. 房间管理

- 房间状态查看
- 楼层/楼栋筛选
- 房间状态更新
- 房间清洁管理

### 3. 入住管理

- 入住列表
- 入住登记
- 退房办理
- 换房操作

### 4. 预订管理

- 预订列表
- 新增预订
- 预订编辑
- 预订取消

### 5. 订单管理

- 订单列表
- 订单编辑
- 订单详情查看

### 6. 会员管理

- 会员列表
- 会员新增/编辑
- 会员消费记录
- 积分管理

### 7. 商品库存

- 商品列表
- 商品管理
- 库存查询

### 8. 财务管理

- 财务报表
- 收入/支出统计
- 导出Excel
- 历史订单查询

### 9. 公告管理

- 公告列表
- 新增公告
- 公告编辑

### 10. 系统管理

- 用户管理
- 角色管理
- 菜单管理
- 操作日志

## 项目结构

```
hotel-web/
├── public/                 # 静态资源
├── src/
│   ├── api/              # API接口定义
│   ├── assets/           # 静态资源
│   ├── components/       # 公共组件
│   ├── composables/      # 组合式函数
│   ├── config/           # 配置文件
│   ├── constants/        # 常量定义
│   ├── layout/           # 布局组件
│   ├── plugins/          # 插件
│   ├── router/           # 路由配置
│   ├── services/         # HTTP服务
│   ├── store/            # Pinia状态管理
│   ├── types/            # TypeScript类型定义
│   ├── utils/            # 工具函数
│   ├── views/            # 页面组件
│   ├── App.vue           # 根组件
│   └── main.ts           # 入口文件
├── .env.development      # 开发环境配置
├── .env.production       # 生产环境配置
├── index.html            # HTML入口
├── package.json          # 依赖配置
├── tsconfig.json         # TypeScript配置
└── vite.config.ts        # Vite配置
```

## 快速开始

### 环境要求

- Node.js >= 18.x
- npm >= 9.x

### 安装依赖

```bash
npm install
```

### 开发环境运行

```bash
npm run dev
```

### 生产环境构建

```bash
npm run build
```

### 预览构建结果

```bash
npm run preview
```

## 开发指南

### 新增页面路由

在 `src/router/index.ts` 中添加路由配置：

```typescript
{
  path: '/your-path',
  name: 'YourPage',
  component: () => import('@/views/your/Page.vue'),
  meta: { title: '页面标题' }
}
```

### 新增API接口

在 `src/api/` 对应模块中定义：

```typescript
import service from '@/services'

export const getList = (params?: any) => {
  return service.get('/your-api', { params })
}
```

### 使用表格组件

项目提供了 `useTable` composable 用于快速实现表格：

```typescript
const {
  loading,
  data,
  current,
  pageSize,
  total,
  handleCurrentChange,
  handleSizeChange,
  fetchList
} = useTable({
  fetchList: (params?: any) => getList(params)
})
```

### Excel导出功能

使用 `exportToExcel` 工具函数：

```typescript
import { exportToExcel } from '@/utils/export'

const columns = [
  { prop: 'id', label: 'ID' },
  { prop: 'name', label: '名称' }
]

exportToExcel(data, columns, '导出文件名')
```

## 后端API配置

后端服务地址配置在：

- **开发环境**: `.env.development`
- **生产环境**: `.env.production`

默认后端地址: `http://localhost:8081`

## 浏览器支持

- Chrome >= 88
- Firefox >= 78
- Safari >= 14
- Edge >= 88

## 许可证

MIT License
