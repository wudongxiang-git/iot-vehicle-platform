package com.iot.vehicle.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serializable;

/**
 * 创建角色DTO
 *
 * @author dongxiang.wu
 */
@Data
public class CreateRoleDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 角色编码
     */
    @NotBlank(message = "角色编码不能为空")
    @Pattern(regexp = "^ROLE_[A-Z_]+$", message = "角色编码必须以ROLE_开头，只能包含大写字母和下划线")
    @Size(max = 50, message = "角色编码长度不能超过50个字符")
    private String roleCode;

    /**
     * 角色名称
     */
    @NotBlank(message = "角色名称不能为空")
    @Size(max = 50, message = "角色名称长度不能超过50个字符")
    private String roleName;

    /**
     * 角色描述
     */
    @Size(max = 200, message = "角色描述长度不能超过200个字符")
    private String description;

    /**
     * 排序
     */
    private Integer sortOrder;
}

