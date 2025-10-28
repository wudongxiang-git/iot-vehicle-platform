package com.iot.vehicle.web.config;

import com.iot.vehicle.web.interceptor.JwtInterceptor;
import com.iot.vehicle.web.interceptor.LogInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web MVC配置
 *
 * @author dongxiang.wu
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private LogInterceptor logInterceptor;

    @Autowired
    private JwtInterceptor jwtInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 日志拦截器（记录所有请求）
        registry.addInterceptor(logInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/error",
                        "/doc.html",
                        "/webjars/**",
                        "/v3/api-docs/**",
                        "/swagger-ui/**",
                        "/swagger-resources/**",
                        "/favicon.ico",
                        "/druid/**"
                );

        // JWT拦截器（验证Token）
        registry.addInterceptor(jwtInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(
                        // 认证相关接口（无需登录）
                        "/auth/register",
                        "/auth/login",
                        "/auth/refresh",
                        // 健康检查接口
                        "/health",
                        "/health/**",
                        // API文档（Knife4j）
                        "/doc.html",
                        "/webjars/**",
                        "/v3/api-docs/**",
                        "/swagger-ui/**",
                        "/swagger-resources/**",
                        "/favicon.ico",
                        // 监控
                        "/druid/**",
                        "/actuator/**",
                        // 错误页面
                        "/error"
                )
                .order(2); // 在日志拦截器之后执行
    }
}

