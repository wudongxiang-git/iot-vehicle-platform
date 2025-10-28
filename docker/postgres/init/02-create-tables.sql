-- IoT Vehicle Platform - Database Tables Creation Script
-- Author: dongxiang.wu
-- Description: 创建用户权限相关表结构
-- Version: 1.0.0

-- ============================================
-- 系统管理 - 用户权限模块
-- ============================================

-- 用户表
CREATE TABLE IF NOT EXISTS sys_user (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    nickname VARCHAR(50),
    email VARCHAR(100),
    phone VARCHAR(20),
    avatar VARCHAR(255),
    status SMALLINT DEFAULT 1,
    last_login_time TIMESTAMP,
    last_login_ip VARCHAR(50),
    deleted SMALLINT DEFAULT 0,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    create_by BIGINT,
    update_by BIGINT
);

-- 添加列注释
COMMENT ON COLUMN sys_user.id IS '主键ID';
COMMENT ON COLUMN sys_user.username IS '用户名';
COMMENT ON COLUMN sys_user.password IS '密码（BCrypt加密）';
COMMENT ON COLUMN sys_user.nickname IS '昵称';
COMMENT ON COLUMN sys_user.email IS '邮箱';
COMMENT ON COLUMN sys_user.phone IS '手机号';
COMMENT ON COLUMN sys_user.avatar IS '头像URL';
COMMENT ON COLUMN sys_user.status IS '状态：0-禁用，1-正常';
COMMENT ON COLUMN sys_user.last_login_time IS '最后登录时间';
COMMENT ON COLUMN sys_user.last_login_ip IS '最后登录IP';
COMMENT ON COLUMN sys_user.deleted IS '删除标记：0-未删除，1-已删除';
COMMENT ON COLUMN sys_user.create_time IS '创建时间';
COMMENT ON COLUMN sys_user.update_time IS '更新时间';
COMMENT ON COLUMN sys_user.create_by IS '创建人ID';
COMMENT ON COLUMN sys_user.update_by IS '更新人ID';

-- 创建索引
CREATE INDEX idx_username ON sys_user(username);
CREATE INDEX idx_email ON sys_user(email);
CREATE INDEX idx_phone ON sys_user(phone);
CREATE INDEX idx_status ON sys_user(status);
CREATE INDEX idx_deleted ON sys_user(deleted);

-- 角色表
CREATE TABLE IF NOT EXISTS sys_role (
    id BIGSERIAL PRIMARY KEY,
    role_code VARCHAR(50) NOT NULL UNIQUE,
    role_name VARCHAR(50) NOT NULL,
    description VARCHAR(200),
    sort_order INT DEFAULT 0,
    status SMALLINT DEFAULT 1,
    deleted SMALLINT DEFAULT 0,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    create_by BIGINT,
    update_by BIGINT
);

-- 添加列注释
COMMENT ON COLUMN sys_role.id IS '主键ID';
COMMENT ON COLUMN sys_role.role_code IS '角色编码（ROLE_ADMIN、ROLE_USER等）';
COMMENT ON COLUMN sys_role.role_name IS '角色名称';
COMMENT ON COLUMN sys_role.description IS '角色描述';
COMMENT ON COLUMN sys_role.sort_order IS '排序';
COMMENT ON COLUMN sys_role.status IS '状态：0-禁用，1-正常';
COMMENT ON COLUMN sys_role.deleted IS '删除标记：0-未删除，1-已删除';
COMMENT ON COLUMN sys_role.create_time IS '创建时间';
COMMENT ON COLUMN sys_role.update_time IS '更新时间';
COMMENT ON COLUMN sys_role.create_by IS '创建人ID';
COMMENT ON COLUMN sys_role.update_by IS '更新人ID';

-- 创建索引
CREATE INDEX idx_role_code ON sys_role(role_code);
CREATE INDEX idx_status_role ON sys_role(status);
CREATE INDEX idx_deleted_role ON sys_role(deleted);

-- 权限表（菜单权限）
CREATE TABLE IF NOT EXISTS sys_permission (
    id BIGSERIAL PRIMARY KEY,
    parent_id BIGINT DEFAULT 0,
    permission_code VARCHAR(100) NOT NULL UNIQUE,
    permission_name VARCHAR(50) NOT NULL,
    permission_type SMALLINT DEFAULT 1,
    path VARCHAR(200),
    component VARCHAR(200),
    icon VARCHAR(100),
    sort_order INT DEFAULT 0,
    status SMALLINT DEFAULT 1,
    deleted SMALLINT DEFAULT 0,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    create_by BIGINT,
    update_by BIGINT
);

-- 添加列注释
COMMENT ON COLUMN sys_permission.id IS '主键ID';
COMMENT ON COLUMN sys_permission.parent_id IS '父权限ID（0表示顶级权限）';
COMMENT ON COLUMN sys_permission.permission_code IS '权限编码（system:user:add）';
COMMENT ON COLUMN sys_permission.permission_name IS '权限名称';
COMMENT ON COLUMN sys_permission.permission_type IS '权限类型：1-菜单，2-按钮，3-接口';
COMMENT ON COLUMN sys_permission.path IS '路由路径';
COMMENT ON COLUMN sys_permission.component IS '组件路径';
COMMENT ON COLUMN sys_permission.icon IS '图标';
COMMENT ON COLUMN sys_permission.sort_order IS '排序';
COMMENT ON COLUMN sys_permission.status IS '状态：0-禁用，1-正常';
COMMENT ON COLUMN sys_permission.deleted IS '删除标记：0-未删除，1-已删除';
COMMENT ON COLUMN sys_permission.create_time IS '创建时间';
COMMENT ON COLUMN sys_permission.update_time IS '更新时间';
COMMENT ON COLUMN sys_permission.create_by IS '创建人ID';
COMMENT ON COLUMN sys_permission.update_by IS '更新人ID';

-- 创建索引
CREATE INDEX idx_parent_id ON sys_permission(parent_id);
CREATE INDEX idx_permission_code ON sys_permission(permission_code);
CREATE INDEX idx_permission_type ON sys_permission(permission_type);
CREATE INDEX idx_status_permission ON sys_permission(status);
CREATE INDEX idx_deleted_permission ON sys_permission(deleted);

-- 用户角色关联表
CREATE TABLE IF NOT EXISTS sys_user_role (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    create_by BIGINT,
    CONSTRAINT uk_user_role UNIQUE (user_id, role_id)
);

-- 添加列注释
COMMENT ON COLUMN sys_user_role.id IS '主键ID';
COMMENT ON COLUMN sys_user_role.user_id IS '用户ID';
COMMENT ON COLUMN sys_user_role.role_id IS '角色ID';
COMMENT ON COLUMN sys_user_role.create_time IS '创建时间';
COMMENT ON COLUMN sys_user_role.create_by IS '创建人ID';

-- 创建索引
CREATE INDEX idx_user_id ON sys_user_role(user_id);
CREATE INDEX idx_role_id ON sys_user_role(role_id);

-- 角色权限关联表
CREATE TABLE IF NOT EXISTS sys_role_permission (
    id BIGSERIAL PRIMARY KEY,
    role_id BIGINT NOT NULL,
    permission_id BIGINT NOT NULL,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    create_by BIGINT,
    CONSTRAINT uk_role_permission UNIQUE (role_id, permission_id)
);

-- 添加列注释
COMMENT ON COLUMN sys_role_permission.id IS '主键ID';
COMMENT ON COLUMN sys_role_permission.role_id IS '角色ID';
COMMENT ON COLUMN sys_role_permission.permission_id IS '权限ID';
COMMENT ON COLUMN sys_role_permission.create_time IS '创建时间';
COMMENT ON COLUMN sys_role_permission.create_by IS '创建人ID';

-- 创建索引
CREATE INDEX idx_role_id_rp ON sys_role_permission(role_id);
CREATE INDEX idx_permission_id ON sys_role_permission(permission_id);

-- ============================================
-- 初始化数据
-- ============================================

-- 插入默认超级管理员用户（密码：admin123，需要使用BCrypt加密）
-- 注意：实际使用时需要替换为加密后的密码
INSERT INTO sys_user (id, username, password, nickname, email, status, create_time)
VALUES (1, 'admin', '$2a$10$06.Bc3MaMqYHxLT9ZmwAJuHunJUVgZFg5WahIHg4cQP93Rf9.31we', '超级管理员', 'dd563254916@qq.com', 1, CURRENT_TIMESTAMP)
ON CONFLICT (username) DO NOTHING;

-- 插入默认角色
INSERT INTO sys_role (id, role_code, role_name, description, sort_order, status)
VALUES 
    (1, 'ROLE_ADMIN', '超级管理员', '拥有所有权限', 1, 1),
    (2, 'ROLE_MANAGER', '管理员', '拥有管理权限', 2, 1),
    (3, 'ROLE_USER', '普通用户', '基础权限', 3, 1)
ON CONFLICT (role_code) DO NOTHING;

-- 插入默认权限（菜单权限）
INSERT INTO sys_permission (id, parent_id, permission_code, permission_name, permission_type, path, icon, sort_order, status)
VALUES 
    -- 一级菜单
    (1, 0, 'system', '系统管理', 1, '/system', 'setting', 1, 1),
    (2, 0, 'device', '设备管理', 1, '/device', 'laptop', 2, 1),
    (3, 0, 'data', '数据管理', 1, '/data', 'database', 3, 1),
    (4, 0, 'monitor', '监控中心', 1, '/monitor', 'monitor', 4, 1),
    
    -- 系统管理子菜单
    (11, 1, 'system:user', '用户管理', 1, '/system/user', 'user', 1, 1),
    (12, 1, 'system:role', '角色管理', 1, '/system/role', 'peoples', 2, 1),
    (13, 1, 'system:permission', '权限管理', 1, '/system/permission', 'tree', 3, 1),
    
    -- 用户管理按钮权限
    (111, 11, 'system:user:add', '新增用户', 2, NULL, NULL, 1, 1),
    (112, 11, 'system:user:edit', '编辑用户', 2, NULL, NULL, 2, 1),
    (113, 11, 'system:user:delete', '删除用户', 2, NULL, NULL, 3, 1),
    (114, 11, 'system:user:reset', '重置密码', 2, NULL, NULL, 4, 1),
    
    -- 角色管理按钮权限
    (121, 12, 'system:role:add', '新增角色', 2, NULL, NULL, 1, 1),
    (122, 12, 'system:role:edit', '编辑角色', 2, NULL, NULL, 2, 1),
    (123, 12, 'system:role:delete', '删除角色', 2, NULL, NULL, 3, 1),
    (124, 12, 'system:role:assign', '分配权限', 2, NULL, NULL, 4, 1)
ON CONFLICT (permission_code) DO NOTHING;

-- 给超级管理员分配角色
INSERT INTO sys_user_role (user_id, role_id)
VALUES (1, 1)
ON CONFLICT (user_id, role_id) DO NOTHING;

-- 给超级管理员角色分配所有权限
INSERT INTO sys_role_permission (role_id, permission_id)
SELECT 1, id FROM sys_permission WHERE deleted = 0
ON CONFLICT (role_id, permission_id) DO NOTHING;

-- ============================================
-- 添加表注释
-- ============================================

COMMENT ON TABLE sys_user IS '系统用户表';
COMMENT ON TABLE sys_role IS '系统角色表';
COMMENT ON TABLE sys_permission IS '系统权限表';
COMMENT ON TABLE sys_user_role IS '用户角色关联表';
COMMENT ON TABLE sys_role_permission IS '角色权限关联表';

-- 输出初始化信息
DO $$
BEGIN
    RAISE NOTICE 'User Permission Tables Created Successfully!';
    RAISE NOTICE 'Default Admin User: admin / admin123';
    RAISE NOTICE 'Author: dongxiang.wu';
    RAISE NOTICE 'Timestamp: %', CURRENT_TIMESTAMP;
END $$;

