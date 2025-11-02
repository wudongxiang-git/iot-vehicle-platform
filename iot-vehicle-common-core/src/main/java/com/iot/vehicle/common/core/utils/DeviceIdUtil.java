package com.iot.vehicle.common.core.utils;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 设备ID生成工具类
 *
 * @author dongxiang.wu
 */
public class DeviceIdUtil {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    /**
     * 生成设备ID
     * 格式：DEV + 时间戳(14位) + 随机数(6位)
     * 示例：DEV202510291234567890AB
     *
     * @return 设备ID
     */
    public static String generateDeviceId() {
        String timestamp = LocalDateTime.now().format(FORMATTER);
        String random = RandomUtil.randomString("ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789", 6);
        return "DEV" + timestamp + random;
    }

    /**
     * 生成设备密钥
     * 使用UUID去掉连字符
     *
     * @return 设备密钥
     */
    public static String generateSecretKey() {
        return IdUtil.simpleUUID();
    }
}

