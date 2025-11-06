package com.iot.vehicle.simulator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 设备模拟器启动类
 *
 * @author dongxiang.wu
 */
@SpringBootApplication
public class SimulatorApplication {

    public static void main(String[] args) {
        SpringApplication.run(SimulatorApplication.class, args);
        System.out.println("""
                
                ====================================
                设备数据模拟器启动成功！
                访问 http://localhost:8081/simulator
                ====================================
                """);
    }
}

