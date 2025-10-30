package com.iot.vehicle.common.core.enums;

import lombok.Getter;

/**
 * 设备状态枚举
 *
 * @author dongxiang.wu
 */
@Getter
public enum DeviceStatus {

    /**
     * 未激活
     */
    NOT_ACTIVATED(0, "未激活"),

    /**
     * 正常
     */
    NORMAL(1, "正常"),

    /**
     * 维护中
     */
    MAINTENANCE(2, "维护中"),

    /**
     * 已停用
     */
    DISABLED(3, "已停用"),

    /**
     * 已报废
     */
    SCRAPPED(4, "已报废");

    private final Integer code;
    private final String desc;

    DeviceStatus(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    /**
     * 根据code获取描述
     */
    public static String getDescByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (DeviceStatus status : values()) {
            if (status.getCode().equals(code)) {
                return status.getDesc();
            }
        }
        return null;
    }
}

