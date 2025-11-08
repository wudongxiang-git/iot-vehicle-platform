# 设备数据模拟器

**作者**: dongxiang.wu  
**版本**: 1.0.0

---

## 📖 简介

设备数据模拟器用于模拟车辆设备的数据上报，无需真实硬件即可测试平台功能。

### 功能特性

- 🚗 模拟GPS轨迹（根据速度和方向移动）
- 📊 模拟OBD数据（转速、油耗、温度、里程）
- 🔢 支持批量设备模拟（可配置数量）
- ⏱️ 可配置数据发送间隔
- 🎮 HTTP接口控制（启动/停止）

---

## 🚀 快速开始

### 1. 配置模拟器

编辑 `application.yml`：

```yaml
simulator:
  device-count: 10          # 模拟设备数量
  send-interval: 5          # 数据发送间隔（秒）
  init-latitude: 31.2304    # 初始纬度（上海）
  init-longitude: 121.4737  # 初始经度
  speed-max: 120            # 最大速度（km/h）
```

### 2. 启动模拟器

```bash
cd iot-vehicle-simulator
mvn spring-boot:run
```

### 3. 控制模拟器

**启动数据发送**：
```bash
curl -X POST http://localhost:8081/simulator/start
```

**停止数据发送**：
```bash
curl -X POST http://localhost:8081/simulator/stop
```

**查询状态**：
```bash
curl -X GET http://localhost:8081/simulator/status
```

---

## 📊 模拟数据说明

### GPS数据
- 位置会根据速度和方向自动移动
- 方向会随机变化（模拟转弯）
- 速度在配置范围内随机

### OBD数据
- **转速**：根据速度计算（速度越快转速越高）
- **油耗**：根据速度消耗（停止时不消耗）
- **温度**：根据转速变化（转速越高温度越高）
- **里程**：根据速度累加

### 生成的设备ID
- 格式：`SIM_DEV_001` 到 `SIM_DEV_XXX`
- 数量由 `device-count` 配置

---

## 🔧 使用场景

### 1. 功能测试
启动少量设备（1-5个）测试数据采集功能

### 2. 性能测试
启动大量设备（100+）测试系统性能

### 3. 演示展示
启动模拟器后台运行，展示实时数据采集

---

## 📝 配置参数详解

| 参数 | 说明 | 默认值 | 范围 |
|------|------|--------|------|
| device-count | 模拟设备数量 | 10 | 1-1000 |
| send-interval | 发送间隔（秒） | 5 | 1-60 |
| init-latitude | 初始纬度 | 31.2304 | -90~90 |
| init-longitude | 初始经度 | 121.4737 | -180~180 |
| gps-drift-range | GPS漂移范围 | 0.01 | 0.001~0.1 |
| speed-min | 最小速度 | 0 | 0~speed-max |
| speed-max | 最大速度 | 120 | speed-min~300 |
| rpm-min | 最小转速 | 800 | 0~rpm-max |
| rpm-max | 最大转速 | 5000 | rpm-min~10000 |

---

## 🎯 注意事项

1. **设备需要先在系统中注册**
   - 模拟器生成的设备ID（SIM_DEV_XXX）需要在平台注册
   - 或修改模拟器使用已注册的设备ID

2. **MQTT连接**
   - 确保EMQ X服务运行
   - 默认连接 `tcp://localhost:1883`

3. **资源消耗**
   - 每个设备一个MQTT连接
   - 建议单机不超过1000个设备

---

## 📚 相关文档

- [MQTT测试指南](../docs/MQTT_TEST_GUIDE.md)
- [项目总结](../PROJECT_SUMMARY.md)
- [API文档](http://localhost:8080/api/doc.html)

---

**作者**: dongxiang.wu  
**最后更新**: 2025-11-05

