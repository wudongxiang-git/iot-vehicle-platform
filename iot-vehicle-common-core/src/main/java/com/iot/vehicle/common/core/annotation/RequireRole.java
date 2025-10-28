package com.iot.vehicle.common.core.annotation;

import java.lang.annotation.*;

/**
 * 角色验证注解
 * 
 * 用于标记需要特定角色的接口
 * 
 * 使用示例：
 * @RequireRole("ROLE_ADMIN")
 * @RequireRole(value = {"ROLE_ADMIN", "ROLE_MANAGER"}, logical = Logical.OR)
 *
 * @author dongxiang.wu
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequireRole {

    /**
     * 需要的角色编码
     */
    String[] value() default {};

    /**
     * 逻辑关系（AND-需要所有角色，OR-只需任一角色）
     */
    Logical logical() default Logical.AND;

    /**
     * 逻辑关系枚举
     */
    enum Logical {
        /**
         * AND - 需要所有角色
         */
        AND,
        
        /**
         * OR - 只需任一角色
         */
        OR
    }
}

