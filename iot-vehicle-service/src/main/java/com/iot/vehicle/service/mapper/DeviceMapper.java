package com.iot.vehicle.service.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.iot.vehicle.api.entity.Device;
import org.apache.ibatis.annotations.Mapper;

/**
 * 设备Mapper
 *
 * @author dongxiang.wu
 */
@Mapper
public interface DeviceMapper extends BaseMapper<Device> {
}

