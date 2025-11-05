package com.iot.vehicle.service.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.iot.vehicle.api.entity.DeviceLatestData;
import org.apache.ibatis.annotations.Mapper;

/**
 * 设备最新数据Mapper
 *
 * @author dongxiang.wu
 */
@Mapper
public interface DeviceLatestDataMapper extends BaseMapper<DeviceLatestData> {
}

