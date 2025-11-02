package com.iot.vehicle.web.controller;

import com.iot.vehicle.api.dto.CreateDeviceDTO;
import com.iot.vehicle.common.core.annotation.RequirePermission;
import com.iot.vehicle.common.core.result.Result;
import com.iot.vehicle.service.service.DeviceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 设备管理接口
 *
 * @author dongxiang.wu
 */
@Tag(name = "设备管理", description = "设备注册、查询、管理等接口")
@RestController
@RequestMapping("/device")
@RequiredArgsConstructor
public class DeviceController {

    private final DeviceService deviceService;

    @Operation(summary = "注册设备", description = "注册新设备并生成设备ID和密钥")
    @PostMapping("/register")
    @RequirePermission("device:add")
    public Result<Map<String, Object>> registerDevice(@Valid @RequestBody CreateDeviceDTO createDeviceDTO) {
        Map<String, Object> result = deviceService.registerDevice(createDeviceDTO);
        return Result.success("设备注册成功", result);
    }

    // TODO: 其他接口待Day2-6实现
}

