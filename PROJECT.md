# hotel-xunni 酒店管理系统

## 项目概述

酒店后台管理系统，支持预订、入住、订单、会员、客房、财务、公告、商品库存等管理功能，基于 RBAC 角色权限模型。

---

## 技术栈

### 后端
| 技术 | 版本 |
|------|------|
| Java | 17 |
| Spring Boot | 2.7.18 |
| MyBatis-Plus | 3.5.15 |
| MySQL | 8.0 |
| Redis | 6.0+ |
| Druid | 1.2.20 |
| JWT (jjwt) | 0.11.5 |
| Hutool | 5.8.13 |

### 前端
| 技术 | 版本 |
|------|------|
| Vue 3 | 3.5.30 |
| TypeScript | 5.9 |
| Vite | 8.0 |
| Pinia | 3.0 |
| Element Plus | 2.13 |
| ECharts | 6.0 |

---

## 项目结构

```
hotel-xunni/
├── hotel-server/                  # 后端 (Maven 多模块)
│   ├── hotel-common/              # 公共模块 (实体类、工具类)
│   ├── hotel-web/                 # Web 模块 (控制器、服务、映射器)
│   └── sql/                       # 查询 SQL
├── hotel-web/                     # 前端 (Vue 3 + Vite)
│   └── src/
│       ├── api/                   # API 接口
│       ├── components/            # 通用组件
│       ├── views/                 # 页面视图
│       ├── router/                # 路由
│       ├── store/                 # Pinia 状态
│       ├── services/              # Axios 封装
│       ├── utils/                 # 工具函数
│       └── layout/                # 布局组件
├── database/                      # 数据库 SQL 脚本
├── config/                        # 生产环境配置
├── nginx-config/                  # Nginx 配置
└── test/                          # 部署修复记录
```

---

## 数据库表 (22张)

| 表名 | 说明 |
|------|------|
| `users` | 用户账户 |
| `roles` | 角色定义 |
| `user_roles` | 用户-角色关联 |
| `menus` | 菜单/权限树 |
| `role_menus` | 角色-菜单权限关联 |
| `room` | 客房信息 |
| `room_type` | 房型字典 |
| `room_status` | 房间状态字典 |
| `building` | 楼栋字典 |
| `floor` | 楼层信息 |
| `orders` | 订单记录 |
| `channel` | 预订渠道字典 |
| `check_in_type` | 入住类型字典 |
| `members` | 会员信息 |
| `member_consume` | 会员消费记录 |
| `goods` | 商品库存 |
| `financial_record` | 财务收支记录 |
| `cleaning_tasks` | 清洁任务 |
| `cleaners` | 保洁人员 |
| `announcements` | 系统公告 |
| `operation_log` | 操作日志 |
| `numbers` | 辅助数字表 |

---

## API 端点

### 认证
- `POST /auth/login` — 登录

### 仪表盘
- `GET /dashboard/stats` — 统计概览
- `GET /dashboard/occupancy` — 入住率
- `GET /dashboard/region-distribution` — 客源分布
- `GET /dashboard/revenue` — 收入数据

### 预订管理
- `GET /booking/list` — 预订列表
- `POST /booking/add` — 新增预订
- `PUT /booking/update` — 更新预订

### 入住管理
- `GET /room/free` — 空闲房间
- `GET /room/checkedIn` — 已入住房间
- `PUT /room/updateCheckin` — 更新入住
- `PUT /room/checkout` — 退房

### 订单管理
- `GET /order/list` — 订单列表
- `POST /order/add` — 新增订单
- `PUT /order/update/{orderId}` — 更新订单

### 会员管理
- `GET /member/list` — 会员列表
- `POST /member/add` — 新增会员

### 客房管理
- `GET /room/list` — 客房列表
- `GET /room/building` — 楼栋
- `GET /room/floor` — 楼层
- `GET /room/type` — 房型
- `GET /room/status` — 房态

### 系统管理
- `GET /user/list` / `POST /user/update` / `DELETE /user/delete/{id}` — 用户管理
- `GET /role/list` / `POST /role/add` / `DELETE /role/delete/{id}` — 角色管理
- `GET /menu/tree/{userId}` — 菜单树
- `GET /menu/role/{roleId}/tree` / `POST /role/{roleId}/permissions` — 权限分配

### 其他
- 公告管理、清洁管理、财务管理、商品库存、会员消费、操作日志等

---

## 部署

### 环境要求
- JDK 17+、MySQL 8.0+、Redis 6.0+、Nginx

### 打包
```bash
cd hotel-server && mvn clean package -DskipTests
cd ../hotel-web && npm run build
```

### 部署架构
```
/www/wwwroot/
├── hotel-web/                    # 前端静态文件 (npm run build 产物)
└── hotel-server/                 # 后端
    ├── hotel-web-1.0-SNAPSHOT.jar
    └── config/
        └── application-prod.yml  # 外部配置文件
```

### 启动

1. 启动 MySQL 和 Redis
2. 导入数据库：`mysql -u root -p hotel_management < hotel_management.sql`
3. 启动后端：
   ```bash
   cd /www/wwwroot/hotel-server
   nohup java -jar hotel-web-1.0-SNAPSHOT.jar --spring.profiles.active=prod > /var/log/hotel/hotel-application.log 2>&1 &
   ```
4. 配置 Nginx 反向代理（`/api/` → `http://127.0.0.1:8080/`）

### 默认账户
- 用户名：`admin`，密码：`123456`

---

## 常见部署问题

详见 `test/deploy-fixes.md`

| 问题 | 原因 | 修复 |
|------|------|------|
| 403 Forbidden | Nginx 缺少 `/api/` 代理 | 添加反向代理配置 |
| 405/404 | `try_files` 在 server 级别拦截 | 移到 `location /` 内部 |
| 502 Bad Gateway | MySQL/Redis 未启动或后端挂了 | 启动数据库和后端 |
| 菜单为空 | `map-underscore-to-camel-case: true` | 改为 `false` |

### 关键配置项

Nginx 配置文件：
- `nginx-config/39.108.212.17.conf` — 站点配置
- `nginx-config/proxy-api.conf` — API 代理

后端外部配置文件：
- `config/application-prod.yml` — 生产环境（已修正驼峰映射问题）
