package com.iot.vehicle.service.mqtt;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.stereotype.Component;

/**
 * MQTT消息回调处理器
 *
 * @author dongxiang.wu
 */
@Slf4j
@Component
public class MqttMessageCallback implements MqttCallback {

    /**
     * 连接丢失回调
     */
    @Override
    public void connectionLost(Throwable cause) {
        log.error("MQTT连接丢失", cause);
        // TODO: 触发重连机制
    }

    /**
     * 消息到达回调
     */
    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        String payload = new String(message.getPayload());
        log.info("收到MQTT消息: topic={}, payload={}", topic, payload);
        
        // TODO: 根据主题分发消息到对应的处理器
        // if (topic.startsWith("device/")) {
        //     处理设备消息
        // }
    }

    /**
     * 消息发送完成回调
     */
    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        log.debug("MQTT消息发送完成: messageId={}", token.getMessageId());
    }
}

