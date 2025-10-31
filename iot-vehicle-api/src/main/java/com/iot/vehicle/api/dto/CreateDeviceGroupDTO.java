package com.iot.vehicle.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serializable;

/**
 * 创建设备分组DTO
 *
 * @author dongxiang.wu
 */
@Data
public class CreateDeviceGroupDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 父分组ID（0表示顶级分组）
     */
    private Long parentId = 0L;

    /**
     * 分组名称
     */
    @NotBlank(message = "分组名称不能为空")
    @Size(max = 100, message = "分组名称长度不能超过100个字符")
    private String groupName;

    /**
     * 分组编码
     */
    @Size(max = 50, message = "分组编码长度不能超过50个字符")
    private String groupCode;

    /**
     * 分组类型
     */
    private String groupType;

    /**
     * 分组描述
     */
    @Size(max = 200, message = "分组描述长度不能超过200个字符")
    private String description;

    /**
     * 排序
     */
    private Integer sortOrder;
}

