package com.iot.vehicle.common.core.annotation;

import java.lang.annotation.*;

/**
 * 权限验证注解
 * 
 * 用于标记需要特定权限的接口
 * 
 * 使用示例：
 * @RequirePermission("system:user:add")
 * @RequirePermission(value = {"system:user:add", "system:user:edit"}, logical = Logical.OR)
 *
 * @author dongxiang.wu
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequirePermission {

    /**
     * 需要的权限编码
     */
    String[] value() default {};

    /**
     * 逻辑关系（AND-需要所有权限，OR-只需任一权限）
     */
    Logical logical() default Logical.AND;

    /**
     * 逻辑关系枚举
     */
    enum Logical {
        /**
         * AND - 需要所有权限
         */
        AND,
        
        /**
         * OR - 只需任一权限
         */
        OR
    }
}

