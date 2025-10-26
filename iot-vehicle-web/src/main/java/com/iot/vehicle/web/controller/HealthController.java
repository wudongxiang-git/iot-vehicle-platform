package com.iot.vehicle.web.controller;

import com.iot.vehicle.common.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 健康检查接口
 *
 * @author dongxiang.wu
 */
@Tag(name = "健康检查", description = "系统健康检查接口")
@RestController
@RequestMapping("/health")
public class HealthController {

    @Operation(summary = "健康检查", description = "检查系统是否正常运行")
    @GetMapping
    public Result<Map<String, Object>> health() {
        Map<String, Object> data = new HashMap<>();
        data.put("status", "UP");
        data.put("timestamp", LocalDateTime.now());
        data.put("application", "IoT Vehicle Platform");
        data.put("version", "1.0.0");
        return Result.success(data);
    }

    @Operation(summary = "测试接口", description = "测试统一返回格式")
    @GetMapping("/test")
    public Result<String> test() {
        return Result.success("系统运行正常！");
    }
}

