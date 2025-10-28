# 数据库设计文档

**项目**: IoT Vehicle Platform  
**作者**: dongxiang.wu  
**更新时间**: 2025-10-26

---

## 数据库概述

- **数据库类型**: PostgreSQL 15
- **字符集**: UTF-8
- **时区**: Asia/Shanghai

---

## 表结构设计

### 1. 用户权限模块

#### 1.1 sys_user（用户表）

| 字段名 | 类型 | 长度 | 是否为空 | 默认值 | 说明 |
|--------|------|------|----------|--------|------|
| id | BIGSERIAL | - | NOT NULL | AUTO | 主键ID |
| username | VARCHAR | 50 | NOT NULL | - | 用户名（唯一） |
| password | VARCHAR | 100 | NOT NULL | - | 密码（BCrypt加密） |
| nickname | VARCHAR | 50 | NULL | - | 昵称 |
| email | VARCHAR | 100 | NULL | - | 邮箱 |
| phone | VARCHAR | 20 | NULL | - | 手机号 |
| avatar | VARCHAR | 255 | NULL | - | 头像URL |
| status | SMALLINT | - | NULL | 1 | 状态：0-禁用，1-正常 |
| last_login_time | TIMESTAMP | - | NULL | - | 最后登录时间 |
| last_login_ip | VARCHAR | 50 | NULL | - | 最后登录IP |
| deleted | SMALLINT | - | NULL | 0 | 删除标记：0-未删除，1-已删除 |
| create_time | TIMESTAMP | - | NULL | CURRENT_TIMESTAMP | 创建时间 |
| update_time | TIMESTAMP | - | NULL | CURRENT_TIMESTAMP | 更新时间 |
| create_by | BIGINT | - | NULL | - | 创建人ID |
| update_by | BIGINT | - | NULL | - | 更新人ID |

**索引**:
- PRIMARY KEY: id
- UNIQUE: username
- INDEX: email, phone, status, deleted

---

#### 1.2 sys_role（角色表）

| 字段名 | 类型 | 长度 | 是否为空 | 默认值 | 说明 |
|--------|------|------|----------|--------|------|
| id | BIGSERIAL | - | NOT NULL | AUTO | 主键ID |
| role_code | VARCHAR | 50 | NOT NULL | - | 角色编码（唯一） |
| role_name | VARCHAR | 50 | NOT NULL | - | 角色名称 |
| description | VARCHAR | 200 | NULL | - | 角色描述 |
| sort_order | INT | - | NULL | 0 | 排序 |
| status | SMALLINT | - | NULL | 1 | 状态：0-禁用，1-正常 |
| deleted | SMALLINT | - | NULL | 0 | 删除标记 |
| create_time | TIMESTAMP | - | NULL | CURRENT_TIMESTAMP | 创建时间 |
| update_time | TIMESTAMP | - | NULL | CURRENT_TIMESTAMP | 更新时间 |
| create_by | BIGINT | - | NULL | - | 创建人ID |
| update_by | BIGINT | - | NULL | - | 更新人ID |

**索引**:
- PRIMARY KEY: id
- UNIQUE: role_code
- INDEX: status, deleted

**预置角色**:
- ROLE_ADMIN: 超级管理员
- ROLE_MANAGER: 管理员
- ROLE_USER: 普通用户

---

#### 1.3 sys_permission（权限表）

| 字段名 | 类型 | 长度 | 是否为空 | 默认值 | 说明 |
|--------|------|------|----------|--------|------|
| id | BIGSERIAL | - | NOT NULL | AUTO | 主键ID |
| parent_id | BIGINT | - | NULL | 0 | 父权限ID（0表示顶级） |
| permission_code | VARCHAR | 100 | NOT NULL | - | 权限编码（唯一） |
| permission_name | VARCHAR | 50 | NOT NULL | - | 权限名称 |
| permission_type | SMALLINT | - | NULL | 1 | 权限类型：1-菜单，2-按钮，3-接口 |
| path | VARCHAR | 200 | NULL | - | 路由路径 |
| component | VARCHAR | 200 | NULL | - | 组件路径 |
| icon | VARCHAR | 100 | NULL | - | 图标 |
| sort_order | INT | - | NULL | 0 | 排序 |
| status | SMALLINT | - | NULL | 1 | 状态：0-禁用，1-正常 |
| deleted | SMALLINT | - | NULL | 0 | 删除标记 |
| create_time | TIMESTAMP | - | NULL | CURRENT_TIMESTAMP | 创建时间 |
| update_time | TIMESTAMP | - | NULL | CURRENT_TIMESTAMP | 更新时间 |
| create_by | BIGINT | - | NULL | - | 创建人ID |
| update_by | BIGINT | - | NULL | - | 更新人ID |

**索引**:
- PRIMARY KEY: id
- UNIQUE: permission_code
- INDEX: parent_id, permission_type, status, deleted

---

#### 1.4 sys_user_role（用户角色关联表）

| 字段名 | 类型 | 长度 | 是否为空 | 默认值 | 说明 |
|--------|------|------|----------|--------|------|
| id | BIGSERIAL | - | NOT NULL | AUTO | 主键ID |
| user_id | BIGINT | - | NOT NULL | - | 用户ID |
| role_id | BIGINT | - | NOT NULL | - | 角色ID |
| create_time | TIMESTAMP | - | NULL | CURRENT_TIMESTAMP | 创建时间 |
| create_by | BIGINT | - | NULL | - | 创建人ID |

**索引**:
- PRIMARY KEY: id
- UNIQUE: (user_id, role_id)
- INDEX: user_id, role_id

---

#### 1.5 sys_role_permission（角色权限关联表）

| 字段名 | 类型 | 长度 | 是否为空 | 默认值 | 说明 |
|--------|------|------|----------|--------|------|
| id | BIGSERIAL | - | NOT NULL | AUTO | 主键ID |
| role_id | BIGINT | - | NOT NULL | - | 角色ID |
| permission_id | BIGINT | - | NOT NULL | - | 权限ID |
| create_time | TIMESTAMP | - | NULL | CURRENT_TIMESTAMP | 创建时间 |
| create_by | BIGINT | - | NULL | - | 创建人ID |

**索引**:
- PRIMARY KEY: id
- UNIQUE: (role_id, permission_id)
- INDEX: role_id, permission_id

---

## RBAC权限模型

### 模型说明

本系统采用标准的RBAC（基于角色的访问控制）模型：

```
用户（User） → 角色（Role） → 权限（Permission）
```

### 关系说明

1. **用户 ← → 角色**：多对多关系
   - 一个用户可以拥有多个角色
   - 一个角色可以分配给多个用户

2. **角色 ← → 权限**：多对多关系
   - 一个角色可以拥有多个权限
   - 一个权限可以分配给多个角色

3. **用户 → 权限**：间接关系
   - 用户通过角色间接获得权限

### 权限类型

1. **菜单权限（permission_type = 1）**
   - 控制菜单是否显示
   - 控制路由是否可访问

2. **按钮权限（permission_type = 2）**
   - 控制页面按钮是否显示
   - 例如：新增、编辑、删除按钮

3. **接口权限（permission_type = 3）**
   - 控制API接口是否可访问
   - 后端通过注解验证

---

## 初始化数据

### 默认管理员账号

- **用户名**: admin
- **密码**: admin123（需要在首次登录后修改）
- **角色**: 超级管理员
- **权限**: 所有权限

### 预置权限（示例）

- 系统管理
  - 用户管理
    - 新增用户
    - 编辑用户
    - 删除用户
    - 重置密码
  - 角色管理
    - 新增角色
    - 编辑角色
    - 删除角色
    - 分配权限

---

## 设计规范

### 命名规范

1. **表名**: 小写字母 + 下划线，使用复数形式
   - 例如：sys_users, sys_roles

2. **字段名**: 小写字母 + 下划线
   - 例如：user_id, create_time

3. **索引名**: idx_表名_字段名
   - 例如：idx_user_username

### 数据类型选择

1. **主键**: BIGSERIAL（自增长整型）
2. **状态字段**: SMALLINT（节省空间）
3. **时间字段**: TIMESTAMP（支持时区）
4. **字符串**: VARCHAR（变长，节省空间）

### 字段规范

1. **必有字段**:
   - id: 主键
   - create_time: 创建时间
   - update_time: 更新时间

2. **可选字段**:
   - deleted: 逻辑删除标记
   - create_by: 创建人
   - update_by: 更新人
   - status: 状态标记

---

## 待开发表（后续计划）

### 设备管理模块（第3-4周）
- tb_device: 设备表
- tb_device_group: 设备分组表
- tb_device_type: 设备类型表

### 数据采集模块（第5-6周）
- tb_device_data: 设备实时数据表
- tb_device_track: 设备轨迹表

### 告警模块（第17-19周）
- tb_alarm_rule: 告警规则表
- tb_alarm_record: 告警记录表

---

**维护者**: dongxiang.wu  
**最后更新**: 2025-10-26

