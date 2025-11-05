-- IoT Vehicle Platform - Device Data Tables
-- Author: dongxiang.wu
-- Description: 创建设备数据采集相关表结构
-- Version: 1.0.0

-- ============================================
-- 设备数据采集模块
-- ============================================

-- 设备实时数据表（核心数据表）
CREATE TABLE IF NOT EXISTS tb_device_data (
    id BIGSERIAL PRIMARY KEY,
    device_id VARCHAR(64) NOT NULL,
    data_time TIMESTAMP NOT NULL,
    latitude DECIMAL(10, 7),
    longitude DECIMAL(10, 7),
    altitude DECIMAL(8, 2),
    speed DECIMAL(6, 2),
    direction SMALLINT,
    gps_valid BOOLEAN DEFAULT false,
    satellite_count SMALLINT,
    speed_rpm INT,
    fuel_level DECIMAL(5, 2),
    fuel_consumption DECIMAL(6, 2),
    engine_temp SMALLINT,
    battery_voltage DECIMAL(5, 2),
    mileage DECIMAL(10, 2),
    signal_strength SMALLINT,
    data_status SMALLINT DEFAULT 0,
    raw_data JSONB,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 添加列注释
COMMENT ON COLUMN tb_device_data.id IS '主键ID';
COMMENT ON COLUMN tb_device_data.device_id IS '设备ID';
COMMENT ON COLUMN tb_device_data.data_time IS '数据时间（设备上报的时间）';
COMMENT ON COLUMN tb_device_data.latitude IS '纬度';
COMMENT ON COLUMN tb_device_data.longitude IS '经度';
COMMENT ON COLUMN tb_device_data.altitude IS '海拔（米）';
COMMENT ON COLUMN tb_device_data.speed IS '速度（km/h）';
COMMENT ON COLUMN tb_device_data.direction IS '方向（0-360度）';
COMMENT ON COLUMN tb_device_data.gps_valid IS 'GPS是否有效';
COMMENT ON COLUMN tb_device_data.satellite_count IS '卫星数量';
COMMENT ON COLUMN tb_device_data.speed_rpm IS '发动机转速（RPM）';
COMMENT ON COLUMN tb_device_data.fuel_level IS '油量（%）';
COMMENT ON COLUMN tb_device_data.fuel_consumption IS '瞬时油耗（L/100km）';
COMMENT ON COLUMN tb_device_data.engine_temp IS '发动机温度（℃）';
COMMENT ON COLUMN tb_device_data.battery_voltage IS '电池电压（V）';
COMMENT ON COLUMN tb_device_data.mileage IS '里程（km）';
COMMENT ON COLUMN tb_device_data.signal_strength IS '信号强度（0-100）';
COMMENT ON COLUMN tb_device_data.data_status IS '数据状态：0-正常，1-异常，2-无效';
COMMENT ON COLUMN tb_device_data.raw_data IS '原始数据（JSON格式，保留完整数据）';
COMMENT ON COLUMN tb_device_data.create_time IS '记录创建时间';

-- 创建索引（数据量大，索引很重要）
CREATE INDEX idx_device_id_data ON tb_device_data(device_id);
CREATE INDEX idx_data_time ON tb_device_data(data_time DESC);
CREATE INDEX idx_device_time ON tb_device_data(device_id, data_time DESC);
CREATE INDEX idx_create_time_data ON tb_device_data(create_time DESC);

-- 创建分区（按月分区，提升查询性能）
-- 注：PostgreSQL 10+支持声明式分区
-- 暂时不创建，第36周（分库分表）时再实现

-- 添加表注释
COMMENT ON TABLE tb_device_data IS '设备实时数据表';

-- ============================================
-- 设备最新数据表（冗余设计，提升查询性能）
-- ============================================

CREATE TABLE IF NOT EXISTS tb_device_latest_data (
    device_id VARCHAR(64) PRIMARY KEY,
    data_time TIMESTAMP NOT NULL,
    latitude DECIMAL(10, 7),
    longitude DECIMAL(10, 7),
    speed DECIMAL(6, 2),
    direction SMALLINT,
    gps_valid BOOLEAN,
    speed_rpm INT,
    fuel_level DECIMAL(5, 2),
    engine_temp SMALLINT,
    battery_voltage DECIMAL(5, 2),
    mileage DECIMAL(10, 2),
    signal_strength SMALLINT,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 添加列注释
COMMENT ON COLUMN tb_device_latest_data.device_id IS '设备ID（主键）';
COMMENT ON COLUMN tb_device_latest_data.data_time IS '数据时间';
COMMENT ON COLUMN tb_device_latest_data.latitude IS '纬度';
COMMENT ON COLUMN tb_device_latest_data.longitude IS '经度';
COMMENT ON COLUMN tb_device_latest_data.speed IS '速度（km/h）';
COMMENT ON COLUMN tb_device_latest_data.direction IS '方向（0-360度）';
COMMENT ON COLUMN tb_device_latest_data.gps_valid IS 'GPS是否有效';
COMMENT ON COLUMN tb_device_latest_data.speed_rpm IS '发动机转速';
COMMENT ON COLUMN tb_device_latest_data.fuel_level IS '油量（%）';
COMMENT ON COLUMN tb_device_latest_data.engine_temp IS '发动机温度';
COMMENT ON COLUMN tb_device_latest_data.battery_voltage IS '电池电压';
COMMENT ON COLUMN tb_device_latest_data.mileage IS '里程';
COMMENT ON COLUMN tb_device_latest_data.signal_strength IS '信号强度';
COMMENT ON COLUMN tb_device_latest_data.update_time IS '更新时间';

-- 添加表注释
COMMENT ON TABLE tb_device_latest_data IS '设备最新数据表（用于实时查询）';

-- 输出初始化信息
DO $$
BEGIN
    RAISE NOTICE 'Device Data Tables Created Successfully!';
    RAISE NOTICE 'Tables: tb_device_data, tb_device_latest_data';
    RAISE NOTICE 'Indexes: 4 indexes on tb_device_data';
    RAISE NOTICE 'Author: dongxiang.wu';
    RAISE NOTICE 'Timestamp: %', CURRENT_TIMESTAMP;
END $$;

