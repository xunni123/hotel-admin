# 部署问题修复记录

## 问题 1：403 Forbidden（登录被拦截）

**原因**：Nginx 没有配置 `/api/` 反向代理，前端请求 `/api/auth/login` 到达 Nginx 后被当作静态文件处理，返回 403。

**修复**：
- 创建 Nginx 代理配置文件 `/www/server/panel/vhost/nginx/proxy/39.108.212.17/api.conf`
- 内容：
```nginx
location ^~ /api/ {
    proxy_pass http://127.0.0.1:8080/;
    proxy_set_header Host $host;
    proxy_set_header X-Real-IP $remote_addr;
    proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
}
```
- 删除宝塔自动生成的有 bug 的代理配置（双斜杠 `^~ /api//`）

---

## 问题 2：405 Not Allowed / 404 Not Found

**原因**：站点配置中 `try_files $uri $uri/ /index.html;` 写在 server 级别，拦截了所有请求（包括 `/api/`），导致代理配置不生效。

**修复**：将 `try_files` 从 server 级别移到 `location /` 块内部。
```nginx
location / {
    try_files $uri $uri/ /index.html;
}
```

---

## 问题 3：502 Bad Gateway（后端未启动/崩溃）

**原因**：MySQL 和 Redis 服务未启动，导致 Spring Boot 启动后无法连接数据库。

**修复**：
- 启动 MySQL：`/etc/init.d/mysqld start`
- 启动 Redis：`/etc/init.d/redis start`

---

## 问题 4：数据库表为空

**原因**：导入 SQL 时未指定数据库，SQL 文件没有 `USE hotel_management;` 语句。

**修复**：重新导入
```bash
mysql -u root -p hotel_management < /path/to/hotel_management.sql
```

---

## 问题 5：菜单为空（`/menu/tree/1` 返回空数组）

**原因**：`application-prod.yml` 中 `mybatis-plus.configuration.map-underscore-to-camel-case: true`，但 Menu 实体类字段名是下划线风格（`parent_id`、`menu_id`），不是驼峰。MyBatis 将数据库字段 `parent_id` 映射到 Java 的 `parentId`，但实体类没有这个字段，所有字段值为 null，`buildTree()` 过滤后返回空。

**修复**：将 `map-underscore-to-camel-case` 改为 `false`。

---

## 问题 6：外部配置文件加载失败

**原因**：Spring Boot 从 JAR 同级目录的 `config/` 子目录加载外部配置，但该目录不存在或配置格式错误。

**修复**：
- 在 JAR 目录下创建 `config/` 子目录
- 放入修正后的 `application-prod.yml`（`map-underscore-to-camel-case: false`）
- Spring Boot 会自动优先加载外部配置文件

---

## 最终部署架构

```
/www/wwwroot/
├── hotel-web/                    # 前端静态文件
│   └── index.html
└── hotel-server/                 # 后端
    ├── hotel-web-1.0-SNAPSHOT.jar
    └── config/
        └── application-prod.yml  # 外部配置（覆盖 JAR 内配置）
```

## 启动命令

```bash
# 启动 MySQL & Redis
/etc/init.d/mysqld start
/etc/init.d/redis start

# 启动后端
cd /www/wwwroot/hotel-server
nohup java -jar hotel-web-1.0-SNAPSHOT.jar --spring.profiles.active=prod > /var/log/hotel/hotel-application.log 2>&1 &
```
