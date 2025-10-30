package com.iot.vehicle.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serializable;

/**
 * 创建设备DTO
 *
 * @author dongxiang.wu
 */
@Data
public class CreateDeviceDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 设备名称
     */
    @NotBlank(message = "设备名称不能为空")
    @Size(max = 100, message = "设备名称长度不能超过100个字符")
    private String deviceName;

    /**
     * 设备序列号
     */
    @Size(max = 100, message = "设备序列号长度不能超过100个字符")
    private String deviceSn;

    /**
     * 设备类型
     */
    @NotBlank(message = "设备类型不能为空")
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
     * 通信协议类型
     */
    private String protocolType;

    /**
     * 备注
     */
    @Size(max = 500, message = "备注长度不能超过500个字符")
    private String remark;
}

