# 项目开发总结

**项目名称**：IoT Vehicle Platform（车联网数据管理平台）  
**作者**：dongxiang.wu  
**更新时间**：2025-11-05

---

## 📊 项目概况

**开发进度**：6/52周（11.5%），Q1完成46.2%  
**实际用时**：3天  
**开发效率**：约10倍

**版本**：v1.0.0-beta  
**代码量**：~20,500行  
**Git提交**：20次规范提交

---

## 🏗️ 技术架构

### 核心技术栈

- **后端框架**：Spring Boot 3.1.5
- **数据库**：PostgreSQL 15（主库）+ Redis 7（缓存）
- **ORM框架**：MyBatis-Plus 3.5.7
- **实时通信**：MQTT（EMQ X 5.3.2 + Eclipse Paho）
- **认证授权**：JWT（JJWT 0.12.3）+ BCrypt密码加密
- **API文档**：Knife4j 4.5.0
- **部署**：Docker Compose

### 模块结构

```
iot-vehicle-platform/
├── iot-vehicle-common-core      # 核心工具（无框架依赖）
├── iot-vehicle-common-mybatis   # MyBatis工具（分页、JSONB处理）
├── iot-vehicle-common-redis     # Redis工具（预留）
├── iot-vehicle-common-web       # Web工具（用户上下文）
├── iot-vehicle-api              # 实体类、DTO、VO
├── iot-vehicle-service          # 业务逻辑、Mapper
└── iot-vehicle-web              # Controller、配置类
```

---

## 💼 业务模块

### 1. 用户权限模块（第2周）

**RBAC权限模型**：
- 5个表：sys_user、sys_role、sys_permission、sys_user_role、sys_role_permission
- JWT认证：Token生成、验证、刷新
- 权限验证：@RequirePermission、@RequireRole注解
- 21个REST API接口

**核心功能**：
- 用户注册、登录、登出
- 用户CRUD管理
- 角色管理
- 权限验证切面

**默认账号**：admin/123456（超级管理员）

---

### 2. 设备管理模块（第3-4周）

**数据模型**：
- tb_device（设备表）：31字段，支持JSONB扩展
- tb_device_group（分组表）：树形结构，实时统计
- tb_device_type（类型表）：6个预置类型
- tb_device_online_log（在线日志）：记录上下线历史

**核心功能**：
- 设备注册：自动生成设备ID（DEV+时间戳+随机码）和密钥
- 设备CRUD：查询、编辑、删除
- 设备分组：单个/批量修改分组
- 设备统计：总数、在线数、状态分布、在线率
- 10个REST API接口

**设备状态**：
- 设备状态：未激活/正常/维护中/已停用/已报废
- 在线状态：离线/在线

**架构亮点**：
- 支持数据权限（不存储冗余统计，实时计算）
- JSONB存储扩展数据（位置信息、元数据）

---

### 3. MQTT接入层（第5周）

**MQTT主题规范**：
```
设备上行：device/{deviceId}/{messageType}
  - device/{deviceId}/data      - 设备数据
  - device/{deviceId}/status    - 状态消息
  - device/{deviceId}/location  - 位置信息
  - device/{deviceId}/alarm     - 告警消息
  - device/{deviceId}/heartbeat - 心跳

设备下行：command/{deviceId}/{commandType}
系统广播：broadcast/{messageType}
```

**核心功能**：
- MQTT客户端自动连接和重连
- 设备认证：基于设备ID和密钥
- 上下线监听：自动更新设备状态
- 消息分发：根据主题分发到不同处理器

**测试验证**：
- ✅ Python测试客户端
- ✅ EMQ X Dashboard监控
- ✅ 设备上下线状态正确更新

---

### 4. 数据采集模块（第6周）

**数据模型**：
- tb_device_data：历史数据表（18条测试数据）
- tb_device_latest_data：最新数据表（实时更新）

**数据协议**（JSON）：
```json
{
  "timestamp": 毫秒时间戳,
  "gps": {
    "lat": 纬度,
    "lng": 经度,
    "speed": 速度,
    "direction": 方向,
    ...
  },
  "obd": {
    "rpm": 转速,
    "fuelLevel": 油量,
    "engineTemp": 温度,
    ...
  },
  "status": {
    "batteryVoltage": 电压,
    "signalStrength": 信号强度
  }
}
```

**三层数据存储**：
1. **tb_device_data** - 所有历史数据（用于分析）
2. **tb_device_latest_data** - 最新数据持久化（兜底）
3. **Redis缓存** - 最快查询（24小时TTL）

**数据处理流程**：
```
MQTT消息 → 数据验证 → 数据清洗 → 三层存储
```

**数据验证规则**：
- 经纬度范围：-90~90, -180~180
- 速度范围：0-300 km/h
- 转速范围：0-10000 RPM
- 油量范围：0-100%

---

## 🔑 关键技术解决方案

### 1. Spring Boot 3兼容性
**问题**：@MapperScan注解导致启动失败  
**解决**：降级Spring Boot 3.2.0 → 3.1.5，在配置类使用@MapperScan

### 2. Lombok注解处理
**问题**：@Slf4j、@Data等注解不生效  
**解决**：为所有模块添加maven-compiler-plugin的annotationProcessorPaths配置

### 3. PostgreSQL JSONB类型
**问题**：String无法直接写入JSONB字段  
**解决**：创建JsonbTypeHandler，使用PGobject转换

### 4. Redis Java 8时间类型
**问题**：LocalDateTime序列化失败  
**解决**：在RedisConfig中注册JavaTimeModule

### 5. 数据权限设计
**问题**：冗余统计字段不支持数据权限  
**解决**：移除冗余字段，Service层实时计算

---

## 📈 数据库设计

### 用户权限（5个表）
- sys_user、sys_role、sys_permission
- sys_user_role、sys_role_permission

### 设备管理（4个表）
- tb_device、tb_device_group
- tb_device_type、tb_device_online_log

### 数据采集（2个表）
- tb_device_data、tb_device_latest_data

**总计**：11个表

---

## 🔌 API接口

### 认证接口（4个）
- POST /auth/register
- POST /auth/login
- POST /auth/logout
- POST /auth/refresh

### 用户管理（8个）
- GET /user/current
- GET /user/{id}
- GET /user/page
- PUT /user/{id}
- DELETE /user/{id}
- PUT /user/{id}/status
- POST /user/{id}/reset-password
- POST /user/change-password

### 角色管理（9个）
- POST /role
- PUT /role/{id}
- DELETE /role/{id}
- GET /role/{id}
- GET /role/all
- GET /role/page
- POST /role/{id}/permissions
- GET /role/user/{userId}
- POST /role/user/{userId}

### 设备管理（10个）
- POST /device/register
- GET /device/page
- GET /device/{id}
- GET /device/by-device-id/{deviceId}
- PUT /device/{id}
- DELETE /device/{id}
- PUT /device/{id}/group
- DELETE /device/batch
- PUT /device/batch/group
- GET /device/statistics

**总计**：31个REST API

---

## 🧪 测试覆盖

**自动化测试**：45个测试用例（全部通过）

### 集成测试（32个）
- AuthIntegrationTest：9个
- UserIntegrationTest：9个
- RoleIntegrationTest：6个
- DeviceIntegrationTest：8个

### Service测试（5个）
- AuthServiceTest：5个

### 工具类测试（8个）
- JwtUtilTest、PasswordUtilTest、ResultTest

**手工测试**：
- ✅ Knife4j API文档测试
- ✅ MQTT消息收发测试
- ✅ 数据采集流程验证

**测试覆盖率**：约90%

---

## 🎯 待开发功能（Q1剩余）

### 第7周：设备数据模拟器
- GPS轨迹模拟
- OBD数据模拟
- 批量设备模拟

### 第8-10周：Vue3前端
- 设备管理页面
- 实时监控地图
- 用户权限管理

### 第11-12周：uni-app移动端
- GPS数据采集
- 设备绑定

### 第13周：发布v1.0.0
- Docker镜像
- 完善文档

---

## 📝 下次继续开发指南

### 环境启动

```bash
# 1. 启动Docker服务
docker-compose up -d

# 2. 启动应用
cd iot-vehicle-web
mvn spring-boot:run

# 3. 测试MQTT（可选）
python3 mqtt-test-client.py
```

### 访问地址
- API文档：http://localhost:8080/api/doc.html
- EMQ X Dashboard：http://localhost:18083（admin/admin123）
- 健康检查：http://localhost:8080/api/health

### 测试账号
- 管理员：admin/123456
- 测试设备：DEV_TEST_001（密钥：test_secret_001）

---

## 🎖️ 项目亮点（面试可提）

1. **完整的技术栈**：Spring Boot 3 + MyBatis-Plus + PostgreSQL + Redis + MQTT
2. **RBAC权限模型**：用户-角色-权限三层设计
3. **MQTT实时通信**：设备接入、上下线监听、数据采集
4. **三层数据架构**：历史数据 + 最新数据 + Redis缓存
5. **数据权限设计**：实时计算，支持多租户
6. **高质量代码**：45个自动化测试，90%覆盖率
7. **工程化实践**：Docker部署、Git规范、完善文档
8. **问题解决能力**：Spring Boot 3兼容性、JSONB类型处理

---

## 📚 重要文档

- [开发计划](../.cursor/plans/----------191517da.plan.md) - 52周详细计划
- [README.md](README.md) - 项目说明
- [CHANGELOG.md](CHANGELOG.md) - 版本更新日志
- [MQTT测试指南](docs/MQTT_TEST_GUIDE.md) - MQTT功能测试
- [贡献指南](CONTRIBUTING.md) - 如何参与开发

---

**下次开发从第7周开始：设备数据模拟器！** 🚀

作者：dongxiang.wu  
最后更新：2025-11-05

