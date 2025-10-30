package com.iot.vehicle.common.core.enums;

import lombok.Getter;

/**
 * 在线状态枚举
 *
 * @author dongxiang.wu
 */
@Getter
public enum OnlineStatus {

    /**
     * 离线
     */
    OFFLINE(0, "离线"),

    /**
     * 在线
     */
    ONLINE(1, "在线");

    private final Integer code;
    private final String desc;

    OnlineStatus(Integer code, String desc) {
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
        for (OnlineStatus status : values()) {
            if (status.getCode().equals(code)) {
                return status.getDesc();
            }
        }
        return null;
    }
}

