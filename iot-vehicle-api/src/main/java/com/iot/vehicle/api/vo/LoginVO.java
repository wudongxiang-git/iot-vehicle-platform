package com.iot.vehicle.api.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 登录返回VO
 *
 * @author dongxiang.wu
 */
@Data
public class LoginVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 访问令牌
     */
    private String accessToken;

    /**
     * 令牌类型
     */
    private String tokenType = "Bearer";

    /**
     * 过期时间（秒）
     */
    private Long expiresIn;

    /**
     * 用户信息
     */
    private UserVO userInfo;

    public LoginVO() {
    }

    public LoginVO(String accessToken, Long expiresIn, UserVO userInfo) {
        this.accessToken = accessToken;
        this.expiresIn = expiresIn;
        this.userInfo = userInfo;
    }
}

