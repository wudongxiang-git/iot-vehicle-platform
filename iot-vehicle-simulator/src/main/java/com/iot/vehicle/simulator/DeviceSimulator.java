package com.iot.vehicle.simulator;

import cn.hutool.json.JSONUtil;
import com.iot.vehicle.simulator.config.SimulatorProperties;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 设备模拟器
 * 
 * Day1-3: 基础GPS和OBD数据模拟
 *
 * @author dongxiang.wu
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DeviceSimulator {

    private final SimulatorProperties properties;
    private final Map<String, VirtualDevice> devices = new ConcurrentHashMap<>();
    private ScheduledExecutorService executor;
    private volatile boolean running = false;

    /**
     * 启动模拟器
     */
    public void start() {
        if (running) {
            log.warn("模拟器已在运行中");
            return;
        }

        log.info("启动设备模拟器，设备数量：{}", properties.getDeviceCount());
        
        // 初始化虚拟设备
        initDevices();
        
        // 启动定时任务
        executor = Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors());
        executor.scheduleAtFixedRate(this::sendAllDevicesData, 0, properties.getSendInterval(), TimeUnit.SECONDS);
        
        running = true;
        log.info("模拟器启动成功");
    }

    /**
     * 停止模拟器
     */
    public void stop() {
        if (!running) {
            return;
        }

        running = false;
        if (executor != null) {
            executor.shutdown();
        }
        
        // 断开所有设备连接
        devices.values().forEach(VirtualDevice::disconnect);
        devices.clear();
        
        log.info("模拟器已停止");
    }

    /**
     * 初始化虚拟设备
     */
    private void initDevices() {
        for (int i = 1; i <= properties.getDeviceCount(); i++) {
            String deviceId = String.format("SIM_DEV_%03d", i);
            VirtualDevice device = new VirtualDevice(deviceId, properties);
            device.connect();
            devices.put(deviceId, device);
        }
        log.info("初始化{}个虚拟设备", devices.size());
    }

    /**
     * 发送所有设备数据
     */
    private void sendAllDevicesData() {
        devices.values().forEach(VirtualDevice::sendData);
    }

    /**
     * 虚拟设备（Day2-3：GPS和OBD数据模拟）
     */
    @Data
    static class VirtualDevice {
        private String deviceId;
        private MqttClient mqttClient;
        private Random random = new Random();
        
        // Day2: GPS状态
        private double latitude;
        private double longitude;
        private int speed;
        private int direction;
        
        // Day3: OBD状态
        private int rpm;
        private double fuelLevel;
        private int engineTemp;
        private double mileage;
        
        private SimulatorProperties config;

        public VirtualDevice(String deviceId, SimulatorProperties config) {
            this.deviceId = deviceId;
            this.config = config;
            
            // 初始化GPS位置（随机偏移）
            this.latitude = config.getInitLatitude() + (random.nextDouble() - 0.5) * config.getGpsDriftRange();
            this.longitude = config.getInitLongitude() + (random.nextDouble() - 0.5) * config.getGpsDriftRange();
            this.direction = random.nextInt(360);
            
            // 初始化OBD数据
            this.fuelLevel = 50 + random.nextDouble() * 50;
            this.mileage = random.nextDouble() * 100000;
            this.engineTemp = 80;
        }

        public void connect() {
            try {
                mqttClient = new MqttClient("tcp://localhost:1883", deviceId);
                MqttConnectOptions options = new MqttConnectOptions();
                options.setAutomaticReconnect(true);
                mqttClient.connect(options);
                
                // 发送上线消息
                sendOnlineMessage();
            } catch (Exception e) {
                log.error("设备连接失败: {}", deviceId, e);
            }
        }

        public void disconnect() {
            try {
                if (mqttClient != null && mqttClient.isConnected()) {
                    mqttClient.disconnect();
                    mqttClient.close();
                }
            } catch (Exception e) {
                log.error("设备断开失败: {}", deviceId, e);
            }
        }

        private void sendOnlineMessage() {
            Map<String, Object> msg = new HashMap<>();
            msg.put("status", "online");
            msg.put("ip", "127.0.0.1");
            msg.put("timestamp", System.currentTimeMillis());
            publish("device/" + deviceId + "/status", msg);
        }

        public void sendData() {
            // Day2: 更新GPS（模拟移动）
            updateGPS();
            
            // Day3: 更新OBD
            updateOBD();
            
            // 构建数据消息
            Map<String, Object> data = new HashMap<>();
            data.put("timestamp", System.currentTimeMillis());
            
            // GPS数据
            Map<String, Object> gps = new HashMap<>();
            gps.put("lat", latitude);
            gps.put("lng", longitude);
            gps.put("speed", speed);
            gps.put("direction", direction);
            gps.put("valid", true);
            gps.put("satellites", 8 + random.nextInt(5));
            data.put("gps", gps);
            
            // OBD数据
            Map<String, Object> obd = new HashMap<>();
            obd.put("rpm", rpm);
            obd.put("fuelLevel", fuelLevel);
            obd.put("fuelConsumption", 6 + random.nextDouble() * 8);
            obd.put("engineTemp", engineTemp);
            obd.put("mileage", mileage);
            data.put("obd", obd);
            
            // 状态数据
            Map<String, Object> status = new HashMap<>();
            status.put("batteryVoltage", 12 + random.nextDouble() * 2);
            status.put("signalStrength", 50 + random.nextInt(50));
            data.put("status", status);
            
            // 发送数据
            publish("device/" + deviceId + "/data", data);
        }

        private void updateGPS() {
            // 随机速度
            speed = config.getSpeedMin() + random.nextInt(config.getSpeedMax() - config.getSpeedMin());
            
            // Day2: 根据速度和方向更新位置（简化计算）
            if (speed > 0) {
                double distance = speed / 3600.0 * config.getSendInterval() / 111.0; // km转度
                double radDirection = Math.toRadians(direction);
                latitude += distance * Math.cos(radDirection);
                longitude += distance * Math.sin(radDirection);
                
                // 随机改变方向
                if (random.nextDouble() < 0.3) {
                    direction = (direction + random.nextInt(60) - 30 + 360) % 360;
                }
            }
        }

        private void updateOBD() {
            // Day3: 根据速度更新转速
            rpm = speed > 0 ? config.getRpmMin() + (int)(speed / 120.0 * (config.getRpmMax() - config.getRpmMin())) : 0;
            
            // 油耗随速度变化
            if (speed > 0) {
                fuelLevel -= 0.01 * (1 + speed / 100.0);
                if (fuelLevel < 0) fuelLevel = 100; // 重置
            }
            
            // 温度随转速变化
            engineTemp = 60 + rpm / 100;
            if (engineTemp > 100) engineTemp = 100;
            
            // 里程累加
            mileage += speed / 3600.0 * config.getSendInterval();
        }

        private void publish(String topic, Map<String, Object> payload) {
            try {
                if (mqttClient != null && mqttClient.isConnected()) {
                    String json = JSONUtil.toJsonStr(payload);
                    MqttMessage message = new MqttMessage(json.getBytes());
                    message.setQos(1);
                    mqttClient.publish(topic, message);
                }
            } catch (Exception e) {
                log.error("发送消息失败: deviceId={}, topic={}", deviceId, topic, e);
            }
        }
    }
}

