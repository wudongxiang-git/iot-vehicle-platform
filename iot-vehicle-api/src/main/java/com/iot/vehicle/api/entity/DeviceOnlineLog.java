package com.iot.vehicle.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 设备在线状态记录实体
 *
 * @author dongxiang.wu
 */
@Data
@TableName("tb_device_online_log")
public class DeviceOnlineLog implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 设备ID
     */
    @TableField("device_id")
    private String deviceId;

    /**
     * 事件类型：1-上线，0-离线
     */
    @TableField("event_type")
    private Integer eventType;

    /**
     * 事件时间
     */
    @TableField("event_time")
    private LocalDateTime eventTime;

    /**
     * IP地址
     */
    @TableField("ip_address")
    private String ipAddress;

    /**
     * 客户端信息
     */
    @TableField("client_info")
    private String clientInfo;

    /**
     * 备注
     */
    @TableField("remark")
    private String remark;

    /**
     * 记录创建时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;
}

