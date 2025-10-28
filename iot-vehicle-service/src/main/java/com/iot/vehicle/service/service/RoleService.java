package com.iot.vehicle.service.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.iot.vehicle.api.dto.CreateRoleDTO;
import com.iot.vehicle.api.entity.SysRole;
import com.iot.vehicle.api.vo.RoleVO;

import java.util.List;

/**
 * 角色服务接口
 *
 * @author dongxiang.wu
 */
public interface RoleService {

    /**
     * 创建角色
     *
     * @param createRoleDTO 角色信息
     * @return 角色ID
     */
    Long createRole(CreateRoleDTO createRoleDTO);

    /**
     * 更新角色
     *
     * @param roleId 角色ID
     * @param role   角色信息
     */
    void updateRole(Long roleId, SysRole role);

    /**
     * 删除角色
     *
     * @param roleId 角色ID
     */
    void deleteRole(Long roleId);

    /**
     * 根据ID查询角色
     *
     * @param roleId 角色ID
     * @return 角色信息
     */
    RoleVO getRoleById(Long roleId);

    /**
     * 查询所有角色
     *
     * @return 角色列表
     */
    List<RoleVO> getAllRoles();

    /**
     * 分页查询角色列表
     *
     * @param current  当前页
     * @param size     每页大小
     * @param roleName 角色名称（模糊查询，可选）
     * @param status   状态（可选）
     * @return 角色分页列表
     */
    Page<RoleVO> getRolePage(Long current, Long size, String roleName, Integer status);

    /**
     * 为角色分配权限
     *
     * @param roleId        角色ID
     * @param permissionIds 权限ID列表
     */
    void assignPermissions(Long roleId, List<Long> permissionIds);

    /**
     * 为用户分配角色
     *
     * @param userId  用户ID
     * @param roleIds 角色ID列表
     */
    void assignRolesToUser(Long userId, List<Long> roleIds);

    /**
     * 根据用户ID查询角色列表
     *
     * @param userId 用户ID
     * @return 角色列表
     */
    List<RoleVO> getRolesByUserId(Long userId);
}

