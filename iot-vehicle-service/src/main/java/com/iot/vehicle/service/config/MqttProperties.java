package com.iot.vehicle.service.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * MQTT配置属性
 *
 * @author dongxiang.wu
 */
@Data
@Component
@ConfigurationProperties(prefix = "mqtt")
public class MqttProperties {

    /**
     * MQTT Broker地址
     */
    private String broker = "tcp://localhost:1883";

    /**
     * 客户端ID
     */
    private String clientId = "iot-vehicle-server";

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 连接超时（秒）
     */
    private Integer timeout = 30;

    /**
     * 心跳间隔（秒）
     */
    private Integer keepalive = 60;

    /**
     * 清除会话
     */
    private Boolean cleanSession = true;

    /**
     * 自动重连
     */
    private Boolean autoReconnect = true;
}

