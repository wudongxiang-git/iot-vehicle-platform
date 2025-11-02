package com.iot.vehicle.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.iot.vehicle.api.dto.CreateDeviceDTO;
import com.iot.vehicle.api.entity.Device;
import com.iot.vehicle.api.vo.DeviceVO;
import com.iot.vehicle.common.core.enums.DeviceStatus;
import com.iot.vehicle.common.core.enums.OnlineStatus;
import com.iot.vehicle.common.core.exception.BusinessException;
import com.iot.vehicle.common.core.result.ResultCode;
import com.iot.vehicle.common.core.utils.DeviceIdUtil;
import com.iot.vehicle.service.mapper.DeviceMapper;
import com.iot.vehicle.service.service.DeviceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 设备服务实现
 *
 * @author dongxiang.wu
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DeviceServiceImpl implements DeviceService {

    private final DeviceMapper deviceMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> registerDevice(CreateDeviceDTO createDeviceDTO) {
        // 生成设备ID和密钥
        String deviceId = DeviceIdUtil.generateDeviceId();
        String secretKey = DeviceIdUtil.generateSecretKey();

        // 检查设备序列号是否已存在（如果提供了）
        if (StrUtil.isNotBlank(createDeviceDTO.getDeviceSn())) {
            LambdaQueryWrapper<Device> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Device::getDeviceSn, createDeviceDTO.getDeviceSn());
            Long count = deviceMapper.selectCount(wrapper);
            if (count > 0) {
                throw new BusinessException("设备序列号已存在");
            }
        }

        // 创建设备
        Device device = new Device();
        device.setDeviceId(deviceId);
        device.setDeviceName(createDeviceDTO.getDeviceName());
        device.setDeviceSn(createDeviceDTO.getDeviceSn());
        device.setDeviceType(createDeviceDTO.getDeviceType());
        device.setDeviceModel(createDeviceDTO.getDeviceModel());
        device.setManufacturer(createDeviceDTO.getManufacturer());
        device.setImei(createDeviceDTO.getImei());
        device.setIccid(createDeviceDTO.getIccid());
        device.setGroupId(createDeviceDTO.getGroupId());
        device.setProtocolType(StrUtil.isNotBlank(createDeviceDTO.getProtocolType()) 
                ? createDeviceDTO.getProtocolType() : "MQTT");
        device.setSecretKey(secretKey);
        device.setRemark(createDeviceDTO.getRemark());
        
        // 设置默认状态
        device.setStatus(DeviceStatus.NOT_ACTIVATED.getCode()); // 未激活
        device.setOnlineStatus(OnlineStatus.OFFLINE.getCode()); // 离线
        device.setActivationStatus(0); // 未激活

        // 保存到数据库
        int result = deviceMapper.insert(device);
        if (result <= 0) {
            throw new BusinessException("设备注册失败");
        }

        log.info("设备注册成功: deviceId={}, name={}", deviceId, createDeviceDTO.getDeviceName());

        // 返回设备信息（包含密钥，仅注册时返回）
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("id", device.getId());
        resultMap.put("deviceId", deviceId);
        resultMap.put("deviceName", device.getDeviceName());
        resultMap.put("secretKey", secretKey);  // 密钥仅在注册时返回一次
        resultMap.put("protocolType", device.getProtocolType());

        return resultMap;
    }

    @Override
    public DeviceVO getDeviceById(Long deviceId) {
        // TODO: 实现
        return null;
    }

    @Override
    public DeviceVO getDeviceByDeviceId(String deviceId) {
        // TODO: 实现
        return null;
    }

    @Override
    public Page<DeviceVO> getDevicePage(Long current, Long size, String deviceName, 
                                        String deviceType, Long groupId, Integer status, Integer onlineStatus) {
        // TODO: 实现
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateDevice(Long id, Device device) {
        // TODO: 实现
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteDevice(Long id) {
        // TODO: 实现
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDeleteDevices(Long[] ids) {
        // TODO: 实现
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateDeviceGroup(Long id, Long groupId) {
        // TODO: 实现
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchUpdateDeviceGroup(Long[] ids, Long groupId) {
        // TODO: 实现
    }

    @Override
    public Map<String, Object> getDeviceStatistics() {
        // TODO: 实现
        return null;
    }
}

