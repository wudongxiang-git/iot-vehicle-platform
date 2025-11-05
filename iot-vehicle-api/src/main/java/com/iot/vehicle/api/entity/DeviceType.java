package com.iot.vehicle.api.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.iot.vehicle.common.mybatis.handler.JsonbTypeHandler;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 设备类型实体
 *
 * @author dongxiang.wu
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("tb_device_type")
public class DeviceType extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 类型编码（唯一）
     */
    @TableField("type_code")
    private String typeCode;

    /**
     * 类型名称
     */
    @TableField("type_name")
    private String typeName;

    /**
     * 类型分类
     */
    @TableField("category")
    private String category;

    /**
     * 类型描述
     */
    @TableField("description")
    private String description;

    /**
     * 图标
     */
    @TableField("icon")
    private String icon;

    /**
     * 类型属性配置（JSON格式）
     */
    @TableField(value = "properties", typeHandler = JsonbTypeHandler.class)
    private String properties;

    /**
     * 排序
     */
    @TableField("sort_order")
    private Integer sortOrder;

    /**
     * 状态：0-禁用，1-正常
     */
    @TableField("status")
    private Integer status;
}

