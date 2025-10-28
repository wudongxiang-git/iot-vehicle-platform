package com.iot.vehicle.service.service;

import com.iot.vehicle.api.dto.LoginDTO;
import com.iot.vehicle.api.dto.RegisterDTO;
import com.iot.vehicle.api.vo.LoginVO;

/**
 * 认证服务接口
 *
 * @author dongxiang.wu
 */
public interface AuthService {

    /**
     * 用户注册
     *
     * @param registerDTO 注册信息
     * @return 注册结果
     */
    void register(RegisterDTO registerDTO);

    /**
     * 用户登录
     *
     * @param loginDTO 登录信息
     * @return 登录结果（包含Token）
     */
    LoginVO login(LoginDTO loginDTO);

    /**
     * 用户登出
     *
     * @param token Token
     */
    void logout(String token);

    /**
     * 刷新Token
     *
     * @param token 旧Token
     * @return 新Token
     */
    String refreshToken(String token);
}

