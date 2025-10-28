package com.iot.vehicle.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.iot.vehicle.api.dto.LoginDTO;
import com.iot.vehicle.api.dto.RegisterDTO;
import com.iot.vehicle.api.entity.SysUser;
import com.iot.vehicle.api.vo.LoginVO;
import com.iot.vehicle.api.vo.UserVO;
import com.iot.vehicle.common.core.exception.BusinessException;
import com.iot.vehicle.common.core.result.ResultCode;
import com.iot.vehicle.common.core.utils.JwtUtil;
import com.iot.vehicle.common.core.utils.PasswordUtil;
import com.iot.vehicle.service.mapper.SysUserMapper;
import com.iot.vehicle.service.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 认证服务实现
 *
 * @author dongxiang.wu
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final SysUserMapper sysUserMapper;

    /**
     * Token过期时间（7天）
     */
    private static final long TOKEN_EXPIRATION = 7 * 24 * 60 * 60 * 1000L;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void register(RegisterDTO registerDTO) {
        // 检查用户名是否已存在
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUser::getUsername, registerDTO.getUsername());
        Long count = sysUserMapper.selectCount(wrapper);
        if (count > 0) {
            throw new BusinessException("用户名已存在");
        }

        // 检查邮箱是否已存在（如果提供了邮箱）
        if (StrUtil.isNotBlank(registerDTO.getEmail())) {
            wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(SysUser::getEmail, registerDTO.getEmail());
            count = sysUserMapper.selectCount(wrapper);
            if (count > 0) {
                throw new BusinessException("邮箱已被注册");
            }
        }

        // 检查手机号是否已存在（如果提供了手机号）
        if (StrUtil.isNotBlank(registerDTO.getPhone())) {
            wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(SysUser::getPhone, registerDTO.getPhone());
            count = sysUserMapper.selectCount(wrapper);
            if (count > 0) {
                throw new BusinessException("手机号已被注册");
            }
        }

        // 创建用户
        SysUser user = new SysUser();
        user.setUsername(registerDTO.getUsername());
        user.setPassword(PasswordUtil.encode(registerDTO.getPassword())); // 加密密码
        user.setNickname(StrUtil.isNotBlank(registerDTO.getNickname()) 
                ? registerDTO.getNickname() : registerDTO.getUsername());
        user.setEmail(registerDTO.getEmail());
        user.setPhone(registerDTO.getPhone());
        user.setStatus(1); // 默认正常状态

        int result = sysUserMapper.insert(user);
        if (result <= 0) {
            throw new BusinessException("注册失败");
        }

        log.info("用户注册成功: username={}, id={}", user.getUsername(), user.getId());
    }

    @Override
    public LoginVO login(LoginDTO loginDTO) {
        // 查询用户
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUser::getUsername, loginDTO.getUsername());
        SysUser user = sysUserMapper.selectOne(wrapper);

        if (user == null) {
            throw new BusinessException(ResultCode.UNAUTHORIZED, "用户名或密码错误");
        }

        // 验证密码
        if (!PasswordUtil.matches(loginDTO.getPassword(), user.getPassword())) {
            throw new BusinessException(ResultCode.UNAUTHORIZED, "用户名或密码错误");
        }

        // 检查用户状态
        if (user.getStatus() == 0) {
            throw new BusinessException(ResultCode.FORBIDDEN, "账号已被禁用");
        }

        // 更新最后登录信息
        // TODO: 后续可以通过MQ异步更新
        updateLoginInfo(user.getId(), "127.0.0.1"); // IP地址暂时写死，后续从参数传入

        // 生成Token
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getId());
        claims.put("username", user.getUsername());
        String token = JwtUtil.generateToken(user.getId().toString(), claims);

        // 构建用户信息
        UserVO userVO = buildUserVO(user);

        // 构建返回结果
        LoginVO loginVO = new LoginVO();
        loginVO.setAccessToken(token);
        loginVO.setExpiresIn(TOKEN_EXPIRATION / 1000); // 转换为秒
        loginVO.setUserInfo(userVO);

        log.info("用户登录成功: username={}, userId={}", user.getUsername(), user.getId());

        return loginVO;
    }

    @Override
    public void logout(String token) {
        // TODO: 将Token加入黑名单（使用Redis）
        String userId = JwtUtil.getSubject(token);
        log.info("用户登出: userId={}", userId);
    }

    @Override
    public String refreshToken(String token) {
        // 验证Token是否有效
        if (!JwtUtil.validateToken(token)) {
            throw new BusinessException(ResultCode.UNAUTHORIZED, "Token无效或已过期");
        }

        // 刷新Token
        String newToken = JwtUtil.refreshToken(token);
        if (newToken == null) {
            throw new BusinessException("Token刷新失败");
        }

        return newToken;
    }

    /**
     * 更新登录信息
     */
    private void updateLoginInfo(Long userId, String loginIp) {
        SysUser updateUser = new SysUser();
        updateUser.setId(userId);
        updateUser.setLastLoginTime(LocalDateTime.now());
        updateUser.setLastLoginIp(loginIp);
        sysUserMapper.updateById(updateUser);
    }

    /**
     * 构建用户VO
     */
    private UserVO buildUserVO(SysUser user) {
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

