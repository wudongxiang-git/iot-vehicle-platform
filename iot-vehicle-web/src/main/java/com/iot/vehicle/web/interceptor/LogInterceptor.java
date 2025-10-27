package com.iot.vehicle.web.interceptor;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * 请求日志拦截器
 * 
 * 功能：
 * 1. 记录请求URL、方法、参数
 * 2. 记录请求响应时间
 * 3. 记录客户端IP地址
 *
 * @author dongxiang.wu
 */
@Slf4j
@Component
public class LogInterceptor implements HandlerInterceptor {

    /**
     * 请求开始时间
     */
    private static final ThreadLocal<Long> START_TIME = new ThreadLocal<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 记录开始时间
        START_TIME.set(System.currentTimeMillis());
        
        // 获取请求信息
        String method = request.getMethod();
        String uri = request.getRequestURI();
        String queryString = request.getQueryString();
        String ip = getClientIp(request);
        
        // 记录请求日志
        log.info("====> 请求开始: {} {} from {}", method, uri, ip);
        if (StrUtil.isNotBlank(queryString)) {
            log.info("      Query参数: {}", queryString);
        }
        
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
        // 可以在这里处理返回的ModelAndView
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        // 计算请求耗时
        long startTime = START_TIME.get();
        long endTime = System.currentTimeMillis();
        long costTime = endTime - startTime;
        
        // 获取请求信息
        String method = request.getMethod();
        String uri = request.getRequestURI();
        int status = response.getStatus();
        
        // 记录响应日志
        if (ex != null) {
            log.error("<==== 请求异常: {} {} [{}ms] 状态码: {} 异常: {}", 
                method, uri, costTime, status, ex.getMessage());
        } else {
            // 根据响应时间使用不同的日志级别
            if (costTime > 3000) {
                log.warn("<==== 请求完成(慢): {} {} [{}ms] 状态码: {}", method, uri, costTime, status);
            } else if (costTime > 1000) {
                log.info("<==== 请求完成: {} {} [{}ms] 状态码: {}", method, uri, costTime, status);
            } else {
                log.debug("<==== 请求完成: {} {} [{}ms] 状态码: {}", method, uri, costTime, status);
            }
        }
        
        // 清除ThreadLocal，防止内存泄漏
        START_TIME.remove();
    }

    /**
     * 获取客户端真实IP地址
     * 
     * @param request 请求对象
     * @return IP地址
     */
    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (isInvalidIp(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (isInvalidIp(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (isInvalidIp(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (isInvalidIp(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (isInvalidIp(ip)) {
            ip = request.getRemoteAddr();
        }
        
        // 处理多级代理的情况，取第一个IP
        if (StrUtil.isNotBlank(ip) && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }
        
        return ip;
    }

    /**
     * 判断IP是否无效
     */
    private boolean isInvalidIp(String ip) {
        return StrUtil.isBlank(ip) || "unknown".equalsIgnoreCase(ip);
    }
}

