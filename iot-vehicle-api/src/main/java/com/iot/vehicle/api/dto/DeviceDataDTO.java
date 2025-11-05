package com.iot.vehicle.api.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 设备数据上报DTO
 * 
 * 设备通过MQTT上报数据时使用此格式
 *
 * @author dongxiang.wu
 */
@Data
public class DeviceDataDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 数据时间戳（毫秒）
     */
    private Long timestamp;

    /**
     * GPS数据
     */
    private GpsData gps;

    /**
     * OBD数据
     */
    private ObdData obd;

    /**
     * 状态数据
     */
    private StatusData status;

    /**
     * GPS数据
     */
    @Data
    public static class GpsData {
        /**
         * 纬度
         */
        private BigDecimal lat;

        /**
         * 经度
         */
        private BigDecimal lng;

        /**
         * 海拔（米）
         */
        private BigDecimal altitude;

        /**
         * 速度（km/h）
         */
        private BigDecimal speed;

        /**
         * 方向（0-360度）
         */
        private Integer direction;

        /**
         * GPS是否有效
         */
        private Boolean valid;

        /**
         * 卫星数量
         */
        private Integer satellites;
    }

    /**
     * OBD数据
     */
    @Data
    public static class ObdData {
        /**
         * 发动机转速（RPM）
         */
        private Integer rpm;

        /**
         * 油量（%）
         */
        private BigDecimal fuelLevel;

        /**
         * 瞬时油耗（L/100km）
         */
        private BigDecimal fuelConsumption;

        /**
         * 发动机温度（℃）
         */
        private Integer engineTemp;

        /**
         * 里程（km）
         */
        private BigDecimal mileage;
    }

    /**
     * 状态数据
     */
    @Data
    public static class StatusData {
        /**
         * 电池电压（V）
         */
        private BigDecimal batteryVoltage;

        /**
         * 信号强度（0-100）
         */
        private Integer signalStrength;
    }
}

