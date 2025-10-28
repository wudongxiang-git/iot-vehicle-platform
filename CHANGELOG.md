# 更新日志

本项目的所有重要变更都将记录在此文件中。

格式基于 [Keep a Changelog](https://keepachangelog.com/zh-CN/1.0.0/)，
版本号遵循 [语义化版本](https://semver.org/lang/zh-CN/)。

---

## [Unreleased]

### 计划中
- 设备管理功能
- MQTT 接入层
- 数据采集模块

---

## [1.0.0-beta] - 2025-10-28

### 新增

**用户权限模块（RBAC模型）**
- 5个数据库表：sys_user、sys_role、sys_permission、sys_user_role、sys_role_permission
- 14个实体类和DTO/VO对象
- 预置超级管理员账号（admin/123456）

**JWT认证系统**
- JwtUtil工具类（Token生成/验证/刷新）
- PasswordUtil工具类（BCrypt加密）
- AuthController：注册、登录、登出、刷新Token（4个接口）

**用户管理**
- UserController：用户CRUD、密码管理、状态管理（8个接口）
- UserService：完整的业务逻辑实现

**角色管理**  
- RoleController：角色CRUD、权限分配、角色分配（9个接口）
- RoleService：角色业务逻辑实现
- 3个Mapper接口和2个XML映射文件

**权限验证框架**
- @RequirePermission和@RequireRole注解
- PermissionAspect权限验证切面
- JwtInterceptor拦截器

**新增模块**
- iot-vehicle-common-web模块（Web工具类）
- UserContextUtil（用户上下文工具）

### 变更
- Spring Boot版本：3.2.0 → 3.1.5（解决兼容性）
- MyBatis-Plus版本：3.5.5 → 3.5.7

### 修复
- 修复Spring Boot 3与MyBatis-Plus的兼容性问题
- 修复API参数名显示问题（@RequestParam明确指定参数名）

### 技术栈
Spring Boot 3.1.5 + MyBatis-Plus 3.5.7 + JWT + Knife4j 4.5.0


作者：dongxiang.wu

---

## [1.0.0-alpha] - 2025-10-26

### 新增

**项目架构**
- Maven多模块项目：common-core、common-mybatis、common-redis、api、service、web
- 清晰的分层架构和依赖管理

**基础功能**
- Result统一返回结果封装
- PageResult分页结果封装
- BusinessException业务异常
- GlobalExceptionHandler全局异常处理
- 健康检查接口

**Spring Boot配置**
- CorsConfig跨域配置
- JacksonConfig序列化配置
- LogInterceptor请求日志拦截器
- MybatisPlusConfig分页插件

**Docker开发环境**
- docker-compose.yml（PostgreSQL + Redis + EMQ X）
- start-dev.sh一键启动脚本

**项目规范**
- Git提交规范（.gitmessage）
- 贡献指南（CONTRIBUTING.md）
- Apache 2.0开源协议

### 技术栈
Spring Boot 3.2.0 + PostgreSQL 15 + MyBatis-Plus 3.5.5 + Redis

作者：dongxiang.wu

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

