package com.iot.vehicle.web.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.iot.vehicle.api.dto.CreateDeviceDTO;
import com.iot.vehicle.api.dto.UpdateDeviceDTO;
import com.iot.vehicle.api.entity.Device;
import com.iot.vehicle.api.vo.DeviceVO;
import com.iot.vehicle.common.core.annotation.RequirePermission;
import com.iot.vehicle.common.core.result.Result;
import com.iot.vehicle.common.mybatis.result.PageResult;
import com.iot.vehicle.service.service.DeviceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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

    @Operation(summary = "分页查询设备列表", description = "支持按设备名称、类型、分组、状态筛选")
    @GetMapping("/page")
    @RequirePermission("device:list")
    public Result<PageResult<DeviceVO>> getDevicePage(
            @Parameter(description = "页码") @RequestParam(value = "current", defaultValue = "1") Long current,
            @Parameter(description = "每页大小") @RequestParam(value = "size", defaultValue = "10") Long size,
            @Parameter(description = "设备名称/ID/序列号") @RequestParam(value = "deviceName", required = false) String deviceName,
            @Parameter(description = "设备类型") @RequestParam(value = "deviceType", required = false) String deviceType,
            @Parameter(description = "分组ID") @RequestParam(value = "groupId", required = false) Long groupId,
            @Parameter(description = "设备状态") @RequestParam(value = "status", required = false) Integer status,
            @Parameter(description = "在线状态") @RequestParam(value = "onlineStatus", required = false) Integer onlineStatus) {
        
        Page<DeviceVO> page = deviceService.getDevicePage(current, size, deviceName, 
                deviceType, groupId, status, onlineStatus);
        PageResult<DeviceVO> pageResult = PageResult.of(page);
        return Result.success(pageResult);
    }

    @Operation(summary = "根据ID查询设备详情", description = "查询指定设备的详细信息")
    @GetMapping("/{id}")
    @RequirePermission("device:view")
    public Result<DeviceVO> getDeviceById(
            @Parameter(description = "设备ID") @PathVariable("id") Long id) {
        DeviceVO deviceVO = deviceService.getDeviceById(id);
        return Result.success(deviceVO);
    }

    @Operation(summary = "根据设备业务ID查询", description = "根据设备业务ID查询设备信息")
    @GetMapping("/by-device-id/{deviceId}")
    @RequirePermission("device:view")
    public Result<DeviceVO> getByDeviceId(
            @Parameter(description = "设备业务ID") @PathVariable("deviceId") String deviceId) {
        DeviceVO deviceVO = deviceService.getDeviceByDeviceId(deviceId);
        return Result.success(deviceVO);
    }

    @Operation(summary = "更新设备信息", description = "更新设备的基本信息")
    @PutMapping("/{id}")
    @RequirePermission("device:edit")
    public Result<String> updateDevice(
            @Parameter(description = "设备ID") @PathVariable("id") Long id,
            @Valid @RequestBody UpdateDeviceDTO updateDeviceDTO) {
        
        Device device = new Device();
        device.setDeviceName(updateDeviceDTO.getDeviceName());
        device.setDeviceModel(updateDeviceDTO.getDeviceModel());
        device.setGroupId(updateDeviceDTO.getGroupId());
        device.setFirmwareVersion(updateDeviceDTO.getFirmwareVersion());
        device.setHardwareVersion(updateDeviceDTO.getHardwareVersion());
        device.setRemark(updateDeviceDTO.getRemark());
        
        deviceService.updateDevice(id, device);
        return Result.success("更新成功");
    }

    @Operation(summary = "删除设备", description = "逻辑删除指定设备")
    @DeleteMapping("/{id}")
    @RequirePermission("device:delete")
    public Result<String> deleteDevice(
            @Parameter(description = "设备ID") @PathVariable("id") Long id) {
        deviceService.deleteDevice(id);
        return Result.success("删除成功");
    }

    @Operation(summary = "修改设备分组", description = "将设备移动到指定分组")
    @PutMapping("/{id}/group")
    @RequirePermission("device:edit")
    public Result<String> updateDeviceGroup(
            @Parameter(description = "设备ID") @PathVariable("id") Long id,
            @Parameter(description = "分组ID") @RequestParam(value = "groupId") Long groupId) {
        deviceService.updateDeviceGroup(id, groupId);
        return Result.success("修改分组成功");
    }

    @Operation(summary = "批量删除设备", description = "批量逻辑删除设备")
    @DeleteMapping("/batch")
    @RequirePermission("device:delete")
    public Result<String> batchDeleteDevices(
            @Parameter(description = "设备ID列表") @RequestBody Long[] ids) {
        deviceService.batchDeleteDevices(ids);
        return Result.success("批量删除成功");
    }

    @Operation(summary = "批量修改设备分组", description = "批量将设备移动到指定分组")
    @PutMapping("/batch/group")
    @RequirePermission("device:edit")
    public Result<String> batchUpdateDeviceGroup(
            @Parameter(description = "设备ID列表") @RequestBody Long[] ids,
            @Parameter(description = "分组ID") @RequestParam(value = "groupId") Long groupId) {
        deviceService.batchUpdateDeviceGroup(ids, groupId);
        return Result.success("批量修改分组成功");
    }

    @Operation(summary = "获取设备统计信息", description = "统计设备总数、在线数、各状态数量等")
    @GetMapping("/statistics")
    @RequirePermission("device:view")
    public Result<Map<String, Object>> getStatistics() {
        Map<String, Object> statistics = deviceService.getDeviceStatistics();
        return Result.success(statistics);
    }
}

