package com.iot.vehicle.service.mqtt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;

/**
 * MQTT服务
 *
 * @author dongxiang.wu
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MqttService {

    private final MqttClient mqttClient;
    private final MqttMessageCallback mqttMessageCallback;

    @PostConstruct
    public void init() {
        try {
            // 设置回调
            mqttClient.setCallback(mqttMessageCallback);
            
            // 订阅主题（订阅所有设备上行消息）
            mqttClient.subscribe("device/+/data", 1);
            mqttClient.subscribe("device/+/status", 1);
            
            log.info("MQTT服务初始化完成，已订阅主题");
        } catch (MqttException e) {
            log.error("MQTT服务初始化失败", e);
        }
    }

    /**
     * 发布消息
     *
     * @param topic   主题
     * @param payload 消息内容
     */
    public void publish(String topic, String payload) {
        publish(topic, payload, 1, false);
    }

    /**
     * 发布消息
     *
     * @param topic    主题
     * @param payload  消息内容
     * @param qos      消息质量等级（0/1/2）
     * @param retained 是否保留消息
     */
    public void publish(String topic, String payload, int qos, boolean retained) {
        try {
            MqttMessage message = new MqttMessage(payload.getBytes());
            message.setQos(qos);
            message.setRetained(retained);
            
            mqttClient.publish(topic, message);
            
            log.debug("MQTT消息发送成功: topic={}, payload={}", topic, payload);
        } catch (MqttException e) {
            log.error("MQTT消息发送失败: topic={}", topic, e);
        }
    }

    /**
     * 订阅主题
     *
     * @param topic 主题
     */
    public void subscribe(String topic) {
        subscribe(topic, 1);
    }

    /**
     * 订阅主题
     *
     * @param topic 主题
     * @param qos   消息质量等级
     */
    public void subscribe(String topic, int qos) {
        try {
            mqttClient.subscribe(topic, qos);
            log.info("订阅MQTT主题成功: topic={}, qos={}", topic, qos);
        } catch (MqttException e) {
            log.error("订阅MQTT主题失败: topic={}", topic, e);
        }
    }

    /**
     * 取消订阅
     *
     * @param topic 主题
     */
    public void unsubscribe(String topic) {
        try {
            mqttClient.unsubscribe(topic);
            log.info("取消订阅MQTT主题: topic={}", topic);
        } catch (MqttException e) {
            log.error("取消订阅失败: topic={}", topic, e);
        }
    }

    /**
     * 检查连接状态
     *
     * @return true-已连接，false-未连接
     */
    public boolean isConnected() {
        return mqttClient != null && mqttClient.isConnected();
    }
}

