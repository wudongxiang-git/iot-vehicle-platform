package com.iot.vehicle.api.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 设备分组实体
 *
 * @author dongxiang.wu
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("tb_device_group")
public class DeviceGroup extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 父分组ID（0表示顶级分组）
     */
    @TableField("parent_id")
    private Long parentId;

    /**
     * 分组名称
     */
    @TableField("group_name")
    private String groupName;

    /**
     * 分组编码（唯一）
     */
    @TableField("group_code")
    private String groupCode;

    /**
     * 分组类型（department/region/fleet等）
     */
    @TableField("group_type")
    private String groupType;

    /**
     * 分组描述
     */
    @TableField("description")
    private String description;

    /**
     * 排序
     */
    @TableField("sort_order")
    private Integer sortOrder;

    /**
     * 层级路径（如：0/1/3）
     */
    @TableField("level_path")
    private String levelPath;

    /**
     * 设备数量
     */
    @TableField("device_count")
    private Integer deviceCount;

    /**
     * 在线设备数量
     */
    @TableField("online_count")
    private Integer onlineCount;

    /**
     * 状态：0-禁用，1-正常
     */
    @TableField("status")
    private Integer status;
}

