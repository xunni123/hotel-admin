# Hotel Server 部署指南

## 1. 环境要求

- JDK 17+
- MySQL 8.0+
- Redis 6.0+
- Linux 服务器 (CentOS 7+ / Ubuntu 18.04+)

## 2. 打包项目

```bash
cd hotel-server
mvn clean package -DskipTests
```

打包完成后，会在 `hotel-web/target/` 目录下生成 `hotel-web-1.0-SNAPSHOT.jar`

## 3. 服务器配置

### 3.1 创建目录

```bash
sudo mkdir -p /opt/hotel
sudo mkdir -p /var/log/hotel
sudo chmod 755 /opt/hotel
sudo chmod 755 /var/log/hotel
```

### 3.2 上传文件

将以下文件上传到 `/opt/hotel/` 目录：
- `hotel-web-1.0-SNAPSHOT.jar`
- `deploy.sh`

```bash
sudo chmod +x /opt/hotel/deploy.sh
```

### 3.3 修改配置文件

编辑 `/opt/hotel/` 目录下的配置文件，或在 JAR 包同目录创建 `application-prod.yml`：

```bash
cp hotel-web/src/main/resources/application-prod.yml /opt/hotel/
```

修改数据库和 Redis 密码：
```yaml
# 修改以下配置
spring:
  datasource:
    password: your_actual_db_password
  data:
    redis:
      password: your_actual_redis_password
```

## 4. 启动服务

### 使用部署脚本

```bash
# 启动
./deploy.sh start

# 查看状态
./deploy.sh status

# 查看日志
./deploy.sh log

# 停止
./deploy.sh stop

# 重启
./deploy.sh restart
```

### 手动启动

```bash
cd /opt/hotel
java -jar hotel-web-1.0-SNAPSHOT.jar --spring.profiles.active=prod
```

### 后台运行

```bash
cd /opt/hotel
nohup java -jar hotel-web-1.0-SNAPSHOT.jar --spring.profiles.active=prod > /var/log/hotel/hotel-application.log 2>&1 &
```

## 5. 日志文件

日志文件位于 `/var/log/hotel/` 目录：

| 文件 | 说明 |
|------|------|
| `hotel-application.log` | 应用运行日志 |
| `hotel-application-error.log` | 错误日志 |

### 日志滚动策略

- 单个日志文件最大 100MB
- 保留最近 30 天日志
- 总日志大小上限 3GB

## 6. 配置 Systemd 服务（可选）

创建服务文件 `/etc/systemd/system/hotel.service`：

```ini
[Unit]
Description=Hotel Application
After=network.target mysql.service redis.service

[Service]
Type=simple
User=root
WorkingDirectory=/opt/hotel
ExecStart=/usr/bin/java -jar /opt/hotel/hotel-web-1.0-SNAPSHOT.jar --spring.profiles.active=prod
Restart=always
RestartSec=10

[Install]
WantedBy=multi-user.target
```

启用服务：

```bash
sudo systemctl daemon-reload
sudo systemctl enable hotel
sudo systemctl start hotel
sudo systemctl status hotel
```

## 7. 验证部署

```bash
# 检查进程
ps aux | grep hotel-web

# 检查端口
netstat -tlnp | grep 8080

# 测试接口
curl http://localhost:8080/api/xxx
```

## 8. Nginx 配置（前端代理）

```nginx
server {
    listen 80;
    server_name your-domain.com;

    location / {
        root /var/www/hotel-web;
        index index.html;
        try_files $uri $uri/ /index.html;
    }

    location /api/ {
        proxy_pass http://127.0.0.1:8080/;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
    }
}
```

## 9. 防火墙配置

```bash
# CentOS/RHEL
sudo firewall-cmd --permanent --add-port=80/tcp
sudo firewall-cmd --permanent --add-port=8080/tcp
sudo firewall-cmd --reload

# Ubuntu
sudo ufw allow 80/tcp
sudo ufw allow 8080/tcp
```
