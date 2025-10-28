package com.iot.vehicle.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.iot.vehicle.api.entity.SysUser;
import com.iot.vehicle.api.vo.UserVO;
import com.iot.vehicle.common.core.exception.BusinessException;
import com.iot.vehicle.common.core.result.ResultCode;
import com.iot.vehicle.common.core.utils.PasswordUtil;
import com.iot.vehicle.service.mapper.SysUserMapper;
import com.iot.vehicle.service.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 用户服务实现
 *
 * @author dongxiang.wu
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final SysUserMapper sysUserMapper;

    @Override
    public UserVO getUserById(Long userId) {
        SysUser user = sysUserMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException(ResultCode.NOT_FOUND, "用户不存在");
        }
        return convertToVO(user);
    }

    @Override
    public UserVO getUserByUsername(String username) {
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUser::getUsername, username);
        SysUser user = sysUserMapper.selectOne(wrapper);
        if (user == null) {
            throw new BusinessException(ResultCode.NOT_FOUND, "用户不存在");
        }
        return convertToVO(user);
    }

    @Override
    public Page<UserVO> getUserPage(Long current, Long size, String username, Integer status) {
        // 构建查询条件
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        
        // 用户名模糊查询
        if (StrUtil.isNotBlank(username)) {
            wrapper.like(SysUser::getUsername, username)
                   .or()
                   .like(SysUser::getNickname, username);
        }
        
        // 状态查询
        if (status != null) {
            wrapper.eq(SysUser::getStatus, status);
        }
        
        // 按创建时间倒序
        wrapper.orderByDesc(SysUser::getCreateTime);

        // 分页查询
        Page<SysUser> page = new Page<>(current, size);
        page = sysUserMapper.selectPage(page, wrapper);

        // 转换为VO
        Page<UserVO> voPage = new Page<>();
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
    public void updateUser(Long userId, SysUser user) {
        // 检查用户是否存在
        SysUser existUser = sysUserMapper.selectById(userId);
        if (existUser == null) {
            throw new BusinessException(ResultCode.NOT_FOUND, "用户不存在");
        }

        // 检查用户名是否被占用
        if (StrUtil.isNotBlank(user.getUsername()) && !user.getUsername().equals(existUser.getUsername())) {
            LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(SysUser::getUsername, user.getUsername());
            Long count = sysUserMapper.selectCount(wrapper);
            if (count > 0) {
                throw new BusinessException("用户名已存在");
            }
        }

        // 检查邮箱是否被占用
        if (StrUtil.isNotBlank(user.getEmail()) && !user.getEmail().equals(existUser.getEmail())) {
            LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(SysUser::getEmail, user.getEmail());
            Long count = sysUserMapper.selectCount(wrapper);
            if (count > 0) {
                throw new BusinessException("邮箱已被注册");
            }
        }

        // 检查手机号是否被占用
        if (StrUtil.isNotBlank(user.getPhone()) && !user.getPhone().equals(existUser.getPhone())) {
            LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(SysUser::getPhone, user.getPhone());
            Long count = sysUserMapper.selectCount(wrapper);
            if (count > 0) {
                throw new BusinessException("手机号已被注册");
            }
        }

        // 更新用户信息
        user.setId(userId);
        user.setPassword(null); // 不允许直接修改密码
        int result = sysUserMapper.updateById(user);
        if (result <= 0) {
            throw new BusinessException("更新用户信息失败");
        }

        log.info("更新用户信息: userId={}", userId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteUser(Long userId) {
        // 检查是否为超级管理员（ID=1不允许删除）
        if (userId == 1L) {
            throw new BusinessException(ResultCode.FORBIDDEN, "不允许删除超级管理员");
        }

        // 逻辑删除
        int result = sysUserMapper.deleteById(userId);
        if (result <= 0) {
            throw new BusinessException("删除用户失败");
        }

        log.info("删除用户: userId={}", userId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUserStatus(Long userId, Integer status) {
        // 检查是否为超级管理员
        if (userId == 1L) {
            throw new BusinessException(ResultCode.FORBIDDEN, "不允许禁用超级管理员");
        }

        SysUser user = new SysUser();
        user.setId(userId);
        user.setStatus(status);
        
        int result = sysUserMapper.updateById(user);
        if (result <= 0) {
            throw new BusinessException("更新用户状态失败");
        }

        log.info("更新用户状态: userId={}, status={}", userId, status);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void resetPassword(Long userId, String newPassword) {
        SysUser user = new SysUser();
        user.setId(userId);
        user.setPassword(PasswordUtil.encode(newPassword));
        
        int result = sysUserMapper.updateById(user);
        if (result <= 0) {
            throw new BusinessException("重置密码失败");
        }

        log.info("重置用户密码: userId={}", userId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void changePassword(Long userId, String oldPassword, String newPassword) {
        // 查询用户
        SysUser user = sysUserMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException(ResultCode.NOT_FOUND, "用户不存在");
        }

        // 验证旧密码
        if (!PasswordUtil.matches(oldPassword, user.getPassword())) {
            throw new BusinessException("旧密码错误");
        }

        // 更新密码
        user.setPassword(PasswordUtil.encode(newPassword));
        int result = sysUserMapper.updateById(user);
        if (result <= 0) {
            throw new BusinessException("修改密码失败");
        }

        log.info("修改用户密码: userId={}", userId);
    }

    @Override
    public UserVO getCurrentUserInfo(Long userId) {
        if (userId == null) {
            throw new BusinessException(ResultCode.UNAUTHORIZED, "请先登录");
        }
        return getUserById(userId);
    }

    /**
     * 转换为VO
     */
    private UserVO convertToVO(SysUser user) {
        UserVO userVO = new UserVO();
        userVO.setId(user.getId());
        userVO.setUsername(user.getUsername());
        userVO.setNickname(user.getNickname());
        userVO.setEmail(user.getEmail());
        userVO.setPhone(user.getPhone());
        userVO.setAvatar(user.getAvatar());
        userVO.setStatus(user.getStatus());
        userVO.setLastLoginTime(user.getLastLoginTime());
        userVO.setLastLoginIp(user.getLastLoginIp());
        userVO.setCreateTime(user.getCreateTime());
        // TODO: 查询用户角色和权限
        return userVO;
    }
}

