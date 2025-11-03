package com.iot.vehicle.common.core.constants;

/**
 * MQTT主题常量
 * 
 * 主题规范说明：
 * - 设备上行消息：device/{deviceId}/{messageType}
 * - 设备下行消息：command/{deviceId}/{commandType}
 * - 系统广播消息：broadcast/{messageType}
 *
 * @author dongxiang.wu
 */
public class MqttTopicConstants {

    /**
     * 设备数据上报主题
     * 格式：device/{deviceId}/data
     * 示例：device/DEV20251029001/data
     */
    public static final String DEVICE_DATA_TOPIC = "device/%s/data";

    /**
     * 设备状态上报主题
     * 格式：device/{deviceId}/status
     * 上报：在线/离线、电量、信号强度等
     */
    public static final String DEVICE_STATUS_TOPIC = "device/%s/status";

    /**
     * 设备位置上报主题
     * 格式：device/{deviceId}/location
     * 上报：GPS坐标、速度、方向等
     */
    public static final String DEVICE_LOCATION_TOPIC = "device/%s/location";

    /**
     * 设备告警上报主题
     * 格式：device/{deviceId}/alarm
     * 上报：超速、围栏、故障等告警
     */
    public static final String DEVICE_ALARM_TOPIC = "device/%s/alarm";

    /**
     * 设备心跳主题
     * 格式：device/{deviceId}/heartbeat
     */
    public static final String DEVICE_HEARTBEAT_TOPIC = "device/%s/heartbeat";

    /**
     * 服务端下发命令主题
     * 格式：command/{deviceId}/{commandType}
     * 示例：command/DEV20251029001/reboot
     */
    public static final String DEVICE_COMMAND_TOPIC = "command/%s/%s";

    /**
     * 系统广播主题
     * 格式：broadcast/{messageType}
     * 用于系统级消息广播
     */
    public static final String BROADCAST_TOPIC = "broadcast/%s";

    /**
     * 订阅所有设备数据
     */
    public static final String SUBSCRIBE_ALL_DEVICE_DATA = "device/+/data";

    /**
     * 订阅所有设备状态
     */
    public static final String SUBSCRIBE_ALL_DEVICE_STATUS = "device/+/status";

    /**
     * 订阅所有设备位置
     */
    public static final String SUBSCRIBE_ALL_DEVICE_LOCATION = "device/+/location";

    /**
     * 订阅所有设备告警
     */
    public static final String SUBSCRIBE_ALL_DEVICE_ALARM = "device/+/alarm";

    /**
     * 订阅所有设备心跳
     */
    public static final String SUBSCRIBE_ALL_DEVICE_HEARTBEAT = "device/+/heartbeat";

    /**
     * 构建设备数据上报主题
     */
    public static String buildDeviceDataTopic(String deviceId) {
        return String.format(DEVICE_DATA_TOPIC, deviceId);
    }

    /**
     * 构建设备状态上报主题
     */
    public static String buildDeviceStatusTopic(String deviceId) {
        return String.format(DEVICE_STATUS_TOPIC, deviceId);
    }

    /**
     * 构建设备位置上报主题
     */
    public static String buildDeviceLocationTopic(String deviceId) {
        return String.format(DEVICE_LOCATION_TOPIC, deviceId);
    }

    /**
     * 构建设备告警上报主题
     */
    public static String buildDeviceAlarmTopic(String deviceId) {
        return String.format(DEVICE_ALARM_TOPIC, deviceId);
    }

    /**
     * 构建设备心跳主题
     */
    public static String buildDeviceHeartbeatTopic(String deviceId) {
        return String.format(DEVICE_HEARTBEAT_TOPIC, deviceId);
    }

    /**
     * 构建设备命令主题
     */
    public static String buildDeviceCommandTopic(String deviceId, String commandType) {
        return String.format(DEVICE_COMMAND_TOPIC, deviceId, commandType);
    }

    /**
     * 构建广播主题
     */
    public static String buildBroadcastTopic(String messageType) {
        return String.format(BROADCAST_TOPIC, messageType);
    }
}

