package com.iot.vehicle.service.mqtt;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.iot.vehicle.api.entity.Device;
import com.iot.vehicle.common.core.enums.OnlineStatus;
import com.iot.vehicle.service.mapper.DeviceMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * 设备认证服务
 * 
 * 负责设备的MQTT连接认证
 *
 * @author dongxiang.wu
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DeviceAuthService {

    private final DeviceMapper deviceMapper;

    /**
     * 验证设备（MQTT连接时调用）
     *
     * @param deviceId  设备ID
     * @param secretKey 设备密钥
     * @return true-验证通过，false-验证失败
     */
    public boolean authenticateDevice(String deviceId, String secretKey) {
        // 查询设备
        LambdaQueryWrapper<Device> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Device::getDeviceId, deviceId);
        Device device = deviceMapper.selectOne(wrapper);

        if (device == null) {
            log.warn("设备认证失败：设备不存在, deviceId={}", deviceId);
            return false;
        }

        // 验证密钥
        if (!secretKey.equals(device.getSecretKey())) {
            log.warn("设备认证失败：密钥错误, deviceId={}", deviceId);
            return false;
        }

        // 检查设备状态（已停用或已报废的设备不允许连接）
        if (device.getStatus() == 3 || device.getStatus() == 4) {
            log.warn("设备认证失败：设备已停用或报废, deviceId={}, status={}", deviceId, device.getStatus());
            return false;
        }

        log.info("设备认证成功: deviceId={}", deviceId);
        return true;
    }

    /**
     * 设备上线（MQTT连接成功后调用）
     *
     * @param deviceId 设备ID
     * @param ipAddress IP地址
     */
    public void deviceOnline(String deviceId, String ipAddress) {
        LambdaQueryWrapper<Device> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Device::getDeviceId, deviceId);
        Device device = deviceMapper.selectOne(wrapper);

        if (device == null) {
            log.warn("设备上线失败：设备不存在, deviceId={}", deviceId);
            return;
        }

        // 更新在线状态
        Device updateDevice = new Device();
        updateDevice.setId(device.getId());
        updateDevice.setOnlineStatus(OnlineStatus.ONLINE.getCode());
        updateDevice.setLastOnlineTime(LocalDateTime.now());
        deviceMapper.updateById(updateDevice);

        log.info("设备上线: deviceId={}, ip={}", deviceId, ipAddress);
        
        // TODO: 记录到设备在线日志表
    }

    /**
     * 设备离线（MQTT断开连接后调用）
     *
     * @param deviceId 设备ID
     */
    public void deviceOffline(String deviceId) {
        LambdaQueryWrapper<Device> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Device::getDeviceId, deviceId);
        Device device = deviceMapper.selectOne(wrapper);

        if (device == null) {
            log.warn("设备离线失败：设备不存在, deviceId={}", deviceId);
            return;
        }

        // 更新离线状态
        Device updateDevice = new Device();
        updateDevice.setId(device.getId());
        updateDevice.setOnlineStatus(OnlineStatus.OFFLINE.getCode());
        updateDevice.setLastOfflineTime(LocalDateTime.now());
        deviceMapper.updateById(updateDevice);

        log.info("设备离线: deviceId={}", deviceId);
        
        // TODO: 记录到设备在线日志表
    }
}

