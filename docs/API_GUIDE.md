# API 使用指南

**项目**: IoT Vehicle Platform  
**作者**: dongxiang.wu  
**更新时间**: 2025-10-27

---

## 📖 文档说明

本文档提供 IoT Vehicle Platform API 的使用指南。

完整的 API 文档请访问：**http://localhost:8080/api/doc.html** (Knife4j)

---

## 🔑 认证说明

### 1. Token 获取

所有需要认证的接口都需要在请求头中携带 JWT Token：

```http
Authorization: Bearer <your-token>
```

### 2. 获取 Token 的方式

**用户登录**:
```bash
POST /api/auth/login
Content-Type: application/json

{
  "username": "admin",
  "password": "admin123"
}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "tokenType": "Bearer",
    "expiresIn": 604800,
    "userInfo": {
      "id": 1,
      "username": "admin",
      "nickname": "超级管理员"
    }
  },
  "timestamp": 1730001234567
}
```

### 3. Token 使用

在后续请求中携带 Token：

```http
GET /api/user/current
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

---

## 📋 API 分类

### 1. 认证相关（无需登录）

| 接口 | 方法 | 路径 | 说明 |
|------|------|------|------|
| 用户注册 | POST | /api/auth/register | 注册新用户 |
| 用户登录 | POST | /api/auth/login | 登录获取Token |
| 刷新Token | POST | /api/auth/refresh | 刷新Token |

### 2. 用户管理（需要登录）

| 接口 | 方法 | 路径 | 权限 |
|------|------|------|------|
| 获取当前用户信息 | GET | /api/user/current | 无需权限 |
| 查询用户详情 | GET | /api/user/{userId} | system:user:view |
| 分页查询用户 | GET | /api/user/page | system:user:list |
| 更新用户信息 | PUT | /api/user/{userId} | system:user:edit |
| 删除用户 | DELETE | /api/user/{userId} | system:user:delete |
| 修改用户状态 | PUT | /api/user/{userId}/status | system:user:edit |
| 重置密码 | POST | /api/user/{userId}/reset-password | system:user:reset |
| 修改密码 | POST | /api/user/change-password | 无需权限 |

### 3. 角色管理（需要登录）

| 接口 | 方法 | 路径 | 权限 |
|------|------|------|------|
| 创建角色 | POST | /api/role | system:role:add |
| 更新角色 | PUT | /api/role/{roleId} | system:role:edit |
| 删除角色 | DELETE | /api/role/{roleId} | system:role:delete |
| 查询角色详情 | GET | /api/role/{roleId} | system:role:view |
| 查询所有角色 | GET | /api/role/all | 无需权限 |
| 分页查询角色 | GET | /api/role/page | system:role:list |
| 分配权限 | POST | /api/role/{roleId}/permissions | system:role:assign |
| 查询用户角色 | GET | /api/role/user/{userId} | system:role:view |
| 分配角色给用户 | POST | /api/role/user/{userId} | system:role:assign |

---

## 💡 使用示例

### 1. 用户注册

```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "password": "123456",
    "nickname": "测试用户",
    "email": "testuser@example.com",
    "phone": "13800138000"
  }'
```

### 2. 用户登录

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin",
    "password": "admin123"
  }'
```

### 3. 查询当前用户信息

```bash
curl -X GET http://localhost:8080/api/user/current \
  -H "Authorization: Bearer <your-token>"
```

### 4. 分页查询用户列表

```bash
curl -X GET "http://localhost:8080/api/user/page?current=1&size=10&username=test" \
  -H "Authorization: Bearer <your-token>"
```

### 5. 更新用户信息

```bash
curl -X PUT http://localhost:8080/api/user/1 \
  -H "Authorization: Bearer <your-token>" \
  -H "Content-Type: application/json" \
  -d '{
    "nickname": "新昵称",
    "email": "newemail@example.com"
  }'
```

---

## 🔒 权限说明

### 权限验证

部分接口需要特定权限才能访问，在接口上使用 `@RequirePermission` 注解标记。

**示例**:
```java
@RequirePermission("system:user:add")
public Result<Void> addUser() {
    // ...
}
```

### 权限编码规范

权限编码格式：`模块:资源:操作`

**示例**:
- `system:user:add` - 系统管理:用户:新增
- `system:user:edit` - 系统管理:用户:编辑
- `system:user:delete` - 系统管理:用户:删除
- `system:role:assign` - 系统管理:角色:分配权限

### 默认账号权限

**超级管理员**:
- 用户名: admin
- 密码: admin123
- 权限: 拥有所有权限

---

## 📊 响应格式

### 成功响应

```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    // 响应数据
  },
  "timestamp": 1730001234567
}
```

### 失败响应

```json
{
  "code": 400,
  "message": "操作失败的原因",
  "data": null,
  "timestamp": 1730001234567
}
```

### 分页响应

```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "records": [],
    "total": 100,
    "current": 1,
    "size": 10,
    "pages": 10
  },
  "timestamp": 1730001234567
}
```

---

## 🚨 错误码

| 错误码 | 说明 |
|--------|------|
| 200 | 成功 |
| 400 | 请求参数错误 |
| 401 | 未授权，请先登录 |
| 403 | 无权限访问 |
| 404 | 资源不存在 |
| 500 | 服务器内部错误 |

---

## 🔧 测试工具

### 1. Swagger UI 在线文档

访问: http://localhost:8080/api/swagger-ui.html

- 可以直接在网页上测试API
- 自动生成请求示例
- 支持Authorization配置
- API文档JSON: http://localhost:8080/api/v3/api-docs

### 2. HTTP 文件

项目根目录提供了 `test-api.http` 文件，在 IntelliJ IDEA 中可以直接运行。

### 3. Postman

导入 Postman Collection（待提供）

---

## 📝 注意事项

1. **Token 过期时间**: 7天（604800秒）
2. **密码加密**: 使用 BCrypt 算法
3. **逻辑删除**: 用户和角色使用逻辑删除，不会物理删除
4. **分页参数**: 默认页码=1，每页大小=10，最大100
5. **超级管理员**: ID为1的超级管理员不允许删除和禁用

---

## 🔗 相关链接

- [Springdoc OpenAPI 文档](https://springdoc.org/)
- [Swagger UI 文档](https://swagger.io/tools/swagger-ui/)
- [JWT 介绍](https://jwt.io/)
- [RESTful API 设计规范](https://restfulapi.net/)

---

**维护者**: dongxiang.wu  
**最后更新**: 2025-10-27

