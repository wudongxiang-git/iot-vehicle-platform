package com.iot.vehicle.api.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 设备实体
 *
 * @author dongxiang.wu
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("tb_device")
public class Device extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 设备唯一标识（业务ID）
     */
    @TableField("device_id")
    private String deviceId;

    /**
     * 设备名称
     */
    @TableField("device_name")
    private String deviceName;

    /**
     * 设备序列号
     */
    @TableField("device_sn")
    private String deviceSn;

    /**
     * 设备类型（vehicle/sensor/terminal等）
     */
    @TableField("device_type")
    private String deviceType;

    /**
     * 设备型号
     */
    @TableField("device_model")
    private String deviceModel;

    /**
     * 制造商
     */
    @TableField("manufacturer")
    private String manufacturer;

    /**
     * IMEI号（移动设备识别码）
     */
    @TableField("imei")
    private String imei;

    /**
     * ICCID号（SIM卡识别码）
     */
    @TableField("iccid")
    private String iccid;

    /**
     * 设备分组ID
     */
    @TableField("group_id")
    private Long groupId;

    /**
     * 所有者用户ID
     */
    @TableField("owner_id")
    private Long ownerId;

    /**
     * 设备状态：0-未激活，1-正常，2-维护中，3-已停用，4-已报废
     */
    @TableField("status")
    private Integer status;

    /**
     * 在线状态：0-离线，1-在线
     */
    @TableField("online_status")
    private Integer onlineStatus;

    /**
     * 激活状态：0-未激活，1-已激活
     */
    @TableField("activation_status")
    private Integer activationStatus;

    /**
     * 通信协议类型（MQTT/HTTP/CoAP）
     */
    @TableField("protocol_type")
    private String protocolType;

    /**
     * 固件版本
     */
    @TableField("firmware_version")
    private String firmwareVersion;

    /**
     * 硬件版本
     */
    @TableField("hardware_version")
    private String hardwareVersion;

    /**
     * 设备密钥（用于MQTT认证）
     */
    @TableField("secret_key")
    private String secretKey;

    /**
     * 最后上线时间
     */
    @TableField("last_online_time")
    private LocalDateTime lastOnlineTime;

    /**
     * 最后离线时间
     */
    @TableField("last_offline_time")
    private LocalDateTime lastOfflineTime;

    /**
     * 激活时间
     */
    @TableField("activation_time")
    private LocalDateTime activationTime;

    /**
     * 最后位置信息（JSON格式）
     */
    @TableField("last_location")
    private String lastLocation;

    /**
     * 累计里程（公里）
     */
    @TableField("total_mileage")
    private BigDecimal totalMileage;

    /**
     * 累计运行时长（秒）
     */
    @TableField("total_runtime")
    private Long totalRuntime;

    /**
     * 设备元数据（JSON格式）
     */
    @TableField("metadata")
    private String metadata;

    /**
     * 备注
     */
    @TableField("remark")
    private String remark;
}

