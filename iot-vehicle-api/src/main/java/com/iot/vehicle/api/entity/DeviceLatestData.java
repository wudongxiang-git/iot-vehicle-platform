package com.iot.vehicle.api.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 设备最新数据实体
 *
 * @author dongxiang.wu
 */
@Data
@TableName("tb_device_latest_data")
public class DeviceLatestData implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 设备ID（主键）
     */
    @TableId("device_id")
    private String deviceId;

    /**
     * 数据时间
     */
    @TableField("data_time")
    private LocalDateTime dataTime;

    /**
     * 纬度
     */
    @TableField("latitude")
    private BigDecimal latitude;

    /**
     * 经度
     */
    @TableField("longitude")
    private BigDecimal longitude;

    /**
     * 速度（km/h）
     */
    @TableField("speed")
    private BigDecimal speed;

    /**
     * 方向（0-360度）
     */
    @TableField("direction")
    private Integer direction;

    /**
     * GPS是否有效
     */
    @TableField("gps_valid")
    private Boolean gpsValid;

    /**
     * 发动机转速
     */
    @TableField("speed_rpm")
    private Integer speedRpm;

    /**
     * 油量（%）
     */
    @TableField("fuel_level")
    private BigDecimal fuelLevel;

    /**
     * 发动机温度
     */
    @TableField("engine_temp")
    private Integer engineTemp;

    /**
     * 电池电压
     */
    @TableField("battery_voltage")
    private BigDecimal batteryVoltage;

    /**
     * 里程
     */
    @TableField("mileage")
    private BigDecimal mileage;

    /**
     * 信号强度
     */
    @TableField("signal_strength")
    private Integer signalStrength;

    /**
     * 更新时间
     */
    @TableField("update_time")
    private LocalDateTime updateTime;
}

