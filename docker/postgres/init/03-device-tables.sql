-- IoT Vehicle Platform - Device Management Tables
-- Author: dongxiang.wu
-- Description: 创建设备管理相关表结构
-- Version: 1.0.0

-- ============================================
-- 设备管理模块
-- ============================================

-- 设备表（核心表）
CREATE TABLE IF NOT EXISTS tb_device (
    id BIGSERIAL PRIMARY KEY,
    device_id VARCHAR(64) NOT NULL UNIQUE,
    device_name VARCHAR(100),
    device_sn VARCHAR(100),
    device_type VARCHAR(50),
    device_model VARCHAR(50),
    manufacturer VARCHAR(100),
    imei VARCHAR(50),
    iccid VARCHAR(50),
    group_id BIGINT,
    owner_id BIGINT,
    status SMALLINT DEFAULT 0,
    online_status SMALLINT DEFAULT 0,
    activation_status SMALLINT DEFAULT 0,
    protocol_type VARCHAR(20),
    firmware_version VARCHAR(50),
    hardware_version VARCHAR(50),
    secret_key VARCHAR(64),
    last_online_time TIMESTAMP,
    last_offline_time TIMESTAMP,
    activation_time TIMESTAMP,
    last_location JSONB,
    total_mileage DECIMAL(10, 2) DEFAULT 0,
    total_runtime BIGINT DEFAULT 0,
    metadata JSONB,
    remark VARCHAR(500),
    deleted SMALLINT DEFAULT 0,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    create_by BIGINT,
    update_by BIGINT
);

-- 添加列注释
COMMENT ON COLUMN tb_device.id IS '主键ID';
COMMENT ON COLUMN tb_device.device_id IS '设备唯一标识（业务ID）';
COMMENT ON COLUMN tb_device.device_name IS '设备名称';
COMMENT ON COLUMN tb_device.device_sn IS '设备序列号';
COMMENT ON COLUMN tb_device.device_type IS '设备类型（vehicle/sensor/terminal等）';
COMMENT ON COLUMN tb_device.device_model IS '设备型号';
COMMENT ON COLUMN tb_device.manufacturer IS '制造商';
COMMENT ON COLUMN tb_device.imei IS 'IMEI号（移动设备识别码）';
COMMENT ON COLUMN tb_device.iccid IS 'ICCID号（SIM卡识别码）';
COMMENT ON COLUMN tb_device.group_id IS '设备分组ID';
COMMENT ON COLUMN tb_device.owner_id IS '所有者用户ID';
COMMENT ON COLUMN tb_device.status IS '设备状态：0-未激活，1-正常，2-维护中，3-已停用，4-已报废';
COMMENT ON COLUMN tb_device.online_status IS '在线状态：0-离线，1-在线';
COMMENT ON COLUMN tb_device.activation_status IS '激活状态：0-未激活，1-已激活';
COMMENT ON COLUMN tb_device.protocol_type IS '通信协议类型（MQTT/HTTP/CoAP）';
COMMENT ON COLUMN tb_device.firmware_version IS '固件版本';
COMMENT ON COLUMN tb_device.hardware_version IS '硬件版本';
COMMENT ON COLUMN tb_device.secret_key IS '设备密钥（用于MQTT认证）';
COMMENT ON COLUMN tb_device.last_online_time IS '最后上线时间';
COMMENT ON COLUMN tb_device.last_offline_time IS '最后离线时间';
COMMENT ON COLUMN tb_device.activation_time IS '激活时间';
COMMENT ON COLUMN tb_device.last_location IS '最后位置信息（JSON格式：{lat, lng, address}）';
COMMENT ON COLUMN tb_device.total_mileage IS '累计里程（公里）';
COMMENT ON COLUMN tb_device.total_runtime IS '累计运行时长（秒）';
COMMENT ON COLUMN tb_device.metadata IS '设备元数据（JSON格式，存储扩展属性）';
COMMENT ON COLUMN tb_device.remark IS '备注';
COMMENT ON COLUMN tb_device.deleted IS '删除标记：0-未删除，1-已删除';
COMMENT ON COLUMN tb_device.create_time IS '创建时间';
COMMENT ON COLUMN tb_device.update_time IS '更新时间';
COMMENT ON COLUMN tb_device.create_by IS '创建人ID';
COMMENT ON COLUMN tb_device.update_by IS '更新人ID';

-- 创建索引
CREATE INDEX idx_device_id ON tb_device(device_id);
CREATE INDEX idx_device_sn ON tb_device(device_sn);
CREATE INDEX idx_device_type ON tb_device(device_type);
CREATE INDEX idx_group_id ON tb_device(group_id);
CREATE INDEX idx_owner_id ON tb_device(owner_id);
CREATE INDEX idx_status ON tb_device(status);
CREATE INDEX idx_online_status ON tb_device(online_status);
CREATE INDEX idx_activation_status ON tb_device(activation_status);
CREATE INDEX idx_deleted_device ON tb_device(deleted);
CREATE INDEX idx_create_time_device ON tb_device(create_time);
CREATE INDEX idx_last_online_time ON tb_device(last_online_time);

-- 创建组合索引（常用查询组合）
CREATE INDEX idx_group_status ON tb_device(group_id, status);
CREATE INDEX idx_owner_online ON tb_device(owner_id, online_status);

-- 添加表注释
COMMENT ON TABLE tb_device IS '设备表';

-- ============================================
-- 初始化测试数据
-- ============================================

-- 插入测试设备（用于开发测试）
INSERT INTO tb_device (
    device_id, device_name, device_sn, device_type, device_model, 
    manufacturer, status, online_status, activation_status, 
    protocol_type, secret_key, owner_id
)
VALUES 
    ('DEV_TEST_001', '测试车辆001', 'SN20251028001', 'vehicle', 'Model-X', 
     '测试厂商', 1, 0, 1, 'MQTT', 'test_secret_001', 1),
    ('DEV_TEST_002', '测试车辆002', 'SN20251028002', 'vehicle', 'Model-Y', 
     '测试厂商', 1, 0, 1, 'MQTT', 'test_secret_002', 1),
    ('DEV_TEST_003', '测试车辆003', 'SN20251028003', 'vehicle', 'Model-Z', 
     '测试厂商', 1, 0, 1, 'MQTT', 'test_secret_003', 1)
ON CONFLICT (device_id) DO NOTHING;

-- 输出初始化信息
DO $$
BEGIN
    RAISE NOTICE 'Device Tables Created Successfully!';
    RAISE NOTICE 'Test Devices: 3 devices inserted';
    RAISE NOTICE 'Author: dongxiang.wu';
    RAISE NOTICE 'Timestamp: %', CURRENT_TIMESTAMP;
END $$;

