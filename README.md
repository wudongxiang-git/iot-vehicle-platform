# IoT Vehicle Platform

<div align="center">

[![License](https://img.shields.io/badge/license-Apache%202-blue.svg)](LICENSE)
[![Java](https://img.shields.io/badge/Java-17-red.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.0-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-blue.svg)](https://www.postgresql.org/)

**车联网数据管理平台**

一个现代化的、可扩展的车联网数据管理平台，支持设备接入、实时监控、数据分析和可视化

[English](README_EN.md) | 简体中文

</div>

---

## 📖 项目简介

IoT Vehicle Platform 是一个基于 Spring Boot 3 和 PostgreSQL 构建的通用车联网数据管理平台。该平台提供完整的设备生命周期管理、实时数据采集、数据分析和可视化功能，无需真实硬件设备即可快速体验和二次开发。

### ✨ 核心特性

- 🚗 **设备管理** - 完整的设备注册、认证、分组和状态管理
- 📡 **数据采集** - 基于 MQTT 协议的实时数据接入
- 📊 **数据分析** - 轨迹回放、行驶报表、驾驶行为分析
- 🔔 **智能告警** - 规则引擎、电子围栏、多渠道通知
- 📱 **移动端支持** - uni-app 跨平台移动应用
- 🎨 **数据大屏** - 实时监控大屏展示
- 🔧 **模拟器** - 内置车辆数据模拟器，无需真实设备

### 🎯 项目目标

- ✅ 提供可用于生产环境的车联网平台基础架构
- ✅ 展示从单体应用到微服务的演进过程
- ✅ 实践现代化的 Java 技术栈和最佳实践
- ✅ 构建易于学习和二次开发的开源项目

---

## 🏗️ 技术架构

### 技术栈

**后端技术**
- Spring Boot 3.2.0
- MyBatis-Plus 3.5.5
- PostgreSQL 15
- Redis 7.x
- MQTT (Eclipse Paho)

**前端技术**
- Vue 3 + TypeScript
- Element Plus
- ECharts
- 高德/百度地图

**移动端**
- uni-app (微信小程序 + H5 + App)

**消息队列**
- Redis Stream (Q1-Q2)
- RocketMQ (Q3+)

**部署运维**
- Docker + Docker Compose
- Kubernetes (Q3+)

### 项目结构

```
iot-vehicle-platform/
├── iot-vehicle-parent              # 父模块（依赖管理）
├── iot-vehicle-common-core         # 公共核心模块（工具类、常量、异常）
├── iot-vehicle-common-mybatis      # MyBatis-Plus公共模块（分页等）
├── iot-vehicle-common-redis        # Redis公共模块（待开发）
├── iot-vehicle-api                 # API模块（实体类、DTO、VO）
├── iot-vehicle-service             # 服务层（业务逻辑）
├── iot-vehicle-web                 # Web层（Controller、配置）
├── iot-vehicle-simulator           # 设备模拟器（待开发）
├── iot-vehicle-frontend            # 前端项目（待开发）
└── iot-vehicle-mobile              # 移动端项目（待开发）
```

---

## 🚀 快速开始

### 环境要求

- JDK 17+
- Maven 3.8+
- PostgreSQL 15+
- Redis 7.0+
- Node.js 18+ (前端开发)

### 本地开发

#### 1. 克隆项目

```bash
git clone https://github.com/wudongxiang-git/iot-vehicle-platform.git
cd iot-vehicle-platform
```

#### 2. 启动 Docker 开发环境

使用一键启动脚本：

```bash
# 启动核心服务（PostgreSQL + Redis + EMQ X）
./start-dev.sh

# 或启动所有服务（包含管理工具）
./start-dev.sh --with-tools
```

或使用 docker-compose 命令：

```bash
# 启动核心服务
docker-compose up -d

# 启动所有服务（包含 Redis Commander 和 pgAdmin）
docker-compose --profile tools up -d
```

**服务地址**：
- PostgreSQL: `localhost:5432`（用户: postgres, 密码: postgres, 数据库: iot_vehicle）
- Redis: `localhost:6379`
- EMQ X MQTT: `tcp://localhost:1883`
- EMQ X Dashboard: http://localhost:18083（admin/admin123）
- Redis Commander: http://localhost:8081（可选）
- pgAdmin: http://localhost:5050（可选）

#### 3. 启动后端应用

```bash
# 编译项目
mvn clean install

# 启动应用
cd iot-vehicle-web
mvn spring-boot:run
```

#### 4. 访问应用

- 应用地址: http://localhost:8080/api
- API文档: http://localhost:8080/api/doc.html
- 健康检查: http://localhost:8080/api/health

#### 5. 停止服务

```bash
# 停止所有 Docker 服务
docker-compose stop

# 停止并删除容器（保留数据）
docker-compose down
```

---

## 🤝 贡献指南

我们欢迎任何形式的贡献！

1. Fork 本仓库
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'feat: Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 提交 Pull Request

提交规范请遵循 [Conventional Commits](https://www.conventionalcommits.org/)

---

## 📄 开源协议

本项目基于 [Apache License 2.0](LICENSE) 开源协议

---

## 👥 联系我们

- 项目主页: https://github.com/wudongxiang-git/iot-vehicle-platform
- 问题反馈: https://github.com/wudongxiang-git/iot-vehicle-platform/issues
- 作者: dongxiang.wu

---

## ⭐ Star History

如果这个项目对你有帮助，请给我一个 Star ⭐

[![Star History Chart](https://api.star-history.com/svg?repos=wudongxiang-git/iot-vehicle-platform&type=Date)](https://star-history.com/#wudongxiang-git/iot-vehicle-platform&Date)

---

<div align="center">

Made with ❤️ by dongxiang.wu

</div>

