# Hotel Server API 接口文档

## 概述

本文档描述了酒店管理系统后端的所有RESTful API接口。

**基础URL**: `http://localhost:8081`

**通用响应格式**:

```json
{
  "code": 200,
  "message": "success",
  "data": {},
  "total": 0
}
```

- `code`: 响应状态码，200表示成功，500表示错误
- `message`: 响应消息
- `data`: 响应数据
- `total`: 总数（用于分页接口）

---

## 认证模块 (Auth)

### 用户登录

**接口**: `POST /auth/login`

**请求参数**:

```json
{
  "username": "admin",
  "password": "123456"
}
```

**响应示例**:

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "userId": 1,
    "username": "admin",
    "token": "eyJhbGciOiJIUzI1NiJ9..."
  }
}
```

---

## 仪表盘模块 (Dashboard)

### 获取仪表盘统计数据

**接口**: `GET /dashboard/stats`

**响应示例**:

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "totalRooms": 100,
    "todayCheckIns": 15,
    "todayCheckOuts": 12,
    "emptyRooms": 45,
    "occupancyRate": 55.0,
    "todayRevenue": 25000.00
  }
}
```

### 获取入住率统计

**接口**: `GET /dashboard/occupancy`

**响应示例**:

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "week": [
      { "date": "2024-06-01", "occupancyRate": 60.0 },
      { "date": "2024-06-02", "occupancyRate": 65.0 }
    ],
    "month": [...],
    "halfYear": [...]
  }
}
```

### 获取区域分布统计

**接口**: `GET /dashboard/region-distribution`

**响应示例**:

```json
{
  "code": 200,
  "message": "success",
  "data": [
    { "region": "A栋", "count": 45 },
    { "region": "B栋", "count": 35 }
  ]
}
```

### 获取收入统计

**接口**: `GET /dashboard/revenue`

**响应示例**:

```json
{
  "code": 200,
  "message": "success",
  "data": [
    {
      "dates": ["2024-06-01", "2024-06-02"],
      "revenue": [25000.00, 28000.00],
      "revpar": [250.00, 280.00]
    }
  ]
}
```

---

## 房间模块 (Room)

### 获取房间列表（分页）

**接口**: `GET /room/list`

**请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| searchText | String | 否 | 搜索关键词（房间号/客人姓名/手机号） |
| building | String | 否 | 楼栋 |
| floor | String | 否 | 楼层 |
| roomType | String | 否 | 房间类型 |
| idle | Boolean | 否 | 是否空闲 |
| dirty | Boolean | 否 | 是否脏房 |
| repair | Boolean | 否 | 是否维修中 |
| booked | Boolean | 否 | 是否已预订 |
| checkedIn | Boolean | 否 | 是否已入住 |
| locked | Boolean | 否 | 是否锁定 |
| selfUse | Boolean | 否 | 是否自用 |
| todayCheckout | Boolean | 否 | 今日退房 |
| checkinType | String | 否 | 入住类型 |
| channel | String | 否 | 渠道 |
| page | Integer | 否 | 页码，默认1 |
| pageSize | Integer | 否 | 每页数量，默认10 |

**响应示例**:

```json
{
  "code": 200,
  "message": "success",
  "data": [
    {
      "roomId": 1,
      "roomNo": "101",
      "roomType": "标准间",
      "building": "A栋",
      "floor": "1楼",
      "status": "idle",
      "guestName": null,
      "checkinTime": null
    }
  ],
  "total": 100
}
```

### 获取所有房间

**接口**: `GET /room/all`

### 获取楼栋列表

**接口**: `GET /room/building`

### 获取楼层列表

**接口**: `GET /room/floor`

### 获取房间类型列表

**接口**: `GET /room/type`

### 获取房间状态列表

**接口**: `GET /room/status`

### 获取入住类型列表

**接口**: `GET /room/checkinType`

### 获取渠道列表

**接口**: `GET /room/channel`

### 获取空闲房间

**接口**: `GET /room/free`

### 获取已入住房间

**接口**: `GET /room/checkedIn`

### 更新入住信息

**接口**: `PUT /room/updateCheckin`

**请求参数**:

```json
{
  "roomId": 1,
  "guestName": "张三"
}
```

### 退房操作

**接口**: `PUT /room/checkout`

**请求参数**:

```json
{
  "roomId": 1,
  "orderId": 1001,
  "checkOutTime": "2024-06-05 12:00:00",
  "remark": "正常退房"
}
```

---

## 用户模块 (User)

### 获取用户列表

**接口**: `GET /user/list`

**请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| username | String | 否 | 用户名 |
| page | Integer | 否 | 页码，默认1 |
| pageSize | Integer | 否 | 每页数量，默认10 |

### 新增用户

**接口**: `POST /user/add`

**请求参数**:

```json
{
  "username": "zhangsan",
  "password": "123456",
  "email": "zhangsan@example.com",
  "phone": "13800138000",
  "roleId": 1,
  "status": "enabled"
}
```

### 更新用户

**接口**: `PUT /user/update/{id}`

### 删除用户

**接口**: `DELETE /user/delete/{id}`

---

## 角色模块 (Role)

### 获取角色列表

**接口**: `GET /role/list`

### 新增角色

**接口**: `POST /role/add`

**请求参数**:

```json
{
  "roleName": "前台",
  "description": "前台工作人员",
  "permissions": []
}
```

### 更新角色

**接口**: `PUT /role/update/{id}`

### 删除角色

**接口**: `DELETE /role/delete/{id}`

### 获取角色权限

**接口**: `GET /role/permissions/{id}`

---

## 菜单模块 (Menu)

### 获取菜单列表

**接口**: `GET /menu/list`

### 获取树形菜单

**接口**: `GET /menu/tree`

### 新增菜单

**接口**: `POST /menu/add`

### 更新菜单

**接口**: `PUT /menu/update/{id}`

### 删除菜单

**接口**: `DELETE /menu/delete/{id}`

---

## 会员模块 (Member)

### 获取会员列表

**接口**: `GET /member/list`

**请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| searchText | String | 否 | 搜索关键词（姓名/手机号） |
| page | Integer | 否 | 页码，默认1 |
| pageSize | Integer | 否 | 每页数量，默认10 |

### 新增会员

**接口**: `POST /member/add`

**请求参数**:

```json
{
  "memberNo": "VIP001",
  "memberName": "张三",
  "phone": "13800138000",
  "email": "zhangsan@example.com",
  "points": 0
}
```

### 更新会员

**接口**: `PUT /member/update/{id}`

### 删除会员

**接口**: `DELETE /member/delete/{id}`

---

## 会员消费模块 (MemberConsume)

### 获取消费记录列表（分页）

**接口**: `GET /member-consume/list`

**请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| keyword | String | 否 | 搜索关键词（会员号/姓名/手机号） |
| type | String | 否 | 消费类型（room/goods/other） |
| page | Integer | 否 | 页码，默认1 |
| limit | Integer | 否 | 每页数量，默认10 |

**响应示例**:

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "list": [
      {
        "consumeId": 1,
        "memberNo": "VIP001",
        "memberName": "张三",
        "phone": "13800138000",
        "type": "room",
        "amount": 200.00,
        "pointsChange": 20,
        "currentPoints": 120,
        "remark": "房费",
        "operator": "admin",
        "createTime": "2024-06-05 10:00:00"
      }
    ],
    "total": 50
  }
}
```

### 获取消费记录详情

**接口**: `GET /member-consume/{id}`

### 新增消费记录

**接口**: `POST /member-consume`

**请求参数**:

```json
{
  "memberId": 1,
  "memberNo": "VIP001",
  "memberName": "张三",
  "phone": "13800138000",
  "type": "room",
  "amount": 200.00,
  "pointsChange": 20,
  "currentPoints": 120,
  "remark": "房费",
  "operator": "admin"
}
```

### 更新消费记录

**接口**: `PUT /member-consume/{id}`

### 删除消费记录

**接口**: `DELETE /member-consume/{id}`

### 获取消费统计

**接口**: `GET /member-consume/statistics`

**响应示例**:

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "totalAmount": 50000.00,
    "totalCount": 100,
    "roomAmount": 30000.00,
    "goodsAmount": 20000.00
  }
}
```

---

## 订单模块 (Order)

### 获取订单列表

**接口**: `GET /order/list`

**请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| searchText | String | 否 | 搜索关键词（订单号/房间号/客人姓名） |
| page | Integer | 否 | 页码，默认1 |
| pageSize | Integer | 否 | 每页数量，默认10 |

### 获取订单详情

**接口**: `GET /order/{id}`

### 新增订单

**接口**: `POST /order/add`

### 更新订单

**接口**: `PUT /order/update/{id}`

### 删除订单

**接口**: `DELETE /order/delete/{id}`

---

## 预订模块 (Booking)

### 获取预订列表

**接口**: `GET /booking/list`

**请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| orderTel | String | 否 | 手机号 |
| page | Integer | 否 | 页码，默认1 |
| pageSize | Integer | 否 | 每页数量，默认10 |

### 新增预订

**接口**: `POST /booking/add`

### 更新预订

**接口**: `PUT /booking/update/{id}`

### 删除预订

**接口**: `DELETE /booking/delete/{id}`

---

## 商品库存模块 (Goods)

### 获取商品列表

**接口**: `GET /goods/list`

**请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| keyword | String | 否 | 搜索关键词（商品名/编码） |
| category | String | 否 | 商品分类 |
| page | Integer | 否 | 页码，默认1 |
| pageSize | Integer | 否 | 每页数量，默认10 |

### 新增商品

**接口**: `POST /goods/add`

### 更新商品

**接口**: `PUT /goods/update/{id}`

### 删除商品

**接口**: `DELETE /goods/delete/{id}`

---

## 财务管理模块 (FinancialRecord)

### 获取财务记录列表

**接口**: `GET /financial-record/list`

**请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| type | String | 否 | 类型（income/expense） |
| paymentMethod | String | 否 | 支付方式 |
| page | Integer | 否 | 页码，默认1 |
| pageSize | Integer | 否 | 每页数量，默认10 |

**响应示例**:

```json
{
  "code": 200,
  "message": "success",
  "data": [
    {
      "id": 1,
      "type": "income",
      "amount": 200.00,
      "paymentMethod": "wechat",
      "orderNo": "ORD001",
      "remark": "房费收入",
      "createTime": "2024-06-05 10:00:00"
    }
  ],
  "total": 50
}
```

### 新增财务记录

**接口**: `POST /financial-record/add`

### 更新财务记录

**接口**: `PUT /financial-record/update/{id}`

### 删除财务记录

**接口**: `DELETE /financial-record/delete/{id}`

---

## 清洁管理模块 (Cleaning)

### 获取清洁任务列表

**接口**: `GET /cleaning/list`

### 获取清洁工列表

**接口**: `GET /cleaning/cleaners`

### 新增清洁任务

**接口**: `POST /cleaning/task/add`

### 完成清洁任务

**接口**: `PUT /cleaning/task/complete/{id}`

---

## 公告管理模块 (Announcement)

### 获取公告列表

**接口**: `GET /announcement/list`

**请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| title | String | 否 | 公告标题 |
| page | Integer | 否 | 页码，默认1 |
| pageSize | Integer | 否 | 每页数量，默认10 |

### 新增公告

**接口**: `POST /announcement/add`

**请求参数**:

```json
{
  "title": "重要通知",
  "content": "请各位员工注意...",
  "publisher": "admin"
}
```

### 更新公告

**接口**: `PUT /announcement/update/{id}`

### 删除公告

**接口**: `DELETE /announcement/delete/{id}`

---

## 操作日志模块 (OperationLog)

### 获取操作日志列表

**接口**: `GET /operation-log/list`

**请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| keyword | String | 否 | 搜索关键词（操作人/操作内容） |
| startDate | String | 否 | 开始日期 |
| endDate | String | 否 | 结束日期 |
| module | String | 否 | 功能模块 |
| page | Integer | 否 | 页码，默认1 |
| limit | Integer | 否 | 每页数量，默认10 |

**响应示例**:

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "list": [
      {
        "id": 1,
        "operator": "admin",
        "module": "user",
        "operation": "新增用户",
        "detail": "新增用户：zhangsan",
        "createTime": "2024-06-05 10:00:00"
      }
    ],
    "total": 100
  }
}
```

---

## 错误码说明

| 错误码 | 说明 |
|--------|------|
| 200 | 成功 |
| 400 | 请求参数错误 |
| 401 | 未授权 |
| 404 | 资源不存在 |
| 500 | 服务器内部错误 |

---

## 注意事项

1. 所有分页接口的页码从1开始
2. 日期格式统一为 `yyyy-MM-dd` 或 `yyyy-MM-dd HH:mm:ss`
3. 金额字段使用BigDecimal类型，保留2位小数
4. 所有接口返回统一的Result格式
