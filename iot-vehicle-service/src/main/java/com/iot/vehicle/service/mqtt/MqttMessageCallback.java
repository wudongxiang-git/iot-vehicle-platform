package com.iot.vehicle.service.mqtt;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * MQTT消息回调处理器
 *
 * @author dongxiang.wu
 */
@Slf4j
@Component
public class MqttMessageCallback implements MqttCallback {

    @Autowired
    private DeviceAuthService deviceAuthService;

    /**
     * 连接丢失回调
     */
    @Override
    public void connectionLost(Throwable cause) {
        log.error("MQTT连接丢失", cause);
        // Paho客户端会自动重连（autoReconnect=true）
    }

    /**
     * 消息到达回调
     */
    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        String payload = new String(message.getPayload());
        log.info("收到MQTT消息: topic={}, payload={}", topic, payload);
        
        // 解析主题，提取设备ID
        String deviceId = extractDeviceId(topic);
        if (deviceId == null) {
            log.warn("无法解析设备ID: topic={}", topic);
            return;
        }

        // 根据主题类型分发处理
        if (topic.contains("/status")) {
            handleDeviceStatus(deviceId, payload);
        } else if (topic.contains("/data")) {
            handleDeviceData(deviceId, payload);
        } else if (topic.contains("/location")) {
            handleDeviceLocation(deviceId, payload);
        } else if (topic.contains("/alarm")) {
            handleDeviceAlarm(deviceId, payload);
        } else if (topic.contains("/heartbeat")) {
            handleDeviceHeartbeat(deviceId, payload);
        }
    }

    /**
     * 消息发送完成回调
     */
    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        log.debug("MQTT消息发送完成: messageId={}", token.getMessageId());
    }

    /**
     * 从主题中提取设备ID
     * 主题格式：device/{deviceId}/messageType
     */
    private String extractDeviceId(String topic) {
        if (topic == null || !topic.startsWith("device/")) {
            return null;
        }
        String[] parts = topic.split("/");
        return parts.length >= 2 ? parts[1] : null;
    }

    /**
     * 处理设备状态消息
     */
    private void handleDeviceStatus(String deviceId, String payload) {
        try {
            JSONObject json = JSON.parseObject(payload);
            String status = json.getString("status");
            
            if ("online".equals(status)) {
                String ipAddress = json.getString("ip");
                deviceAuthService.deviceOnline(deviceId, ipAddress);
            } else if ("offline".equals(status)) {
                deviceAuthService.deviceOffline(deviceId);
            }
        } catch (Exception e) {
            log.error("处理设备状态消息失败: deviceId={}", deviceId, e);
        }
    }

    /**
     * 处理设备数据消息
     */
    private void handleDeviceData(String deviceId, String payload) {
        log.debug("处理设备数据: deviceId={}, payload={}", deviceId, payload);
        // TODO: 实现数据采集功能
    }

    /**
     * 处理设备位置消息
     */
    private void handleDeviceLocation(String deviceId, String payload) {
        log.debug("处理设备位置: deviceId={}, payload={}", deviceId, payload);
        // TODO: 实现位置数据处理
    }

    /**
     * 处理设备告警消息
     */
    private void handleDeviceAlarm(String deviceId, String payload) {
        log.debug("处理设备告警: deviceId={}, payload={}", deviceId, payload);
        // TODO: 实现告警处理
    }

    /**
     * 处理设备心跳消息
     */
    private void handleDeviceHeartbeat(String deviceId, String payload) {
        log.debug("收到设备心跳: deviceId={}", deviceId);
        // TODO: 更新设备最后心跳时间
    }
}


