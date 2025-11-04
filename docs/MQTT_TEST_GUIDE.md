# MQTT功能测试指南

**作者**: dongxiang.wu  
**日期**: 2025-11-03

---

## 🎯 测试目标

验证MQTT接入层的功能：
- ✅ 服务端能成功连接EMQ X
- ✅ 设备能通过MQTT上报数据
- ✅ 服务端能接收和处理设备消息
- ✅ 设备上下线状态能正确更新

---

## 📋 前置准备

### 1. 确认EMQ X运行

```bash
docker-compose ps emqx

# 应该显示 "Up" 状态
```

### 2. 确认应用已启动

应用启动日志应包含：
```
MQTT连接配置已创建: broker=tcp://localhost:1883
MQTT客户端连接成功: clientId=iot-vehicle-server-xxx
MQTT服务初始化完成，已订阅主题
```

### 3. 安装Python MQTT库（如果未安装）

```bash
pip3 install paho-mqtt
```

---

## 🚀 测试步骤

### 步骤1：访问EMQ X Dashboard

1. 打开浏览器访问：**http://localhost:18083**

2. 登录
   - 用户名：`admin`
   - 密码：`admin123`

3. 查看客户端连接
   - 点击左侧菜单「客户端」
   - 应该能看到 `iot-vehicle-server-xxx` 连接

4. 查看订阅列表
   - 点击「订阅」
   - 应该能看到订阅的主题：
     - `device/+/data`
     - `device/+/status`

---

### 步骤2：运行MQTT测试客户端

在新的终端窗口中运行：

```bash
cd /Users/wudongxiang/Documents/open-source/iot-vehicle-platform
python3 mqtt-test-client.py
```

**预期输出**：
```
==================================================
IoT Vehicle Platform - MQTT测试客户端
==================================================
设备ID: DEV_TEST_001
Broker: localhost:1883
==================================================
✅ 连接成功！设备ID: DEV_TEST_001
📤 发送上线消息: device/DEV_TEST_001/status

--- 第 1 次发送 ---
📤 发送设备数据: {"speed": 75, "rpm": 2500, ...}
📤 发送位置数据: lat=31.2345, lng=121.4723
💓 发送心跳

--- 第 2 次发送 ---
...
```

---

### 步骤3：查看服务端日志

在应用的控制台中，应该能看到：

```
收到MQTT消息: topic=device/DEV_TEST_001/status, payload={"status":"online",...}
设备上线: deviceId=DEV_TEST_001, ip=192.168.1.100

收到MQTT消息: topic=device/DEV_TEST_001/data, payload={"speed":75,...}
处理设备数据: deviceId=DEV_TEST_001, payload=...

收到MQTT消息: topic=device/DEV_TEST_001/location, payload={"lat":31.23,...}
处理设备位置: deviceId=DEV_TEST_001, payload=...
```

---

### 步骤4：查看数据库变化

使用数据库客户端或命令行查询：

```sql
-- 查看设备在线状态
SELECT device_id, device_name, online_status, last_online_time
FROM tb_device
WHERE device_id = 'DEV_TEST_001';

-- online_status应该变成1（在线）
-- last_online_time应该更新为当前时间
```

或使用Docker命令：

```bash
docker exec -it iot-vehicle-postgres psql -U postgres -d iot_vehicle -c \
  "SELECT device_id, device_name, online_status, last_online_time FROM tb_device WHERE device_id = 'DEV_TEST_001';"
```

---

### 步骤5：测试设备离线

在Python客户端按 `Ctrl+C` 停止，应该看到：

```
⏹️  停止发送...
📤 发送离线消息
👋 已断开连接
```

服务端日志应该显示：

```
收到MQTT消息: topic=device/DEV_TEST_001/status, payload={"status":"offline"...}
设备离线: deviceId=DEV_TEST_001
```

再次查询数据库：

```sql
SELECT device_id, online_status, last_offline_time
FROM tb_device
WHERE device_id = 'DEV_TEST_001';

-- online_status应该变成0（离线）
-- last_offline_time应该更新
```

---

## ✅ 测试验证清单

完成后打勾：

- [ ] EMQ X服务正常运行
- [ ] 应用启动成功，MQTT连接成功
- [ ] EMQ X Dashboard能看到服务端连接
- [ ] EMQ X Dashboard能看到订阅的主题
- [ ] Python测试客户端能连接成功
- [ ] 服务端能收到设备上线消息
- [ ] 数据库中设备状态更新为在线
- [ ] 服务端能收到设备数据消息
- [ ] 服务端能收到位置消息
- [ ] 服务端能收到心跳消息
- [ ] 设备离线后状态正确更新

---

## 🎨 EMQ X Dashboard功能

在Dashboard中你还可以：

1. **查看实时消息**
   - WebSocket工具：可以订阅主题查看实时消息
   
2. **发布测试消息**
   - 在Dashboard中手动发布消息到设备

3. **查看统计信息**
   - 消息收发数量
   - 连接数统计
   - 订阅数统计

---

## 🐛 常见问题

### 1. Python客户端连接失败

**错误**: `Connection refused`

**解决**:
```bash
# 检查EMQ X是否运行
docker-compose ps emqx

# 重启EMQ X
docker-compose restart emqx
```

### 2. 服务端收不到消息

**检查**:
- EMQ X Dashboard查看是否有消息流量
- 确认服务端已订阅对应主题
- 检查主题格式是否正确

### 3. 设备状态未更新

**检查**:
- 消息格式是否正确（JSON格式）
- device_id是否匹配数据库中的设备
- 查看服务端日志是否有错误

---

## 📝 测试消息格式

### 上线消息

```json
{
  "status": "online",
  "ip": "192.168.1.100",
  "timestamp": 1730649600000
}
```

### 设备数据

```json
{
  "speed": 75,
  "rpm": 2500,
  "fuel": 80,
  "temperature": 85,
  "timestamp": 1730649600000
}
```

### 位置数据

```json
{
  "lat": 31.2304,
  "lng": 121.4737,
  "speed": 60,
  "direction": 180,
  "timestamp": 1730649600000
}
```

---

**祝测试顺利！** 🎉

