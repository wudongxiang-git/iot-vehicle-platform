package com.iot.vehicle.web.interceptor;

import cn.hutool.core.util.StrUtil;
import com.iot.vehicle.common.core.exception.BusinessException;
import com.iot.vehicle.common.core.result.ResultCode;
import com.iot.vehicle.common.core.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * JWT拦截器
 * 
 * 功能：
 * 1. 从请求头中获取Token
 * 2. 验证Token的有效性
 * 3. 解析Token并将用户信息存入请求上下文
 *
 * @author dongxiang.wu
 */
@Slf4j
@Component
public class JwtInterceptor implements HandlerInterceptor {

    /**
     * Token请求头名称
     */
    private static final String TOKEN_HEADER = "Authorization";

    /**
     * Token前缀
     */
    private static final String TOKEN_PREFIX = "Bearer ";

    /**
     * 用户ID属性名
     */
    public static final String USER_ID_ATTR = "userId";

    /**
     * 用户名属性名
     */
    public static final String USERNAME_ATTR = "username";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 获取Token
        String token = getTokenFromRequest(request);
        
        if (StrUtil.isBlank(token)) {
            throw new BusinessException(ResultCode.UNAUTHORIZED, "请先登录");
        }

        // 验证Token
        if (!JwtUtil.validateToken(token)) {
            throw new BusinessException(ResultCode.UNAUTHORIZED, "Token无效或已过期，请重新登录");
        }

        // 解析Token
        Claims claims = JwtUtil.parseToken(token);
        if (claims == null) {
            throw new BusinessException(ResultCode.UNAUTHORIZED, "Token解析失败");
        }

        // 将用户信息存入请求属性
        Object userId = claims.get(USER_ID_ATTR);
        Object username = claims.get(USERNAME_ATTR);
        
        if (userId != null) {
            request.setAttribute(USER_ID_ATTR, userId);
        }
        if (username != null) {
            request.setAttribute(USERNAME_ATTR, username);
        }

        log.debug("JWT验证通过: userId={}, username={}", userId, username);

        return true;
    }

    /**
     * 从请求中获取Token
     *
     * @param request 请求对象
     * @return Token字符串
     */
    private String getTokenFromRequest(HttpServletRequest request) {
        String header = request.getHeader(TOKEN_HEADER);
        
        if (StrUtil.isBlank(header)) {
            return null;
        }

        if (header.startsWith(TOKEN_PREFIX)) {
            return header.substring(TOKEN_PREFIX.length());
        }

        return header;
    }
}

