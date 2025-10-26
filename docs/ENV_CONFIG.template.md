# 环境配置模板

> **说明**: 这是环境配置文档的模板，请复制为 `ENV_CONFIG.md` 并填写实际配置
>
> ```bash
> cp docs/ENV_CONFIG.template.md docs/ENV_CONFIG.md
> ```
>
> ⚠️ `ENV_CONFIG.md` 已在 `.gitignore` 中，不会被提交到Git

---

## 📋 配置清单

### 本地开发环境

#### PostgreSQL
```
Host: localhost
Port: 5432
Database: iot_vehicle
Username: postgres
Password: [填写密码]
```

#### Redis
```
Host: localhost
Port: 6379
Password: [填写密码或留空]
```

#### EMQ X MQTT
```
MQTT: tcp://localhost:1883
Dashboard: http://localhost:18083
Username: [填写用户名]
Password: [填写密码]
```

---

### 测试环境

```
待部署后填写
```

---

### 生产环境

```
待部署后填写
```

---

### 第三方服务

#### 邮件服务
```
SMTP Host: 
SMTP Port: 
Username: 
Password: 
```

#### 短信服务
```
AccessKey ID: 
AccessKey Secret: 
```

#### 对象存储
```
Endpoint: 
AccessKey: 
SecretKey: 
Bucket: 
```

#### 地图服务
```
API Key: 
```

---

## 📝 使用说明

1. 复制此模板创建实际配置文件
2. 填写所有必要的连接信息和密码
3. `ENV_CONFIG.md` 不会被提交到Git，请妥善保管
4. 定期更新配置信息
5. 生产环境密码至少每3个月更换一次

---

**维护者**: dongxiang.wu  
**创建时间**: 2025-10-26

