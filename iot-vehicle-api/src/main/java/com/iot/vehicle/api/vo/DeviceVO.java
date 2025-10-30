package com.iot.vehicle.api.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 设备视图对象
 *
 * @author dongxiang.wu
 */
@Data
public class DeviceVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 设备ID
     */
    private Long id;

    /**
     * 设备唯一标识
     */
    private String deviceId;

    /**
     * 设备名称
     */
    private String deviceName;

    /**
     * 设备序列号
     */
    private String deviceSn;

    /**
     * 设备类型
     */
    private String deviceType;

    /**
     * 设备型号
     */
    private String deviceModel;

    /**
     * 制造商
     */
    private String manufacturer;

    /**
     * IMEI号
     */
    private String imei;

    /**
     * ICCID号
     */
    private String iccid;

    /**
     * 设备分组ID
     */
    private Long groupId;

    /**
     * 设备分组名称
     */
    private String groupName;

    /**
     * 所有者用户ID
     */
    private Long ownerId;

    /**
     * 所有者用户名
     */
    private String ownerName;

    /**
     * 设备状态：0-未激活，1-正常，2-维护中，3-已停用，4-已报废
     */
    private Integer status;

    /**
     * 设备状态描述
     */
    private String statusDesc;

    /**
     * 在线状态：0-离线，1-在线
     */
    private Integer onlineStatus;

    /**
     * 在线状态描述
     */
    private String onlineStatusDesc;

    /**
     * 激活状态：0-未激活，1-已激活
     */
    private Integer activationStatus;

    /**
     * 通信协议类型
     */
    private String protocolType;

    /**
     * 固件版本
     */
    private String firmwareVersion;

    /**
     * 硬件版本
     */
    private String hardwareVersion;

    /**
     * 最后上线时间
     */
    private LocalDateTime lastOnlineTime;

    /**
     * 最后离线时间
     */
    private LocalDateTime lastOfflineTime;

    /**
     * 激活时间
     */
    private LocalDateTime activationTime;

    /**
     * 最后位置信息
     */
    private String lastLocation;

    /**
     * 累计里程（公里）
     */
    private BigDecimal totalMileage;

    /**
     * 累计运行时长（秒）
     */
    private Long totalRuntime;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}

