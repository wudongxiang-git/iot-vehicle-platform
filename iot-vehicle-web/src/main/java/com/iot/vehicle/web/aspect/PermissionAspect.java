package com.iot.vehicle.web.aspect;

import com.iot.vehicle.common.core.annotation.RequirePermission;
import com.iot.vehicle.common.core.annotation.RequireRole;
import com.iot.vehicle.common.core.exception.BusinessException;
import com.iot.vehicle.common.core.result.ResultCode;
import com.iot.vehicle.common.web.util.UserContextUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * 权限验证切面
 * 
 * 功能：
 * 1. 验证@RequireRole注解标记的角色权限
 * 2. 验证@RequirePermission注解标记的功能权限
 *
 * @author dongxiang.wu
 */
@Slf4j
//@Aspect  // 暂时注释，调试启动问题
//@Component
@RequiredArgsConstructor
public class PermissionAspect {

    /**
     * 验证角色权限
     */
    @Before("@annotation(com.iot.vehicle.common.core.annotation.RequireRole) || " +
            "@within(com.iot.vehicle.common.core.annotation.RequireRole)")
    public void checkRole(JoinPoint joinPoint) {
        // 获取方法上的注解
        RequireRole requireRole = getMethodAnnotation(joinPoint, RequireRole.class);
        if (requireRole == null) {
            // 如果方法上没有，获取类上的注解
            requireRole = joinPoint.getTarget().getClass().getAnnotation(RequireRole.class);
        }

        if (requireRole == null || requireRole.value().length == 0) {
            return;
        }

        // 获取当前用户ID
        Long userId = UserContextUtil.getCurrentUserId();
        if (userId == null) {
            throw new BusinessException(ResultCode.UNAUTHORIZED, "请先登录");
        }

        // TODO: 查询用户角色并验证
        // 暂时跳过验证，等完成用户角色查询后再实现
        log.debug("角色验证（待实现）: userId={}, requiredRoles={}", userId, requireRole.value());
    }

    /**
     * 验证功能权限
     */
    @Before("@annotation(com.iot.vehicle.common.core.annotation.RequirePermission) || " +
            "@within(com.iot.vehicle.common.core.annotation.RequirePermission)")
    public void checkPermission(JoinPoint joinPoint) {
        // 获取方法上的注解
        RequirePermission requirePermission = getMethodAnnotation(joinPoint, RequirePermission.class);
        if (requirePermission == null) {
            // 如果方法上没有，获取类上的注解
            requirePermission = joinPoint.getTarget().getClass().getAnnotation(RequirePermission.class);
        }

        if (requirePermission == null || requirePermission.value().length == 0) {
            return;
        }

        // 获取当前用户ID
        Long userId = UserContextUtil.getCurrentUserId();
        if (userId == null) {
            throw new BusinessException(ResultCode.UNAUTHORIZED, "请先登录");
        }

        // TODO: 查询用户权限并验证
        // 暂时跳过验证，等完成用户权限查询后再实现
        log.debug("权限验证（待实现）: userId={}, requiredPermissions={}", userId, requirePermission.value());
    }

    /**
     * 获取方法上的注解
     */
    private <T extends java.lang.annotation.Annotation> T getMethodAnnotation(JoinPoint joinPoint, Class<T> annotationClass) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        return method.getAnnotation(annotationClass);
    }
}

