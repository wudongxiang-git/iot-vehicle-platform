package com.iot.vehicle.service.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.iot.vehicle.api.entity.SysUser;
import org.apache.ibatis.annotations.Mapper;

/**
 * 系统用户Mapper
 *
 * @author dongxiang.wu
 */
@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {
}

