package com.iot.vehicle.service.service;

import com.iot.vehicle.api.dto.DeviceDataDTO;
import com.iot.vehicle.api.entity.DeviceData;

/**
 * 设备数据服务接口
 *
 * @author dongxiang.wu
 */
public interface DeviceDataService {

    /**
     * 保存设备数据
     *
     * @param deviceId 设备ID
     * @param dataDTO  数据DTO
     */
    void saveDeviceData(String deviceId, DeviceDataDTO dataDTO);

    /**
     * 获取设备最新数据
     *
     * @param deviceId 设备ID
     * @return 最新数据
     */
    DeviceData getLatestData(String deviceId);

    /**
     * 批量保存设备数据（性能优化）
     *
     * @param dataList 数据列表
     */
    void batchSaveDeviceData(java.util.List<DeviceData> dataList);
}

