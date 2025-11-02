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
        Device device = deviceMapper.selectById(deviceId);
        if (device == null) {
            throw new BusinessException(ResultCode.NOT_FOUND, "设备不存在");
        }
        return convertToVO(device);
    }

    @Override
    public DeviceVO getDeviceByDeviceId(String deviceId) {
        LambdaQueryWrapper<Device> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Device::getDeviceId, deviceId);
        Device device = deviceMapper.selectOne(wrapper);
        if (device == null) {
            throw new BusinessException(ResultCode.NOT_FOUND, "设备不存在");
        }
        return convertToVO(device);
    }

    @Override
    public Page<DeviceVO> getDevicePage(Long current, Long size, String deviceName, 
                                        String deviceType, Long groupId, Integer status, Integer onlineStatus) {
        // 构建查询条件
        LambdaQueryWrapper<Device> wrapper = new LambdaQueryWrapper<>();
        
        // 设备名称模糊查询
        if (StrUtil.isNotBlank(deviceName)) {
            wrapper.like(Device::getDeviceName, deviceName)
                   .or()
                   .like(Device::getDeviceId, deviceName)
                   .or()
                   .like(Device::getDeviceSn, deviceName);
        }
        
        // 设备类型
        if (StrUtil.isNotBlank(deviceType)) {
            wrapper.eq(Device::getDeviceType, deviceType);
        }
        
        // 分组ID
        if (groupId != null) {
            wrapper.eq(Device::getGroupId, groupId);
        }
        
        // 设备状态
        if (status != null) {
            wrapper.eq(Device::getStatus, status);
        }
        
        // 在线状态
        if (onlineStatus != null) {
            wrapper.eq(Device::getOnlineStatus, onlineStatus);
        }
        
        // 按创建时间倒序
        wrapper.orderByDesc(Device::getCreateTime);

        // 分页查询
        Page<Device> page = new Page<>(current, size);
        page = deviceMapper.selectPage(page, wrapper);

        // 转换为VO
        Page<DeviceVO> voPage = new Page<>();
        voPage.setCurrent(page.getCurrent());
        voPage.setSize(page.getSize());
        voPage.setTotal(page.getTotal());
        voPage.setRecords(page.getRecords().stream()
                .map(this::convertToVO)
                .toList());

        return voPage;
    }

    /**
     * 转换为VO
     */
    private DeviceVO convertToVO(Device device) {
        DeviceVO vo = new DeviceVO();
        vo.setId(device.getId());
        vo.setDeviceId(device.getDeviceId());
        vo.setDeviceName(device.getDeviceName());
        vo.setDeviceSn(device.getDeviceSn());
        vo.setDeviceType(device.getDeviceType());
        vo.setDeviceModel(device.getDeviceModel());
        vo.setManufacturer(device.getManufacturer());
        vo.setImei(device.getImei());
        vo.setIccid(device.getIccid());
        vo.setGroupId(device.getGroupId());
        vo.setOwnerId(device.getOwnerId());
        vo.setStatus(device.getStatus());
        vo.setStatusDesc(DeviceStatus.getDescByCode(device.getStatus()));
        vo.setOnlineStatus(device.getOnlineStatus());
        vo.setOnlineStatusDesc(OnlineStatus.getDescByCode(device.getOnlineStatus()));
        vo.setActivationStatus(device.getActivationStatus());
        vo.setProtocolType(device.getProtocolType());
        vo.setFirmwareVersion(device.getFirmwareVersion());
        vo.setHardwareVersion(device.getHardwareVersion());
        vo.setLastOnlineTime(device.getLastOnlineTime());
        vo.setLastOfflineTime(device.getLastOfflineTime());
        vo.setActivationTime(device.getActivationTime());
        vo.setLastLocation(device.getLastLocation());
        vo.setTotalMileage(device.getTotalMileage());
        vo.setTotalRuntime(device.getTotalRuntime());
        vo.setRemark(device.getRemark());
        vo.setCreateTime(device.getCreateTime());
        vo.setUpdateTime(device.getUpdateTime());
        // TODO: 查询分组名称和所有者名称
        return vo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateDevice(Long id, Device device) {
        // 检查设备是否存在
        Device existDevice = deviceMapper.selectById(id);
        if (existDevice == null) {
            throw new BusinessException(ResultCode.NOT_FOUND, "设备不存在");
        }

        // 更新设备信息
        device.setId(id);
        // 不允许修改设备ID、密钥等关键字段
        device.setDeviceId(null);
        device.setSecretKey(null);
        
        int result = deviceMapper.updateById(device);
        if (result <= 0) {
            throw new BusinessException("更新设备失败");
        }

        log.info("更新设备信息: deviceId={}", id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteDevice(Long id) {
        // 逻辑删除
        int result = deviceMapper.deleteById(id);
        if (result <= 0) {
            throw new BusinessException("删除设备失败");
        }

        log.info("删除设备: deviceId={}", id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDeleteDevices(Long[] ids) {
        if (ids == null || ids.length == 0) {
            throw new BusinessException("请选择要删除的设备");
        }

        for (Long id : ids) {
            deviceMapper.deleteById(id);
        }

        log.info("批量删除设备: count={}", ids.length);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateDeviceGroup(Long id, Long groupId) {
        Device device = new Device();
        device.setId(id);
        device.setGroupId(groupId);
        
        int result = deviceMapper.updateById(device);
        if (result <= 0) {
            throw new BusinessException("修改设备分组失败");
        }

        log.info("修改设备分组: deviceId={}, groupId={}", id, groupId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchUpdateDeviceGroup(Long[] ids, Long groupId) {
        if (ids == null || ids.length == 0) {
            throw new BusinessException("请选择要移动的设备");
        }

        for (Long id : ids) {
            Device device = new Device();
            device.setId(id);
            device.setGroupId(groupId);
            deviceMapper.updateById(device);
        }

        log.info("批量修改设备分组: count={}, groupId={}", ids.length, groupId);
    }

    @Override
    public Map<String, Object> getDeviceStatistics() {
        Map<String, Object> stats = new HashMap<>();
        
        // 总设备数
        Long totalCount = deviceMapper.selectCount(null);
        
        // 在线设备数
        LambdaQueryWrapper<Device> onlineWrapper = new LambdaQueryWrapper<>();
        onlineWrapper.eq(Device::getOnlineStatus, OnlineStatus.ONLINE.getCode());
        Long onlineCount = deviceMapper.selectCount(onlineWrapper);
        
        // 离线设备数
        Long offlineCount = totalCount - onlineCount;
        
        // 各状态设备数
        Map<String, Long> statusCount = new HashMap<>();
        for (DeviceStatus deviceStatus : DeviceStatus.values()) {
            LambdaQueryWrapper<Device> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Device::getStatus, deviceStatus.getCode());
            Long count = deviceMapper.selectCount(wrapper);
            statusCount.put(deviceStatus.getDesc(), count);
        }
        
        stats.put("totalCount", totalCount);
        stats.put("onlineCount", onlineCount);
        stats.put("offlineCount", offlineCount);
        stats.put("onlineRate", totalCount > 0 ? String.format("%.2f%%", onlineCount * 100.0 / totalCount) : "0%");
        stats.put("statusCount", statusCount);
        
        return stats;
    }
}

