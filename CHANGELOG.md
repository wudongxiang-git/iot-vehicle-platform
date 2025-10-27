# 更新日志

本项目的所有重要变更都将记录在此文件中。

格式基于 [Keep a Changelog](https://keepachangelog.com/zh-CN/1.0.0/)，
版本号遵循 [语义化版本](https://semver.org/lang/zh-CN/)。

---

## [Unreleased]

### 计划中
- 用户权限模块
- 设备管理功能
- MQTT 接入层
- 数据采集模块

---

## [1.0.0-alpha] - 2025-10-26

### 新增
- 创建 Maven 多模块项目结构
  - iot-vehicle-common-core（核心模块）
  - iot-vehicle-common-mybatis（MyBatis-Plus 模块）
  - iot-vehicle-common-redis（Redis 模块，空壳）
  - iot-vehicle-api（API 模块）
  - iot-vehicle-service（服务层）
  - iot-vehicle-web（Web 层）

- Spring Boot 基础配置
  - 跨域配置（CorsConfig）
  - Jackson 序列化配置（支持 Java 8 时间格式化）
  - 请求日志拦截器（LogInterceptor）
  - MyBatis-Plus 分页插件（PostgreSQL 适配）
  - WebMVC 配置

- 统一返回结果封装
  - Result 统一返回结果
  - ResultCode 返回状态码枚举
  - PageResult 分页结果封装
  - PageUtil 分页工具类

- 异常处理
  - BusinessException 业务异常
  - GlobalExceptionHandler 全局异常处理器
  - 支持多种异常类型处理

- 测试接口
  - 健康检查接口（/api/health）
  - 分页测试接口（/api/health/test-page）

- Docker 开发环境
  - docker-compose.yml（PostgreSQL + Redis + EMQ X）
  - 一键启动脚本（start-dev.sh）
  - 数据库初始化脚本

- 项目文档
  - README.md（项目说明）
  - CONTRIBUTING.md（贡献指南）
  - LICENSE（Apache 2.0）
  - .gitmessage（Git 提交模板）
  - CHANGELOG.md（更新日志）

### 变更
- 重构 common 模块架构
  - 拆分为 core/mybatis/redis 三个模块
  - 解决依赖耦合问题
  - 更新所有包名和导入语句

### 修复
- 修复 API 文档参数名显示为 arg0/arg1 的问题
- 配置 Maven 保留方法参数名

### 技术栈
- Spring Boot 3.2.0
- Java 17
- PostgreSQL 15
- MyBatis-Plus 3.5.5
- Redis 7.0
- Docker + Docker Compose

### 作者
dongxiang.wu

---

## 版本说明

### 语义化版本

版本格式：主版本号.次版本号.修订号

- 主版本号：不兼容的 API 修改
- 次版本号：向下兼容的功能性新增
- 修订号：向下兼容的问题修正

### 版本阶段

- **alpha**：内部测试版本
- **beta**：公开测试版本
- **rc**：发布候选版本
- **正式版**：稳定发布版本

### 里程碑

- **v1.0.0** (第13周) - MVP 版本
- **v2.0.0** (第26周) - 数据分析和可视化
- **v3.0.0** (第52周) - 微服务架构

---

[Unreleased]: https://github.com/wudongxiang-git/iot-vehicle-platform/compare/v1.0.0-alpha...HEAD
[1.0.0-alpha]: https://github.com/wudongxiang-git/iot-vehicle-platform/releases/tag/v1.0.0-alpha

