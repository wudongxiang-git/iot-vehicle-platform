package com.iot.vehicle.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.iot.vehicle.common.mybatis.handler.JsonbTypeHandler;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 设备实时数据实体
 *
 * @author dongxiang.wu
 */
@Data
@TableName("tb_device_data")
public class DeviceData implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 设备ID
     */
    @TableField("device_id")
    private String deviceId;

    /**
     * 数据时间（设备上报的时间）
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
     * 海拔（米）
     */
    @TableField("altitude")
    private BigDecimal altitude;

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
     * 卫星数量
     */
    @TableField("satellite_count")
    private Integer satelliteCount;

    /**
     * 发动机转速（RPM）
     */
    @TableField("speed_rpm")
    private Integer speedRpm;

    /**
     * 油量（%）
     */
    @TableField("fuel_level")
    private BigDecimal fuelLevel;

    /**
     * 瞬时油耗（L/100km）
     */
    @TableField("fuel_consumption")
    private BigDecimal fuelConsumption;

    /**
     * 发动机温度（℃）
     */
    @TableField("engine_temp")
    private Integer engineTemp;

    /**
     * 电池电压（V）
     */
    @TableField("battery_voltage")
    private BigDecimal batteryVoltage;

    /**
     * 里程（km）
     */
    @TableField("mileage")
    private BigDecimal mileage;

    /**
     * 信号强度（0-100）
     */
    @TableField("signal_strength")
    private Integer signalStrength;

    /**
     * 数据状态：0-正常，1-异常，2-无效
     */
    @TableField("data_status")
    private Integer dataStatus;

    /**
     * 原始数据（JSON格式）
     */
    @TableField(value = "raw_data", typeHandler = JsonbTypeHandler.class)
    private String rawData;

    /**
     * 记录创建时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;
}

