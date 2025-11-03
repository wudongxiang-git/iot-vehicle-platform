package com.iot.vehicle.service.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * MQTT配置
 *
 * @author dongxiang.wu
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class MqttConfig {

    private final MqttProperties mqttProperties;

    @Bean
    public MqttConnectOptions mqttConnectOptions() {
        MqttConnectOptions options = new MqttConnectOptions();
        
        // 设置Broker地址
        options.setServerURIs(new String[]{mqttProperties.getBroker()});
        
        // 设置用户名密码（如果有）
        if (mqttProperties.getUsername() != null && !mqttProperties.getUsername().isEmpty()) {
            options.setUserName(mqttProperties.getUsername());
        }
        if (mqttProperties.getPassword() != null && !mqttProperties.getPassword().isEmpty()) {
            options.setPassword(mqttProperties.getPassword().toCharArray());
        }
        
        // 设置连接参数
        options.setConnectionTimeout(mqttProperties.getTimeout());
        options.setKeepAliveInterval(mqttProperties.getKeepalive());
        options.setCleanSession(mqttProperties.getCleanSession());
        options.setAutomaticReconnect(mqttProperties.getAutoReconnect());
        
        // 设置遗嘱消息（可选）
        // options.setWill("server/status", "offline".getBytes(), 1, false);
        
        log.info("MQTT连接配置已创建: broker={}", mqttProperties.getBroker());
        
        return options;
    }

    @Bean
    public MqttClient mqttClient(MqttConnectOptions mqttConnectOptions) {
        try {
            MqttClient client = new MqttClient(
                    mqttProperties.getBroker(),
                    mqttProperties.getClientId()
            );
            
            // 连接到Broker
            client.connect(mqttConnectOptions);
            
            log.info("MQTT客户端连接成功: clientId={}", mqttProperties.getClientId());
            
            return client;
        } catch (MqttException e) {
            log.error("MQTT客户端连接失败", e);
            throw new RuntimeException("MQTT客户端初始化失败", e);
        }
    }
}

