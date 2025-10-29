# API 测试指南

**作者**: dongxiang.wu  
**日期**: 2025-10-28

---

## 🚀 快速测试步骤

### 前置准备

1. **确保Docker服务运行**
   ```bash
   docker-compose ps
   # 确认 postgres、redis、emqx 都在运行
   ```

2. **初始化数据库**（首次使用需要）
   ```bash
   # 重新创建数据库（会执行所有初始化脚本）
   docker-compose down -v
   docker-compose up -d postgres
   
   # 等待30秒让初始化脚本执行完成
   sleep 30
   ```

3. **启动应用**
   ```bash
   cd iot-vehicle-web
   mvn spring-boot:run
   ```

---

## 📝 测试步骤

### 方式1：使用 Knife4j 文档测试（推荐）

1. **访问 Knife4j 文档**
   
   打开浏览器访问：http://localhost:8080/api/doc.html

2. **测试用户注册**
   
   找到 `认证管理` → `用户注册`
   
   请求体：
   ```json
   {
     "username": "testuser",
     "password": "123456",
     "nickname": "测试用户",
     "email": "test@example.com",
     "phone": "13800138000"
   }
   ```
   
   点击「调试」→「发送」
   
   **预期结果**：
   ```json
   {
     "code": 200,
     "message": "注册成功",
     "timestamp": 1730134567890
   }
   ```

3. **测试用户登录**
   
   找到 `认证管理` → `用户登录`
   
   请求体：
   ```json
   {
     "username": "admin",
     "password": "admin123"
   }
   ```
   
   点击「调试」→「发送」
   
   **预期结果**：
   ```json
   {
     "code": 200,
     "message": "操作成功",
     "data": {
       "accessToken": "eyJhbGciOiJIUzUxMiJ9...",
       "tokenType": "Bearer",
       "expiresIn": 604800,
       "userInfo": {
         "id": 1,
         "username": "admin",
         "nickname": "超级管理员"
       }
     }
   }
   ```

4. **复制 Token**
   
   复制返回的 `accessToken`

5. **配置全局 Token**
   
   在 Knife4j 页面右上角点击「文档管理」→「全局参数设置」
   
   添加参数：
   - 参数名：`Authorization`
   - 参数值：`Bearer <你的token>`
   - 参数类型：`header`

6. **测试需要登录的接口**
   
   找到 `用户管理` → `获取当前用户信息`
   
   点击「调试」→「发送」
   
   **预期结果**：返回当前登录用户的详细信息

---

### 方式2：使用 test-api.http 文件测试

在 IntelliJ IDEA 中打开 `test-api.http` 文件，逐个点击运行。

---

### 方式3：使用 curl 命令测试

#### 1. 用户注册

```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "password": "123456",
    "nickname": "测试用户",
    "email": "test@example.com",
    "phone": "13800138000"
  }'
```

#### 2. 用户登录

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin",
    "password": "admin123"
  }'
```

#### 3. 获取当前用户信息（需要Token）

```bash
# 替换 <your-token> 为登录返回的 accessToken
curl -X GET http://localhost:8080/api/user/current \
  -H "Authorization: Bearer <your-token>"
```

#### 4. 分页查询用户列表

```bash
curl -X GET "http://localhost:8080/api/user/page?current=1&size=10" \
  -H "Authorization: Bearer <your-token>"
```

---

## ✅ 预置测试账号

已在数据库中初始化：

| 用户名 | 密码 | 角色 | 权限 |
|--------|------|------|------|
| admin | admin123 | 超级管理员 | 所有权限 |

---

## 🔍 功能验证清单

测试完成后打勾：

- [ ] 健康检查接口正常
- [ ] Knife4j文档可访问
- [ ] 用户注册成功
- [ ] 用户登录成功（返回Token）
- [ ] Token能正常解析
- [ ] 获取当前用户信息成功
- [ ] 分页查询用户列表成功
- [ ] 未登录访问受保护接口返回401
- [ ] 修改密码功能正常

---

## 🐛 常见问题

### 1. 提示"用户名已存在"

**原因**：数据库中已有该用户

**解决**：换个用户名或清空数据库

### 2. 提示"Token无效或已过期"

**原因**：Token已过期（7天有效期）

**解决**：重新登录获取新Token

### 3. 提示"请先登录"

**原因**：未携带Token或Token格式错误

**解决**：检查 Authorization 请求头格式：`Bearer <token>`

---

## 📞 需要帮助？

如果测试遇到问题，记录：
1. 请求的接口路径
2. 请求参数
3. 完整的错误信息
4. 后台日志

---

**祝测试顺利！** 🎉

