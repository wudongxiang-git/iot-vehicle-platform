package com.iot.vehicle.web.controller;

import com.iot.vehicle.common.core.result.Result;
import com.iot.vehicle.common.mybatis.result.PageResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

    @Operation(summary = "测试分页", description = "测试分页返回格式")
    @GetMapping("/test-page")
    public Result<PageResult<Map<String, Object>>> testPage(
            @Parameter(description = "页码") @RequestParam(value = "current", defaultValue = "1") Long current,
            @Parameter(description = "每页大小") @RequestParam(value = "size", defaultValue = "10") Long size) {
        
        // 模拟数据
        List<Map<String, Object>> records = new ArrayList<>();
        for (int i = 1; i <= size; i++) {
            Map<String, Object> item = new HashMap<>();
            item.put("id", (current - 1) * size + i);
            item.put("name", "测试数据 " + i);
            item.put("timestamp", LocalDateTime.now());
            records.add(item);
        }
        
        // 模拟总数
        long total = 100L;
        
        PageResult<Map<String, Object>> pageResult = PageResult.of(records, total, current, size);
        return Result.success(pageResult);
    }
}

