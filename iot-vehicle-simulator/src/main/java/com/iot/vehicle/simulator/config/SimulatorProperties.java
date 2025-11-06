package com.iot.vehicle.simulator.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 模拟器配置属性
 *
 * @author dongxiang.wu
 */
@Data
@Component
@ConfigurationProperties(prefix = "simulator")
public class SimulatorProperties {

    /**
     * 模拟设备数量
     */
    private Integer deviceCount = 10;

    /**
     * 数据发送间隔（秒）
     */
    private Integer sendInterval = 5;

    /**
     * 是否自动启动
     */
    private Boolean autoStart = false;

    /**
     * 初始纬度
     */
    private Double initLatitude = 31.2304;

    /**
     * 初始经度
     */
    private Double initLongitude = 121.4737;

    /**
     * GPS漂移范围
     */
    private Double gpsDriftRange = 0.01;

    /**
     * 最小速度
     */
    private Integer speedMin = 0;

    /**
     * 最大速度
     */
    private Integer speedMax = 120;

    /**
     * 最小转速
     */
    private Integer rpmMin = 800;

    /**
     * 最大转速
     */
    private Integer rpmMax = 5000;
}

