package com.iot.vehicle.service.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.iot.vehicle.api.dto.CreateDeviceDTO;
import com.iot.vehicle.api.entity.Device;
import com.iot.vehicle.api.vo.DeviceVO;

import java.util.Map;

/**
 * 设备服务接口
 *
 * @author dongxiang.wu
 */
public interface DeviceService {

    /**
     * 注册设备
     * 
     * @param createDeviceDTO 设备信息
     * @return 设备信息（包含设备ID和密钥）
     */
    Map<String, Object> registerDevice(CreateDeviceDTO createDeviceDTO);

    /**
     * 根据ID查询设备
     *
     * @param deviceId 设备ID（主键）
     * @return 设备信息
     */
    DeviceVO getDeviceById(Long deviceId);

    /**
     * 根据设备业务ID查询
     *
     * @param deviceId 设备业务ID
     * @return 设备信息
     */
    DeviceVO getDeviceByDeviceId(String deviceId);

    /**
     * 分页查询设备列表
     *
     * @param current    当前页
     * @param size       每页大小
     * @param deviceName 设备名称（模糊查询）
     * @param deviceType 设备类型
     * @param groupId    分组ID
     * @param status     设备状态
     * @param onlineStatus 在线状态
     * @return 设备分页列表
     */
    Page<DeviceVO> getDevicePage(Long current, Long size, String deviceName, 
                                  String deviceType, Long groupId, Integer status, Integer onlineStatus);

    /**
     * 更新设备信息
     *
     * @param id     设备ID
     * @param device 设备信息
     */
    void updateDevice(Long id, Device device);

    /**
     * 删除设备
     *
     * @param id 设备ID
     */
    void deleteDevice(Long id);

    /**
     * 批量删除设备
     *
     * @param ids 设备ID列表
     */
    void batchDeleteDevices(Long[] ids);

    /**
     * 修改设备分组
     *
     * @param id      设备ID
     * @param groupId 分组ID
     */
    void updateDeviceGroup(Long id, Long groupId);

    /**
     * 批量修改设备分组
     *
     * @param ids     设备ID列表
     * @param groupId 分组ID
     */
    void batchUpdateDeviceGroup(Long[] ids, Long groupId);

    /**
     * 获取设备统计信息
     *
     * @return 统计信息
     */
    Map<String, Object> getDeviceStatistics();
}

