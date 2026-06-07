# Hotel Server Docker 原生命令部署指南

## 概述

本文档描述了如何使用原生 Docker 命令（而非 Docker Compose）部署酒店管理系统。

---

## 1. 环境要求

| 软件 | 版本 | 说明 |
|------|------|------|
| Docker | 20.10+ | 容器运行时 |

### 验证 Docker 安装

```bash
docker --version
```

---

## 2. 部署步骤

### 2.1 创建网络

```bash
# 创建自定义网络，用于容器间通信
docker network create hotel-network
```

### 2.2 创建数据目录

```bash
# 创建数据持久化目录
mkdir -p /opt/hotel/mysql/data
mkdir -p /opt/hotel/redis/data
mkdir -p /opt/hotel/logs
mkdir -p /opt/hotel/nginx/html
mkdir -p /opt/hotel/nginx/conf.d
mkdir -p /opt/hotel/nginx/certs
```

### 2.3 部署 MySQL

```bash
# 拉取镜像
docker pull mysql:8.0

# 启动 MySQL 容器
docker run -d \
  --name hotel-mysql \
  --restart always \
  --network hotel-network \
  -p 3306:3306 \
  -e MYSQL_ROOT_PASSWORD="hotel@123" \
  -e MYSQL_DATABASE="hotel_db" \
  -e MYSQL_USER="hotel_user" \
  -e MYSQL_PASSWORD="hotel@123" \
  -e TZ="Asia/Shanghai" \
  -v /opt/hotel/mysql/data:/var/lib/mysql \
  mysql:8.0 \
  --character-set-server=utf8mb4 \
  --collation-server=utf8mb4_unicode_ci
```

**验证 MySQL**:

```bash
# 查看容器状态
docker ps | grep hotel-mysql

# 查看日志
docker logs hotel-mysql

# 进入 MySQL
docker exec -it hotel-mysql mysql -u root -p"hotel@123"
```

### 2.4 部署 Redis

```bash
# 拉取镜像
docker pull redis:6.2

# 启动 Redis 容器
docker run -d \
  --name hotel-redis \
  --restart always \
  --network hotel-network \
  -p 6379:6379 \
  -v /opt/hotel/redis/data:/data \
  redis:6.2 \
  redis-server --requirepass "hotel@123" --appendonly yes
```

**验证 Redis**:
```bash
# 查看容器状态
docker ps | grep hotel-redis

# 测试连接
docker exec -it hotel-redis redis-cli -a "hotel@123" ping
```

### 2.5 部署后端应用

#### 方式一：使用 Dockerfile 构建

```bash
# 进入项目目录
cd /opt/hotel

# 构建 Docker 镜像
docker build -t hotel-app:latest .

# 启动应用容器
docker run -d \
  --name hotel-app \
  --restart always \
  --network hotel-network \
  -p 8081:8081 \
  -e SPRING_PROFILES_ACTIVE="prod" \
  -e SPRING_DATASOURCE_URL="jdbc:mysql://hotel-mysql:3306/hotel_management?useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai" \
  -e SPRING_DATASOURCE_USERNAME="xunni" \
  -e SPRING_DATASOURCE_PASSWORD="123456" \
  -e SPRING_DATA_REDIS_HOST="hotel-redis" \
  -e SPRING_DATA_REDIS_PORT="6379" \
  -e SPRING_DATA_REDIS_PASSWORD="hotel@123" \
  -v /opt/hotel/logs:/app/logs \
  hotel-app:latest
```

#### 方式二：直接运行 JAR（推荐）

```bash
# 上传 JAR 文件到服务器
# 假设 JAR 文件位于 /opt/hotel/hotel-web-1.0-SNAPSHOT.jar

# 使用 Java 基础镜像运行
docker run -d \
  --name hotel-app \
  --restart always \
  --network hotel-network \
  -p 8081:8081 \
  -e SPRING_PROFILES_ACTIVE="prod" \
  -e SPRING_DATASOURCE_URL="jdbc:mysql://hotel-mysql:3306/hotel_management?useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai" \
  -e SPRING_DATASOURCE_USERNAME=${USERNAME} \
  -e SPRING_DATASOURCE_PASSWORD=${PASSWORD} \
  -e SPRING_DATA_REDIS_HOST=${REDIS_HOST} \
  -e SPRING_DATA_REDIS_PORT="6379" \
  -e SPRING_DATA_REDIS_PASSWORD=${REDIS_PASSWORD} \
  -v /opt/hotel/hotel-web-1.0-SNAPSHOT.jar:/app/app.jar \
  -v /opt/hotel/logs:/app/logs \
  openjdk:17-jdk-slim \
  java -jar /app/app.jar --spring.profiles.active=prod
```

**验证后端应用**:
```bash
# 查看容器状态
docker ps | grep hotel-app

# 查看日志
docker logs -f hotel-app

# 测试接口
curl http://localhost:8081/api/auth/login
```

### 2.6 部署 Nginx

#### 创建 Nginx 配置文件

```bash
cat > /opt/hotel/nginx/conf.d/hotel.conf << 'EOF'
server {
    listen 80;
    server_name localhost;

    # 前端静态文件
    location / {
        root /usr/share/nginx/html;
        index index.html;
        try_files $uri $uri/ /index.html;
    }

    # 后端API代理
    location /api/ {
        proxy_pass http://hotel-app:8081/;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_connect_timeout 60s;
        proxy_read_timeout 60s;
    }

    # 健康检查
    location /health {
        proxy_pass http://hotel-app:8081/health;
        access_log off;
    }
}
EOF
```

#### 启动 Nginx 容器

```bash
# 拉取镜像
docker pull nginx:latest

# 启动 Nginx 容器
docker run -d \
  --name hotel-nginx \
  --restart always \
  --network hotel-network \
  -p 80:80 \
  -p 443:443 \
  -v /opt/hotel/nginx/html:/usr/share/nginx/html \
  -v /opt/hotel/nginx/conf.d:/etc/nginx/conf.d \
  -v /opt/hotel/nginx/certs:/etc/nginx/certs \
  nginx:latest
```

**验证 Nginx**:
```bash
# 查看容器状态
docker ps | grep hotel-nginx

# 测试访问
curl http://localhost/
```

---

## 3. 管理脚本

创建管理脚本 `/opt/hotel/hotel.sh`:

```bash
#!/bin/bash

# 酒店系统管理脚本

case "$1" in
    start)
        echo "启动所有服务..."
        docker start hotel-mysql
        docker start hotel-redis
        docker start hotel-app
        docker start hotel-nginx
        echo "所有服务已启动"
        ;;
    
    stop)
        echo "停止所有服务..."
        docker stop hotel-nginx
        docker stop hotel-app
        docker stop hotel-redis
        docker stop hotel-mysql
        echo "所有服务已停止"
        ;;
    
    restart)
        $0 stop
        sleep 3
        $0 start
        ;;
    
    status)
        echo "=== 服务状态 ==="
        docker ps -a --format "table {{.Names}}\t{{.Status}}\t{{.Ports}}" | grep hotel
        ;;
    
    logs)
        case "$2" in
            mysql)
                docker logs -f hotel-mysql
                ;;
            redis)
                docker logs -f hotel-redis
                ;;
            app)
                docker logs -f hotel-app
                ;;
            nginx)
                docker logs -f hotel-nginx
                ;;
            *)
                echo "用法: $0 logs [mysql|redis|app|nginx]"
                ;;
        esac
        ;;
    
    ps)
        docker ps --format "table {{.Names}}\t{{.Status}}\t{{.Ports}}" | grep hotel
        ;;
    
    *)
        echo "用法: $0 {start|stop|restart|status|logs|ps}"
        echo ""
        echo "命令说明:"
        echo "  start   - 启动所有服务"
        echo "  stop    - 停止所有服务"
        echo "  restart - 重启所有服务"
        echo "  status  - 查看服务状态"
        echo "  logs    - 查看日志 (logs mysql/redis/app/nginx)"
        echo "  ps      - 查看容器列表"
        ;;
esac
```

**使用管理脚本**:
```bash
# 添加执行权限
chmod +x /opt/hotel/hotel.sh

# 启动所有服务
./hotel.sh start

# 查看状态
./hotel.sh status

# 查看日志
./hotel.sh logs app

# 停止所有服务
./hotel.sh stop
```

---

## 4. 一键部署脚本

创建一键部署脚本 `/opt/hotel/deploy.sh`:

```bash
#!/bin/bash

# 一键部署脚本

set -e

echo "=========================================="
echo "  酒店管理系统一键部署脚本"
echo "=========================================="

# 配置变量
MYSQL_ROOT_PASSWORD="hotel@123"
MYSQL_DATABASE="hotel_db"
MYSQL_USER="hotel_user"
MYSQL_PASSWORD="hotel@123"
REDIS_PASSWORD="hotel@123"

# 创建目录
echo "[1/6] 创建目录..."
mkdir -p /opt/hotel/mysql/data
mkdir -p /opt/hotel/redis/data
mkdir -p /opt/hotel/logs
mkdir -p /opt/hotel/nginx/html
mkdir -p /opt/hotel/nginx/conf.d
mkdir -p /opt/hotel/nginx/certs

# 创建网络
echo "[2/6] 创建网络..."
docker network create hotel-network 2>/dev/null || echo "网络已存在"

# 部署 MySQL
echo "[3/6] 部署 MySQL..."
if [ "$(docker ps -aq -f name=hotel-mysql)" ]; then
    echo "MySQL 容器已存在，跳过..."
else
    docker run -d \
      --name hotel-mysql \
      --restart always \
      --network hotel-network \
      -p 3306:3306 \
      -e MYSQL_ROOT_PASSWORD="${MYSQL_ROOT_PASSWORD}" \
      -e MYSQL_DATABASE="${MYSQL_DATABASE}" \
      -e MYSQL_USER="${MYSQL_USER}" \
      -e MYSQL_PASSWORD="${MYSQL_PASSWORD}" \
      -e TZ="Asia/Shanghai" \
      -v /opt/hotel/mysql/data:/var/lib/mysql \
      mysql:8.0 \
      --character-set-server=utf8mb4 \
      --collation-server=utf8mb4_unicode_ci
    
    echo "等待 MySQL 启动..."
    sleep 30
fi

# 部署 Redis
echo "[4/6] 部署 Redis..."
if [ "$(docker ps -aq -f name=hotel-redis)" ]; then
    echo "Redis 容器已存在，跳过..."
else
    docker run -d \
      --name hotel-redis \
      --restart always \
      --network hotel-network \
      -p 6379:6379 \
      -v /opt/hotel/redis/data:/data \
      redis:6.2 \
      redis-server --requirepass "${REDIS_PASSWORD}" --appendonly yes
    
    sleep 5
fi

# 部署后端应用
echo "[5/6] 部署后端应用..."
if [ "$(docker ps -aq -f name=hotel-app)" ]; then
    echo "后端应用容器已存在，跳过..."
else
    # 检查 JAR 文件是否存在
    if [ ! -f "/opt/hotel/hotel-web-1.0-SNAPSHOT.jar" ]; then
        echo "错误: JAR 文件不存在，请先上传 hotel-web-1.0-SNAPSHOT.jar 到 /opt/hotel/"
        exit 1
    fi
    
    docker run -d \
      --name hotel-app \
      --restart always \
      --network hotel-network \
      -p 8081:8081 \
      -e SPRING_PROFILES_ACTIVE="prod" \
      -e SPRING_DATASOURCE_URL="jdbc:mysql://hotel-mysql:3306/${MYSQL_DATABASE}?useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai" \
      -e SPRING_DATASOURCE_USERNAME="${MYSQL_USER}" \
      -e SPRING_DATASOURCE_PASSWORD="${MYSQL_PASSWORD}" \
      -e SPRING_DATA_REDIS_HOST="hotel-redis" \
      -e SPRING_DATA_REDIS_PORT="6379" \
      -e SPRING_DATA_REDIS_PASSWORD="${REDIS_PASSWORD}" \
      -v /opt/hotel/hotel-web-1.0-SNAPSHOT.jar:/app/app.jar \
      -v /opt/hotel/logs:/app/logs \
      openjdk:17-jdk-slim \
      java -jar /app/app.jar --spring.profiles.active=prod
    
    echo "等待应用启动..."
    sleep 20
fi

# 部署 Nginx
echo "[6/6] 部署 Nginx..."
if [ "$(docker ps -aq -f name=hotel-nginx)" ]; then
    echo "Nginx 容器已存在，跳过..."
else
    # 创建 Nginx 配置
    cat > /opt/hotel/nginx/conf.d/hotel.conf << 'EOF'
server {
    listen 80;
    server_name localhost;

    location / {
        root /usr/share/nginx/html;
        index index.html;
        try_files $uri $uri/ /index.html;
    }

    location /api/ {
        proxy_pass http://hotel-app:8081/;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
    }
}
EOF
    
    docker run -d \
      --name hotel-nginx \
      --restart always \
      --network hotel-network \
      -p 80:80 \
      -p 443:443 \
      -v /opt/hotel/nginx/html:/usr/share/nginx/html \
      -v /opt/hotel/nginx/conf.d:/etc/nginx/conf.d \
      -v /opt/hotel/nginx/certs:/etc/nginx/certs \
      nginx:latest
fi

echo ""
echo "=========================================="
echo "  部署完成！"
echo "=========================================="
echo ""
echo "服务地址:"
echo "  前端: http://localhost"
echo "  后端: http://localhost:8081"
echo ""
echo "查看状态: docker ps | grep hotel"
echo "查看日志: docker logs -f hotel-app"
echo ""
```

---

## 5. 常用命令

### 5.1 容器管理

```bash
# 查看所有容器
docker ps -a | grep hotel

# 启动单个容器
docker start hotel-mysql
docker start hotel-redis
docker start hotel-app
docker start hotel-nginx

# 停止单个容器
docker stop hotel-nginx
docker stop hotel-app
docker stop hotel-redis
docker stop hotel-mysql

# 重启单个容器
docker restart hotel-app

# 删除容器（需先停止）
docker stop hotel-app
docker rm hotel-app
```

### 5.2 查看日志

```bash
# 查看实时日志
docker logs -f hotel-app

# 查看最近100行日志
docker logs --tail 100 hotel-app

# 查看指定时间段日志
docker logs --since "2024-01-01" hotel-app
```

### 5.3 进入容器

```bash
# 进入 MySQL
docker exec -it hotel-mysql bash

# 进入 Redis
docker exec -it hotel-redis bash

# 进入应用容器
docker exec -it hotel-app bash

# 进入 Nginx
docker exec -it hotel-nginx bash
```

### 5.4 数据备份

```bash
# 备份 MySQL 数据
docker exec hotel-mysql mysqldump -u root -p"hotel@123" hotel_db > backup.sql

# 备份 Redis 数据
docker exec hotel-redis redis-cli -a "hotel@123" BGSAVE
cp /opt/hotel/redis/data/dump.rdb ./redis_backup.rdb
```

---

## 6. 故障排查

### 6.1 容器无法启动

```bash
# 查看容器日志
docker logs hotel-app

# 查看容器详细信息
docker inspect hotel-app

# 检查网络连接
docker network inspect hotel-network
```

### 6.2 网络问题

```bash
# 检查容器是否在同一网络
docker network inspect hotel-network

# 测试容器间连通性
docker exec hotel-app ping hotel-mysql
docker exec hotel-app ping hotel-redis
```

### 6.3 端口冲突

```bash
# 查看端口占用
netstat -tlnp | grep 3306
netstat -tlnp | grep 6379
netstat -tlnp | grep 8081
netstat -tlnp | grep 80

# 停止占用端口的进程
kill -9 <PID>
```

---

## 7. 升级部署

### 7.1 升级后端应用

```bash
# 停止旧容器
docker stop hotel-app
docker rm hotel-app

# 上传新的 JAR 文件
# 使用 scp 或其他方式上传

# 启动新容器
docker run -d \
  --name hotel-app \
  --restart always \
  --network hotel-network \
  -p 8081:8081 \
  -e SPRING_PROFILES_ACTIVE="prod" \
  -e SPRING_DATASOURCE_URL="jdbc:mysql://hotel-mysql:3306/hotel_db?useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai" \
  -e SPRING_DATASOURCE_USERNAME="hotel_user" \
  -e SPRING_DATASOURCE_PASSWORD="hotel@123" \
  -e SPRING_DATA_REDIS_HOST="hotel-redis" \
  -e SPRING_DATA_REDIS_PORT="6379" \
  -e SPRING_DATA_REDIS_PASSWORD="hotel@123" \
  -v /opt/hotel/hotel-web-1.0-SNAPSHOT.jar:/app/app.jar \
  -v /opt/hotel/logs:/app/logs \
  openjdk:17-jdk-slim \
  java -jar /app/app.jar --spring.profiles.active=prod
```

### 7.2 升级前端

```bash
# 上传新的前端文件到 /opt/hotel/nginx/html/
# Nginx 会自动加载新文件
```

---

## 8. 清理卸载

```bash
# 停止所有容器
docker stop hotel-nginx hotel-app hotel-redis hotel-mysql

# 删除所有容器
docker rm hotel-nginx hotel-app hotel-redis hotel-mysql

# 删除网络
docker network rm hotel-network

# 删除数据目录（谨慎操作！）
rm -rf /opt/hotel
```

---

## 附录：端口说明

| 端口 | 服务 | 说明 |
|------|------|------|
| 3306 | MySQL | 数据库服务 |
| 6379 | Redis | 缓存服务 |
| 8081 | 后端应用 | API 服务 |
| 80 | Nginx | HTTP 前端访问 |
| 443 | Nginx | HTTPS 前端访问 |
