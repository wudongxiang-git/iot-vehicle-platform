package com.iot.vehicle.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.iot.vehicle.api.dto.CreateRoleDTO;
import com.iot.vehicle.api.entity.SysRole;
import com.iot.vehicle.api.entity.SysRolePermission;
import com.iot.vehicle.api.entity.SysUserRole;
import com.iot.vehicle.api.vo.RoleVO;
import com.iot.vehicle.common.core.exception.BusinessException;
import com.iot.vehicle.common.core.result.ResultCode;
import com.iot.vehicle.service.mapper.SysRoleMapper;
import com.iot.vehicle.service.service.RoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 角色服务实现
 *
 * @author dongxiang.wu
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final SysRoleMapper sysRoleMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createRole(CreateRoleDTO createRoleDTO) {
        // 检查角色编码是否已存在
        LambdaQueryWrapper<SysRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysRole::getRoleCode, createRoleDTO.getRoleCode());
        Long count = sysRoleMapper.selectCount(wrapper);
        if (count > 0) {
            throw new BusinessException("角色编码已存在");
        }

        // 创建角色
        SysRole role = new SysRole();
        role.setRoleCode(createRoleDTO.getRoleCode());
        role.setRoleName(createRoleDTO.getRoleName());
        role.setDescription(createRoleDTO.getDescription());
        role.setSortOrder(createRoleDTO.getSortOrder() != null ? createRoleDTO.getSortOrder() : 0);
        role.setStatus(1);

        int result = sysRoleMapper.insert(role);
        if (result <= 0) {
            throw new BusinessException("创建角色失败");
        }

        log.info("创建角色成功: roleCode={}, roleId={}", role.getRoleCode(), role.getId());
        return role.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateRole(Long roleId, SysRole role) {
        // 检查角色是否存在
        SysRole existRole = sysRoleMapper.selectById(roleId);
        if (existRole == null) {
            throw new BusinessException(ResultCode.NOT_FOUND, "角色不存在");
        }

        // 检查是否为系统预置角色（不允许修改编码）
        if (roleId <= 3 && role.getRoleCode() != null && !role.getRoleCode().equals(existRole.getRoleCode())) {
            throw new BusinessException(ResultCode.FORBIDDEN, "系统预置角色不允许修改角色编码");
        }

        // 检查角色编码是否被占用
        if (StrUtil.isNotBlank(role.getRoleCode()) && !role.getRoleCode().equals(existRole.getRoleCode())) {
            LambdaQueryWrapper<SysRole> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(SysRole::getRoleCode, role.getRoleCode());
            Long count = sysRoleMapper.selectCount(wrapper);
            if (count > 0) {
                throw new BusinessException("角色编码已存在");
            }
        }

        // 更新角色
        role.setId(roleId);
        int result = sysRoleMapper.updateById(role);
        if (result <= 0) {
            throw new BusinessException("更新角色失败");
        }

        log.info("更新角色: roleId={}", roleId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteRole(Long roleId) {
        // 检查是否为系统预置角色
        if (roleId <= 3) {
            throw new BusinessException(ResultCode.FORBIDDEN, "系统预置角色不允许删除");
        }

        // TODO: 检查是否有用户关联此角色

        // 逻辑删除
        int result = sysRoleMapper.deleteById(roleId);
        if (result <= 0) {
            throw new BusinessException("删除角色失败");
        }

        log.info("删除角色: roleId={}", roleId);
    }

    @Override
    public RoleVO getRoleById(Long roleId) {
        SysRole role = sysRoleMapper.selectById(roleId);
        if (role == null) {
            throw new BusinessException(ResultCode.NOT_FOUND, "角色不存在");
        }
        return convertToVO(role);
    }

    @Override
    public List<RoleVO> getAllRoles() {
        LambdaQueryWrapper<SysRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysRole::getStatus, 1);
        wrapper.orderByAsc(SysRole::getSortOrder);
        
        List<SysRole> roles = sysRoleMapper.selectList(wrapper);
        return roles.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }

    @Override
    public Page<RoleVO> getRolePage(Long current, Long size, String roleName, Integer status) {
        // 构建查询条件
        LambdaQueryWrapper<SysRole> wrapper = new LambdaQueryWrapper<>();
        
        // 角色名称模糊查询
        if (StrUtil.isNotBlank(roleName)) {
            wrapper.like(SysRole::getRoleName, roleName)
                   .or()
                   .like(SysRole::getRoleCode, roleName);
        }
        
        // 状态查询
        if (status != null) {
            wrapper.eq(SysRole::getStatus, status);
        }
        
        // 按排序字段排序
        wrapper.orderByAsc(SysRole::getSortOrder);

        // 分页查询
        Page<SysRole> page = new Page<>(current, size);
        page = sysRoleMapper.selectPage(page, wrapper);

        // 转换为VO
        Page<RoleVO> voPage = new Page<>();
        voPage.setCurrent(page.getCurrent());
        voPage.setSize(page.getSize());
        voPage.setTotal(page.getTotal());
        voPage.setRecords(page.getRecords().stream()
                .map(this::convertToVO)
                .toList());

        return voPage;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void assignPermissions(Long roleId, List<Long> permissionIds) {
        // TODO: 实现角色权限分配（需要SysRolePermissionMapper）
        log.info("为角色分配权限（待完善）: roleId={}, permissionIds={}", roleId, permissionIds);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void assignRolesToUser(Long userId, List<Long> roleIds) {
        // TODO: 实现用户角色分配（需要SysUserRoleMapper）
        log.info("为用户分配角色（待完善）: userId={}, roleIds={}", userId, roleIds);
    }

    @Override
    public List<RoleVO> getRolesByUserId(Long userId) {
        List<SysRole> roles = sysRoleMapper.selectRolesByUserId(userId);
        return roles.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }

    /**
     * 转换为VO
     */
    private RoleVO convertToVO(SysRole role) {
        RoleVO roleVO = new RoleVO();
        roleVO.setId(role.getId());
        roleVO.setRoleCode(role.getRoleCode());
        roleVO.setRoleName(role.getRoleName());
        roleVO.setDescription(role.getDescription());
        roleVO.setSortOrder(role.getSortOrder());
        roleVO.setStatus(role.getStatus());
        roleVO.setCreateTime(role.getCreateTime());
        return roleVO;
    }
}

