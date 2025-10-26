-- IoT Vehicle Platform - Database Initialization Script
-- Author: dongxiang.wu
-- Description: 创建数据库和基础表结构

-- 设置时区
SET timezone = 'Asia/Shanghai';

-- 创建扩展（TimescaleDB暂不启用，后续引入）
-- CREATE EXTENSION IF NOT EXISTS timescaledb;

-- 创建数据库（如果不存在）
-- 注意：此脚本在docker-entrypoint-initdb.d中运行时，数据库已创建
-- 此处仅作为参考

-- ============================================
-- 系统管理表
-- ============================================

-- 用户表（待实现）
-- CREATE TABLE IF NOT EXISTS sys_user (
--     id BIGSERIAL PRIMARY KEY,
--     username VARCHAR(50) NOT NULL UNIQUE,
--     password VARCHAR(100) NOT NULL,
--     nickname VARCHAR(50),
--     email VARCHAR(100),
--     phone VARCHAR(20),
--     avatar VARCHAR(255),
--     status SMALLINT DEFAULT 1,
--     deleted SMALLINT DEFAULT 0,
--     create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
--     update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
-- );

-- ============================================
-- 设备管理表（待实现）
-- ============================================

-- 设备表
-- CREATE TABLE IF NOT EXISTS iot_device (
--     id BIGSERIAL PRIMARY KEY,
--     device_id VARCHAR(64) NOT NULL UNIQUE,
--     device_name VARCHAR(100),
--     device_type VARCHAR(50),
--     device_secret VARCHAR(64),
--     status SMALLINT DEFAULT 0,
--     online_status SMALLINT DEFAULT 0,
--     group_id BIGINT,
--     deleted SMALLINT DEFAULT 0,
--     create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
--     update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
-- );

-- ============================================
-- 测试数据
-- ============================================

-- 创建测试表验证数据库连接
CREATE TABLE IF NOT EXISTS test_table (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 插入测试数据
INSERT INTO test_table (name) VALUES ('Database initialized successfully');

-- 输出初始化信息
DO $$
BEGIN
    RAISE NOTICE 'IoT Vehicle Platform Database initialized successfully!';
    RAISE NOTICE 'Author: dongxiang.wu';
    RAISE NOTICE 'Timestamp: %', CURRENT_TIMESTAMP;
END $$;

