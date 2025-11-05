package com.iot.vehicle.service.impl;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.iot.vehicle.api.dto.DeviceDataDTO;
import com.iot.vehicle.api.entity.DeviceData;
import com.iot.vehicle.api.entity.DeviceLatestData;
import com.iot.vehicle.common.core.exception.BusinessException;
import com.iot.vehicle.service.mapper.DeviceDataMapper;
import com.iot.vehicle.service.mapper.DeviceLatestDataMapper;
import com.iot.vehicle.service.service.DeviceDataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 设备数据服务实现
 *
 * @author dongxiang.wu
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DeviceDataServiceImpl implements DeviceDataService {

    private final DeviceDataMapper deviceDataMapper;
    private final DeviceLatestDataMapper deviceLatestDataMapper;
    private final RedisTemplate<String, Object> redisTemplate;

    /**
     * Redis key前缀
     */
    private static final String LATEST_DATA_KEY_PREFIX = "device:latest:";

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveDeviceData(String deviceId, DeviceDataDTO dataDTO) {
        // Day4: 数据验证
        if (!validateData(dataDTO)) {
            log.warn("数据验证失败: deviceId={}", deviceId);
            return;
        }

        // Day4: 数据清洗
        DeviceData deviceData = cleanAndConvert(deviceId, dataDTO);

        // Day5: 数据入库（历史数据表）
        try {
            deviceDataMapper.insert(deviceData);
            log.debug("设备数据保存成功: deviceId={}", deviceId);
        } catch (Exception e) {
            log.error("设备数据保存失败: deviceId={}", deviceId, e);
            throw new BusinessException("数据保存失败");
        }

        // 更新最新数据表（持久化最新数据）
        updateLatestDataTable(deviceId, deviceData);

        // Day6: 更新Redis缓存（最新数据）
        cacheLatestData(deviceId, deviceData);
    }

    @Override
    public DeviceData getLatestData(String deviceId) {
        // 先从Redis缓存获取
        String key = LATEST_DATA_KEY_PREFIX + deviceId;
        DeviceData cachedData = (DeviceData) redisTemplate.opsForValue().get(key);
        
        if (cachedData != null) {
            log.debug("从缓存获取最新数据: deviceId={}", deviceId);
            return cachedData;
        }

        // 缓存未命中，从数据库查询
        LambdaQueryWrapper<DeviceData> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DeviceData::getDeviceId, deviceId)
               .orderByDesc(DeviceData::getDataTime)
               .last("LIMIT 1");
        
        DeviceData latestData = deviceDataMapper.selectOne(wrapper);
        
        if (latestData != null) {
            // 更新缓存
            cacheLatestData(deviceId, latestData);
        }
        
        return latestData;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchSaveDeviceData(List<DeviceData> dataList) {
        if (dataList == null || dataList.isEmpty()) {
            return;
        }

        // 批量插入（MyBatis-Plus的批量插入）
        for (DeviceData data : dataList) {
            deviceDataMapper.insert(data);
        }

        log.info("批量保存设备数据: count={}", dataList.size());
    }

    /**
     * Day4: 验证数据
     */
    private boolean validateData(DeviceDataDTO dataDTO) {
        if (dataDTO == null) {
            return false;
        }

        // 验证时间戳
        if (dataDTO.getTimestamp() == null || dataDTO.getTimestamp() <= 0) {
            log.warn("时间戳无效");
            return false;
        }

        // 验证GPS数据（如果有）
        if (dataDTO.getGps() != null) {
            DeviceDataDTO.GpsData gps = dataDTO.getGps();
            
            // 验证经纬度范围
            if (gps.getLat() != null && (gps.getLat().doubleValue() < -90 || gps.getLat().doubleValue() > 90)) {
                log.warn("纬度超出范围: {}", gps.getLat());
                return false;
            }
            if (gps.getLng() != null && (gps.getLng().doubleValue() < -180 || gps.getLng().doubleValue() > 180)) {
                log.warn("经度超出范围: {}", gps.getLng());
                return false;
            }

            // 验证速度范围（0-300 km/h）
            if (gps.getSpeed() != null && (gps.getSpeed().doubleValue() < 0 || gps.getSpeed().doubleValue() > 300)) {
                log.warn("速度超出合理范围: {}", gps.getSpeed());
                return false;
            }
        }

        // 验证OBD数据（如果有）
        if (dataDTO.getObd() != null) {
            DeviceDataDTO.ObdData obd = dataDTO.getObd();
            
            // 验证转速范围（0-10000 RPM）
            if (obd.getRpm() != null && (obd.getRpm() < 0 || obd.getRpm() > 10000)) {
                log.warn("转速超出范围: {}", obd.getRpm());
                return false;
            }

            // 验证油量范围（0-100%）
            if (obd.getFuelLevel() != null && (obd.getFuelLevel().doubleValue() < 0 || obd.getFuelLevel().doubleValue() > 100)) {
                log.warn("油量超出范围: {}", obd.getFuelLevel());
                return false;
            }
        }

        return true;
    }

    /**
     * Day4: 数据清洗和转换
     */
    private DeviceData cleanAndConvert(String deviceId, DeviceDataDTO dataDTO) {
        DeviceData deviceData = new DeviceData();
        
        // 设备ID和时间
        deviceData.setDeviceId(deviceId);
        deviceData.setDataTime(LocalDateTime.ofInstant(
                Instant.ofEpochMilli(dataDTO.getTimestamp()), 
                ZoneId.systemDefault()));

        // GPS数据
        if (dataDTO.getGps() != null) {
            DeviceDataDTO.GpsData gps = dataDTO.getGps();
            deviceData.setLatitude(gps.getLat());
            deviceData.setLongitude(gps.getLng());
            deviceData.setAltitude(gps.getAltitude());
            deviceData.setSpeed(gps.getSpeed());
            deviceData.setDirection(gps.getDirection());
            deviceData.setGpsValid(gps.getValid() != null ? gps.getValid() : false);
            deviceData.setSatelliteCount(gps.getSatellites());
        }

        // OBD数据
        if (dataDTO.getObd() != null) {
            DeviceDataDTO.ObdData obd = dataDTO.getObd();
            deviceData.setSpeedRpm(obd.getRpm());
            deviceData.setFuelLevel(obd.getFuelLevel());
            deviceData.setFuelConsumption(obd.getFuelConsumption());
            deviceData.setEngineTemp(obd.getEngineTemp());
            deviceData.setMileage(obd.getMileage());
        }

        // 状态数据
        if (dataDTO.getStatus() != null) {
            DeviceDataDTO.StatusData status = dataDTO.getStatus();
            deviceData.setBatteryVoltage(status.getBatteryVoltage());
            deviceData.setSignalStrength(status.getSignalStrength());
        }

        // 数据状态默认为正常
        deviceData.setDataStatus(0);

        // 保存原始数据（方便后续分析）
        deviceData.setRawData(JSON.toJSONString(dataDTO));

        return deviceData;
    }

    /**
     * 更新最新数据表（使用UPSERT逻辑）
     */
    private void updateLatestDataTable(String deviceId, DeviceData deviceData) {
        try {
            // 转换为最新数据实体
            DeviceLatestData latestData = new DeviceLatestData();
            latestData.setDeviceId(deviceId);
            latestData.setDataTime(deviceData.getDataTime());
            latestData.setLatitude(deviceData.getLatitude());
            latestData.setLongitude(deviceData.getLongitude());
            latestData.setSpeed(deviceData.getSpeed());
            latestData.setDirection(deviceData.getDirection());
            latestData.setGpsValid(deviceData.getGpsValid());
            latestData.setSpeedRpm(deviceData.getSpeedRpm());
            latestData.setFuelLevel(deviceData.getFuelLevel());
            latestData.setEngineTemp(deviceData.getEngineTemp());
            latestData.setBatteryVoltage(deviceData.getBatteryVoltage());
            latestData.setMileage(deviceData.getMileage());
            latestData.setSignalStrength(deviceData.getSignalStrength());
            latestData.setUpdateTime(LocalDateTime.now());

            // 检查是否已存在
            DeviceLatestData existData = deviceLatestDataMapper.selectById(deviceId);
            if (existData == null) {
                // 新增
                deviceLatestDataMapper.insert(latestData);
            } else {
                // 更新
                deviceLatestDataMapper.updateById(latestData);
            }
            
            log.debug("更新最新数据表成功: deviceId={}", deviceId);
        } catch (Exception e) {
            log.error("更新最新数据表失败: deviceId={}", deviceId, e);
            // 更新失败不影响主流程
        }
    }

    /**
     * Day6: 缓存最新数据到Redis
     */
    private void cacheLatestData(String deviceId, DeviceData deviceData) {
        try {
            String key = LATEST_DATA_KEY_PREFIX + deviceId;
            redisTemplate.opsForValue().set(key, deviceData, 24, TimeUnit.HOURS);
            log.debug("缓存最新数据: deviceId={}", deviceId);
        } catch (Exception e) {
            log.error("缓存数据失败: deviceId={}", deviceId, e);
            // 缓存失败不影响主流程
        }
    }
}

