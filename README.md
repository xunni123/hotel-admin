# hotel-admin
# 酒店后台管理系统

一套完整的前后端分离酒店管理后台：覆盖客房预订、入住退房、订单财务、会员消费、清洁任务、商品库存、系统权限管理、房态仪表盘等全流程业务。内置 JWT 鉴权、Spring Security、接口级权限、BCrypt 密码加密、操作日志审计、全局异常处理等安全防护。

---

## 目录

- [技术栈](#技术栈)
- [功能模块清单](#功能模块清单)
- [本地启动步骤](#本地启动步骤)
- [部署说明（可选）](#部署说明可选)
- [项目结构](#项目结构)

---



## 部分效果展示



#### 登录页：

![36cb1f84270ba718c5d13cf6d7943df2](C:\Users\26476\xwechat_files\wxid_awkhlo2ky25d22_dac9\temp\RWTemp\2026-07\36cb1f84270ba718c5d13cf6d7943df2.png)

#### 首页：

![e848a683b7efded1c6416526f08f96eb](C:\Users\26476\xwechat_files\wxid_awkhlo2ky25d22_dac9\temp\RWTemp\2026-07\e848a683b7efded1c6416526f08f96eb.png)

#### 清洁管理：

![image-20260703004250786](C:\Users\26476\AppData\Roaming\Typora\typora-user-images\image-20260703004250786.png)

#### 角色权限管理：

![image-20260703004311232](C:\Users\26476\AppData\Roaming\Typora\typora-user-images\image-20260703004311232.png)

#### 财务：

![image-20260703004326354](C:\Users\26476\AppData\Roaming\Typora\typora-user-images\image-20260703004326354.png)

#### 房态：

![image-20260703004346346](C:\Users\26476\AppData\Roaming\Typora\typora-user-images\image-20260703004346346.png)

## 技术栈

### 后端（Spring Boot + Maven 多模块）

| 类别 | 技术 | 版本 | 用途 |
|------|------|------|------|
| 核心 | Java | 17 | 编译与运行 |
|  | Spring Boot | 2.7.18 | 主框架 |
| ORM 与数据库 | MyBatis-Plus | 3.5.15 | ORM 与通用 CRUD |
|  | Druid | 1.2.20 | 数据库连接池与监控 |
|  | MySQL | 8.0.33 | 业务数据库 |
| 缓存 | Spring Data Redis | 2.7.18 | 缓存支持 |
|  | Spring Cache | 2.7.18 | 方法级缓存抽象 |
| 安全 | Spring Security | 5.7.11 | 权限拦截与 403 响应 |
|  | JJWT | 0.11.5 | JWT Token 生成与解析 |
|  | BCrypt Spring Crypto | 由 Spring Boot 提供 | 登录密码加密与比对 |
| 工具 | Hutool | 5.8.13 | 通用工具库 |
|  | Lombok | 1.18.30 | 注解代替样板代码，简化开发 |
| 增强 | Spring Validation | 2.7.18 | 参数校验注解支持 |
|  | Spring WebSocket | 2.7.18 | 实时消息推送，预留扩展 |
| 构建 | Maven | 3.6 及以上 | 多模块构建，父 hotel-server，子模块 hotel-common 与 hotel-web |

### 前端（Vue 3 + Vite + TypeScript）

| 类别 | 技术 | 版本 | 用途 |
|------|------|------|------|
| 核心 | Vue | 3.5.30 | 主框架，Composition API 与 script setup |
|  | TypeScript | 5.9.3 | 类型系统 |
|  | Vite | 8.0.1 | 构建与开发服务器，热更新 |
| 路由与状态 | Vue Router | 4.6.4 | 动态路由，菜单权限驱动 |
|  | Pinia | 3.0.4 | 状态管理 |
|  | pinia-plugin-persistedstate | 4.7.1 | Pinia 本地持久化 |
| UI 层 | Element Plus | 2.13.6 | UI 组件库，按需自动引入 |
|  | unplugin-auto-import | 21.0.0 | API 自动导入 |
|  | unplugin-vue-components | 31.0.0 | 组件自动注册 |
|  | sass-embedded | 1.98.0 | SCSS 样式预处理 |
| 网络与工具 | Axios | 1.13.6 | HTTP 请求，统一封装与拦截器 |
|  | dayjs | 1.11.20 | 日期格式化 |
|  | NProgress | 0.2.0 | 路由切换进度条 |
|  | screenfull | 6.0.2 | 顶栏全屏按钮 |
| 图表与导出 | ECharts | 6.0.0 | 仪表盘图表 |
|  | vue-echarts | 8.0.1 | ECharts Vue 组件封装 |
|  | xlsx | 0.18.5 | Excel 导出，财务与订单模块 |
| 构建优化 | vite-plugin-compression | 0.5.1 | 产物 gzip 压缩 |
|  | rollupOptions.manualChunks | Vite 内置 | 手动分包，Vue ElementPlus ECharts xlsx 分别独立 chunk |

---

## 功能模块清单

### 1. 业务模块

| 模块 | 子功能 | 前端页面 | 后端关键类 |
|------|--------|----------|------------|
| 仪表盘 Dashboard | 房态卡片、今日入住退房、营收柱线图、房型占比饼图、收入趋势、员工排期 | views/dashboard | DashboardController DashboardMapper |
| 房态面板 | 楼栋楼层房型状态过滤，房间状态色块展示，入住退房快捷操作 | views/roomstatus | RoomController RoomMapper |
| 预订管理 | 预订列表、新建预订、取消预订、预订详情、客人信息维护 | views/booking/list | BookingController BookingMapper |
| 入住列表 | 办理入住、客人信息录入、退房结算、押金计算、订单生成 | views/List | RoomController check in 与 check out 方法 |
| 订单管理 | 订单列表、订单详情、消费明细、支付方式管理 | views/order | OrderController OrderMapper |
| 会员管理 | 会员档案、会员列表、积分与余额、会员编辑 | views/member | MemberController MemberMapper |
| 会员消费 | 消费记录、商品挂账记账、消费查询 | views/memberConsume/consumeRecord | MemberConsumeController MemberConsumeMapper |
| 清洁管理 | 保洁员管理、清洁任务分配、任务完成状态 | views/room/cleaning | CleaningController CleaningTaskMapper CleanerMapper |
| 商品库存 | 商品增删改查、库存数量、单价、分类管理 | views/goodsInventory/goodsManage | GoodsController GoodsMapper |
| 财务历史订单 | 订单筛选、按日期与渠道查询、导出 Excel | views/finance/historyOrder | OrderController |
| 财务财务报表 | 按日期与支付方式营收图表，柱图与饼图、报表导出 | views/finance/financialReport | FinancialRecordController FinancialRecordMapper |
| 公告管理 | 新增公告、公告列表、编辑与删除、状态管理 | views/notice/create 与 views/notice/list | AnnouncementController AnnouncementMapper |

### 2. 系统权限模块

| 模块 | 子功能 | 前端页面 | 后端关键类 |
|------|--------|----------|------------|
| 用户管理 | 用户增删改查、启停用、分配角色、重置密码、修改头像 | views/system/user | UserController UserMapper |
| 角色管理 | 角色增删改查、配置菜单权限树形勾选、保存角色权限 | views/system/role | AddRoleController SelectRoleController UpdateRoleController DeleteRoleController RolePermissionController RoleMenuMapper |
| 菜单管理 | 菜单树形列表、菜单按钮目录增删改查、权限标识维护 | views/system/menu | MenuController MenuMapper |
| 操作日志 | 操作审计日志、按用户模块时间筛选 | views/system/operationLog | OperationLogController OperationLogMapper |
| 认证与登录 | 图形验证码、登录、登出、Token 刷新、403 无权限页 | views/login router/auth.ts | AuthController SecurityConfig CorsConfig |

### 3. 安全与防护，开箱即用

- JWT 无状态认证与 BCrypt 密码加密存储，见 JwtUtil 与 PasswordEncoderConfig
- Spring Security 全局 403 鉴权与接口级权限控制，见 SecurityConfig 与 PermissionService
- 全局 CORS 跨域配置，支持前后端分离开发，见 CorsConfig
- 操作日志审计，关键操作自动落库，见 OperationLog 相关模块
- 全局异常统一返回 Result 泛型结构，见 GlobalExceptionHandler 与 Result 类
- MyBatis-Plus 防 SQL 注入与分页插件支持
- Druid 连接池监控与防攻击过滤配置
- Redis 缓存支持，可配合业务层做热点数据缓存

---

## 本地启动步骤

### 环境要求

| 依赖 | 最低版本 | 检查命令 |
|------|----------|----------|
| JDK | 17 | java -version |
| Maven | 3.6 及以上 | mvn -v |
| Node.js | 18 及以上 | node -v |
| npm 或 pnpm | 任意稳定版 | npm -v |
| MySQL | 8.0 | mysql --version 或客户端执行 SELECT VERSION(); |
| Redis | 6.0 及以上，生产环境必需，本地开发可选 | redis-cli ping |

---

### 步骤 1：准备数据库

创建数据库：

```sql
-- 使用你喜欢的 MySQL 客户端执行
CREATE DATABASE hotel_management DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
```

建表与初始化数据说明：

- 项目根目录的 database/ 文件夹中提供了完整的建表与初始化 SQL 脚本，按顺序执行即可。主要脚本包括：
  - hotel_management.sql：核心业务表
  - init_additional_tables.sql：补充表结构
  - orders.sql：订单表
  - member_consume.sql：会员消费表
  - init_goods.sql：商品初始化
  - init_announcements.sql：公告初始化
  - init_new_menus.sql：菜单与权限初始化
  - cleaning_tables.sql：清洁相关表
  - financial_record.sql：财务记录表

- 实体类位于 hotel-server/hotel-common/src/main/java/com/xunni/hotel/entity/，可作为表结构参考。

- 字典表包括 building、floor、room_type、room_status、check_in_type、channel，可参考 hotel-server/sql/check_and_query.sql 中的查询字段维护。

- 用户表 users 的 password 字段必须存储 BCrypt 哈希，可通过以下 Java 代码片段生成：
  ```java
  new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder().encode("你的明文密码");
  ```

---

### 步骤 2：启动后端（Spring Boot）

#### 2.1 填写本地配置

进入后端模块的配置目录，按实际环境修改配置文件：

```
hotel-server/hotel-web/src/main/resources/
  +-- application.yml              主配置，JWT 密钥、Jackson 等
  +-- application-active.yml       本地开发环境配置，默认推荐使用
  +-- application-test.yml         测试环境配置
  +-- application-prod.yml         生产环境配置
```

说明：

- 本地开发推荐使用 active 环境，Druid 连接池参数与 MySQL 连接信息在 application-active.yml 中维护。
- 请根据本地实际情况修改数据库的 username、password、url。
- 若本地未启动 Redis，可在 spring.autoconfigure.exclude 中关闭 Redis 自动配置，或保持默认。
- 请将 application.yml 中的 JWT 密钥 jwt.secret 修改为自己的高强度随机值。

#### 2.2 编译并启动

方式 A：命令行（推荐）

```bash
cd hotel-server

# 第一步，把公共模块 hotel-common 安装到本地 Maven 仓库，首次构建必需
mvn install -pl hotel-common -am -DskipTests

# 第二步，启动业务模块，激活 active profile
mvn spring-boot:run -pl hotel-web -Dspring-boot.run.profiles=active
```

方式 B：使用 IDEA 或 Eclipse

- 在 IDE 中导入根目录 hotel-server/pom.xml 作为 Maven 项目
- 运行启动类 HotelApplication.java，位置见 hotel-server/hotel-web/src/main/java/com/xunni/hotel/web/HotelApplication.java
- Run Configuration 的 VM options 中追加：
  ```
  -Dspring.profiles.active=active
  ```

后端启动成功后默认监听 http://localhost:8080 ，未登录直接访问接口会被 Spring Security 拦截，返回登录重定向或 401/403 响应。

---

### 步骤 3：启动前端（Vue 3 + Vite）

```bash
cd hotel-web

# 安装依赖
npm install
# 或者使用 pnpm install，通常速度更快
```

开发环境关键配置，已在项目中预设：

- 前端端口 3000，见 vite.config.ts server.port
- 代理规则 /api/** 转发到 http://localhost:8080，见 vite.config.ts server.proxy
- 环境变量文件 .env.development 与 .env.production

启动开发服务器：

```bash
npm run dev
```

浏览器会自动打开 http://localhost:3000 。

---

### 步骤 4：登录使用

1. 打开 http://localhost:3000，未登录时自动跳转登录页。
2. 使用 users 表中的账号登录；若 users 表为空，请先按步骤 1 中说明使用 BCrypt 生成密码哈希后插入。
3. 登录成功后，系统会根据该用户绑定的角色与菜单，动态生成左侧菜单与路由。

---

## 部署说明（可选）

项目提供了部署相关脚本与配置，可根据需要参考使用：

- hotel-server/deploy.sh：部署脚本，可作为服务器部署参考。
- hotel-server/DEPLOY.md：部署文档，包含详细说明。
- 根目录 nginx-config/：Nginx 站点配置示例，包括反向代理 /api 到后端服务与 HTTPS 证书配置示例。
- 根目录 test/：测试环境的 Nginx 配置与 application-prod 示例，可参考。
- 根目录 config/application-prod.yml：生产环境配置样例，可根据实际情况修改。

生产部署建议的总体流程：

1. 使用 Maven 将 hotel-web 打包成 jar，命令示例：
   ```bash
   cd hotel-server
   mvn -pl hotel-web -am clean package -DskipTests
   ```
2. 使用 Vite 打包前端：
   ```bash
   cd hotel-web
   npm run build
   ```
3. 将前端 dist 目录部署到 Nginx，配置静态资源与反向代理到后端 jar 服务。
4. 后端以生产配置（application-prod.yml）启动 jar，建议使用 systemd 或 supervisor 守护进程。

---





##  试运行，求实战反馈

本项目已具备基础功能，但未经大规模生产验证。如果你有酒店或 PMS 行业经验，恳请你从以下角度帮我“过一遍”：

1. **操作流程**：从预订→入住→换房→离店→开票，是否存在断头路？
2. **异常场景**：网络中断后重提交，是否会生成重复订单？
3. **性能感受**：查询半年历史订单时，页面是否明显卡顿？

你的行业经验比我写 10 年代码都珍贵，望不吝赐教。

**所有有效意见（无论采纳与否），均会在鸣谢列表中署名致谢*
