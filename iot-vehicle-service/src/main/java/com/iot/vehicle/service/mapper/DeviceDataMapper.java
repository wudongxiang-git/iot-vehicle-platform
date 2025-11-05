package com.iot.vehicle.service.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.iot.vehicle.api.entity.DeviceData;
import org.apache.ibatis.annotations.Mapper;

/**
 * 设备数据Mapper
 *
 * @author dongxiang.wu
 */
@Mapper
public interface DeviceDataMapper extends BaseMapper<DeviceData> {
}

