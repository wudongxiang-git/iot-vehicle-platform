package com.iot.vehicle.web;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * IoT Vehicle Platform Application
 * 车联网数据管理平台
 *
 * @author dongxiang.wu
 * @version 1.0.0
 */
@SpringBootApplication(scanBasePackages = {
        "com.iot.vehicle.web",
        "com.iot.vehicle.service",
        "com.iot.vehicle.common.web"
})
public class IotVehicleApplication {

    public static void main(String[] args) {
        SpringApplication.run(IotVehicleApplication.class, args);
        System.out.println("""
                
                ====================================
                IoT Vehicle Platform Started Successfully!
                车联网数据管理平台 启动成功！
                ====================================
                """);
    }
}

