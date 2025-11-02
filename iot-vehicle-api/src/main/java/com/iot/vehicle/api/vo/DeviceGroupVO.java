package com.iot.vehicle.api.vo;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 设备分组视图对象
 *
 * @author dongxiang.wu
 */
@Data
public class DeviceGroupVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 分组ID
     */
    private Long id;

    /**
     * 父分组ID
     */
    private Long parentId;

    /**
     * 分组名称
     */
    private String groupName;

    /**
     * 分组编码
     */
    private String groupCode;

    /**
     * 分组类型
     */
    private String groupType;

    /**
     * 分组描述
     */
    private String description;

    /**
     * 排序
     */
    private Integer sortOrder;

    /**
     * 层级路径
     */
    private String levelPath;

    /**
     * 状态：0-禁用，1-正常
     */
    private Integer status;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 设备数量（实时计算，考虑数据权限）
     * 注意：此字段不存储在数据库中，由Service层查询时动态计算
     */
    private Integer deviceCount;

    /**
     * 在线设备数量（实时计算，考虑数据权限）
     * 注意：此字段不存储在数据库中，由Service层查询时动态计算
     */
    private Integer onlineCount;

    /**
     * 子分组列表（用于树形结构展示）
     */
    private List<DeviceGroupVO> children;
}

