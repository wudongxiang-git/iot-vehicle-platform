package com.iot.vehicle.web.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.iot.vehicle.api.dto.CreateRoleDTO;
import com.iot.vehicle.api.entity.SysRole;
import com.iot.vehicle.api.vo.RoleVO;
import com.iot.vehicle.common.core.annotation.RequirePermission;
import com.iot.vehicle.common.core.result.Result;
import com.iot.vehicle.common.mybatis.result.PageResult;
import com.iot.vehicle.service.service.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 角色管理接口
 *
 * @author dongxiang.wu
 */
@Tag(name = "角色管理", description = "角色信息的增删改查接口")
@RestController
@RequestMapping("/role")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    @Operation(summary = "创建角色", description = "创建新的角色")
    @PostMapping
    @RequirePermission("system:role:add")
    public Result<Long> createRole(@Valid @RequestBody CreateRoleDTO createRoleDTO) {
        Long roleId = roleService.createRole(createRoleDTO);
        return Result.success("创建成功", roleId);
    }

    @Operation(summary = "更新角色", description = "更新角色信息")
    @PutMapping("/{roleId}")
    @RequirePermission("system:role:edit")
    public Result<String> updateRole(
            @Parameter(description = "角色ID") @PathVariable("roleId") Long roleId,
            @Valid @RequestBody CreateRoleDTO roleDTO) {
        SysRole role = new SysRole();
        role.setRoleCode(roleDTO.getRoleCode());
        role.setRoleName(roleDTO.getRoleName());
        role.setDescription(roleDTO.getDescription());
        role.setSortOrder(roleDTO.getSortOrder());
        
        roleService.updateRole(roleId, role);
        return Result.success("更新成功");
    }

    @Operation(summary = "删除角色", description = "删除指定角色")
    @DeleteMapping("/{roleId}")
    @RequirePermission("system:role:delete")
    public Result<String> deleteRole(
            @Parameter(description = "角色ID") @PathVariable("roleId") Long roleId) {
        roleService.deleteRole(roleId);
        return Result.success("删除成功");
    }

    @Operation(summary = "查询角色详情", description = "根据ID查询角色详细信息")
    @GetMapping("/{roleId}")
    @RequirePermission("system:role:view")
    public Result<RoleVO> getRoleById(
            @Parameter(description = "角色ID") @PathVariable("roleId") Long roleId) {
        RoleVO roleVO = roleService.getRoleById(roleId);
        return Result.success(roleVO);
    }

    @Operation(summary = "查询所有角色", description = "查询所有可用角色（不分页）")
    @GetMapping("/all")
    public Result<List<RoleVO>> getAllRoles() {
        List<RoleVO> roles = roleService.getAllRoles();
        return Result.success(roles);
    }

    @Operation(summary = "分页查询角色", description = "分页查询角色列表")
    @GetMapping("/page")
    @RequirePermission("system:role:list")
    public Result<PageResult<RoleVO>> getRolePage(
            @Parameter(description = "页码") @RequestParam(value = "current", defaultValue = "1") Long current,
            @Parameter(description = "每页大小") @RequestParam(value = "size", defaultValue = "10") Long size,
            @Parameter(description = "角色名称") @RequestParam(value = "roleName", required = false) String roleName,
            @Parameter(description = "状态") @RequestParam(value = "status", required = false) Integer status) {
        Page<RoleVO> page = roleService.getRolePage(current, size, roleName, status);
        PageResult<RoleVO> pageResult = PageResult.of(page);
        return Result.success(pageResult);
    }

    @Operation(summary = "分配权限", description = "为角色分配权限")
    @PostMapping("/{roleId}/permissions")
    @RequirePermission("system:role:assign")
    public Result<String> assignPermissions(
            @Parameter(description = "角色ID") @PathVariable("roleId") Long roleId,
            @Parameter(description = "权限ID列表") @RequestBody List<Long> permissionIds) {
        roleService.assignPermissions(roleId, permissionIds);
        return Result.success("分配权限成功");
    }

    @Operation(summary = "查询用户角色", description = "查询指定用户的所有角色")
    @GetMapping("/user/{userId}")
    @RequirePermission("system:role:view")
    public Result<List<RoleVO>> getUserRoles(
            @Parameter(description = "用户ID") @PathVariable("userId") Long userId) {
        List<RoleVO> roles = roleService.getRolesByUserId(userId);
        return Result.success(roles);
    }

    @Operation(summary = "分配角色给用户", description = "为用户分配角色")
    @PostMapping("/user/{userId}")
    @RequirePermission("system:role:assign")
    public Result<String> assignRolesToUser(
            @Parameter(description = "用户ID") @PathVariable("userId") Long userId,
            @Parameter(description = "角色ID列表") @RequestBody List<Long> roleIds) {
        roleService.assignRolesToUser(userId, roleIds);
        return Result.success("分配角色成功");
    }
}

